package com.yeon.mom.rabbitmq.consumer;

import com.yeon.api.ApiServiceFactory;
import com.yeon.mom.EventProcessor;
import com.yeon.mom.EventProcessorInfo;
import com.yeon.mom.MomInfo;
import com.yeon.mom.ProjectInfo;
import com.yeon.mom.event.EventApiService;
import com.yeon.mom.event.EventType;
import com.yeon.mom.rabbitmq.RabbitMqUtils;
import com.yeon.mom.service.ServiceRunStatus;
import com.yeon.mom.service.ServiceWorkerStatus;
import com.yeon.mom.util.EventProcessorValidationUtil;
import com.yeon.mom.util.TicketUtils;
import com.yeon.monitor.alarm.AlarmLog;
import com.yeon.monitor.alarm.AlarmLogType;
import com.yeon.server.Server;
import com.yeon.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link EventProcessor}가 수신하는 event를 {@link MessageConsumer}에 연결하는 작업을 한다.
 * @author pulsarang
 * 
 * @see RabbitMqMessageConsumer
 */
public class MessageConsumerGroup {
	private final Logger log = LoggerFactory.getLogger(MessageConsumerGroup.class);

	private static AtomicInteger seed = new AtomicInteger();

	private final EventProcessorExecutionInfo executionInfo;

	MessageConsumerFactory messageConsumerFactory;

	private final Map<Integer, MessageConsumer> messageConsumerMap = new HashMap<Integer, MessageConsumer>();

	private volatile ServiceRunStatus status;
	private final AtomicInteger addCount = new AtomicInteger();
	private final AtomicInteger removeCount = new AtomicInteger();

	private final EventApiService eventApi = ApiServiceFactory.getEventApiService();

	public MessageConsumerGroup(EventProcessorExecutionInfo executionInfo, MessageConsumerFactory messageConsumerFactory) {
		this.executionInfo = executionInfo;
		this.status = ServiceRunStatus.INIT_WAIT;

		EventProcessorValidationUtil.checkValid(executionInfo.getEventProcessor());

		subscribeAll();

		this.messageConsumerFactory = messageConsumerFactory;

		notifyEventProcessor(executionInfo.getEventProcessor());
	}

	public EventProcessorInfo getEventProcessorInfo() {
		return executionInfo.getEventProcessorInfo();
	}

	public EventProcessor getEventProcessor() {
		return executionInfo.getEventProcessor();
	}

	public EventProcessorExecutionInfo getExecutionInfo() {
		return executionInfo;
	}

	public Map<Integer, MessageConsumer> getMessageConsumerMap() {
		return Collections.unmodifiableMap(messageConsumerMap);
	}

	public synchronized boolean start() {
		if (!status.isClose() && status != ServiceRunStatus.INIT_WAIT) {
			return false;
		}

		if (!isSubscribeEnabled()) {
			status = ServiceRunStatus.PAUSE;
			log.info("[YEON] EventProcessor is disabled.({})", executionInfo.getCanonicalName());
			return false;
		}

		status = ServiceRunStatus.INITIALIZING;
		try {
			setThreadCount(executionInfo.getEventProcessorInfo().getThreadCount());
		} catch (Exception e) {
			status = ServiceRunStatus.INIT_FAIL;
			ExceptionUtils.launderThrowable(e);
		}

		status = ServiceRunStatus.NORMAL;

		return true;
	}

	public synchronized void stop() {
		if (status.isClose()) {
			return;
		}

		status = ServiceRunStatus.STOP_WAIT;

		log.info("[YEON] Unsubscribe eventProcessor '{}'", executionInfo.getCanonicalName());

		setThreadCount(0);
		status = ServiceRunStatus.STOP;
	}

	public synchronized void reconnect() {
		subscribeAll();

		if (!isSubscribeEnabled()) {
			return;
		}

		for (MessageConsumer consumer : messageConsumerMap.values()) {
			consumer.stop();
		}

		setThreadCount(executionInfo.getEventProcessorInfo().getThreadCount());
	}

	public synchronized void ensureLiveThreadCount() {
		if (status != ServiceRunStatus.NORMAL) {
			return;
		}

		List<Integer> stopedMessageConsumerIds = null;

		for (Entry<Integer, MessageConsumer> entry : messageConsumerMap.entrySet()) {
			MessageConsumer consumer = entry.getValue();
			ServiceWorkerStatus consumerStatus = consumer.getStatus(true);
			if (!consumerStatus.isGarbage()) {
				continue;
			}

			if (stopedMessageConsumerIds == null) {
				stopedMessageConsumerIds = new ArrayList<Integer>(2);
			}

			stopedMessageConsumerIds.add(entry.getKey());

			if (consumerStatus == ServiceWorkerStatus.UNEXPECTED_STOP) {
				AlarmLog alarmLog = new AlarmLog(AlarmLogType.ALT_MESSAGE_CONSUMER_STOP, "EventConsumer stopped unexpectedly.", null, executionInfo.getCanonicalName() + "#" + entry.getKey());
				ApiServiceFactory.getMonitorApiService().logAlarm(alarmLog);
			}
		}

		if (stopedMessageConsumerIds == null) {
			return;
		}

		for (int stopedMessageConsumerId : stopedMessageConsumerIds) {
			remove(stopedMessageConsumerId);
		}

		setThreadCount(executionInfo.getEventProcessorInfo().getThreadCount());
	}

	public synchronized void setThreadCount(int count) {
		if (count < 0) {
			count = 0;
		} else if (count > 32) {
			count = 32;
		}

		final int liveThreadCount = getLiveThreadCount();
		if (liveThreadCount > count) {
			ArrayList<Integer> ids = new ArrayList<Integer>(messageConsumerMap.keySet());
			for (int i = 0, n = liveThreadCount - count; i < n; i++) {
				remove(ids.get(i));
			}
		} else if (liveThreadCount < count) {
			for (int i = 0, n = count - liveThreadCount; i < n; i++) {
				add();
			}
		}
	}

	public synchronized int getLiveThreadCount() {
		int liveThreadCount = 0;
		for (MessageConsumer consumer : messageConsumerMap.values()) {
			if (!consumer.getStatus(true).isClose()) {
				liveThreadCount++;
			}
		}

		return liveThreadCount;
	}

	private synchronized MessageConsumer add() {
		addCount.incrementAndGet();

		int id = seed.incrementAndGet();
		MessageConsumer consumer = messageConsumerFactory.createMomMessageConsumer(id, executionInfo);
		consumer.start();
		messageConsumerMap.put(id, consumer);

		return consumer;
	}

	private MessageConsumer remove(int id) {
		removeCount.incrementAndGet();

		MessageConsumer consumer;
		synchronized (this) {
			consumer = messageConsumerMap.remove(id);
		}

		if (consumer != null) {
			consumer.stop();
			return consumer;
		}

		return null;
	}

	public ServiceRunStatus getStatus() {
		return status;
	}

	public int getAddCount() {
		return addCount.get();
	}

	public int getRemoveCount() {
		return removeCount.get();
	}

	private void subscribeAll() {
		try {
			RabbitMqUtils.subscribeAll(executionInfo.getCanonicalName(), executionInfo.getEventProcessor().getSubscribeEvents());
		} catch (IOException e) {
			log.error("[YEON] Fail to subscribe events.(" + executionInfo.getEventProcessor().getSubscribeEvents() + ")", e);
		}
	}

	private void notifyEventProcessor(EventProcessor eventProcessor) {
		EventProcessorInfo eventProcessorInfo = new EventProcessorInfo();
		eventProcessorInfo.setName(executionInfo.getCanonicalName());

		List<? extends EventType> eventTypes = eventProcessor.getSubscribeEvents();
		List<String> eventCodes = new ArrayList<String>(eventTypes.size());
		for (EventType eventType : eventTypes) {
			eventCodes.add(eventType.getCode());
		}
		eventProcessorInfo.setSubscribeEventCodes(eventCodes);

		try {
			eventApi.registEventProcessor(eventProcessorInfo);
		} catch (RuntimeException ignore) {
			log.info("[YEON] fail to regist eventProcessor to admin.");
		}
	}

	public boolean isSubscribeEnabled() {
		MomInfo momInfo = TicketUtils.getMomInfo();
		ProjectInfo projectInfo = TicketUtils.getProjectInfo();
		Server server = TicketUtils.getServer();

		if (projectInfo == null || server == null) {
			log.warn("[YEON] Not registered server.");
			return false;
		}

		if (!momInfo.getMomStatus().isProcessEnabled()) {
			return false;
		}

		if (!projectInfo.isSubscribeEnabled()) {
			return false;
		}

		if (!server.isNormal()) {
			return false;
		}

		return executionInfo.getEventProcessorInfo().isSubscribeEnabled();
	}
}
