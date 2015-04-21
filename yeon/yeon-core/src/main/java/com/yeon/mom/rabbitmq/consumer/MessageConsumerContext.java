package com.yeon.mom.rabbitmq.consumer;

import com.yeon.YeonContext;
import com.yeon.YeonEnvironment;
import com.yeon.mom.EventProcessor;
import com.yeon.mom.EventProcessorInfo;
import com.yeon.mom.EventProcessorInfoId;
import com.yeon.mom.rabbitmq.consumer.mon.EventProcessorMonitor;
import com.yeon.mom.rabbitmq.consumer.mon.EventProcessorMonitorManager;
import com.yeon.mom.rabbitmq.consumer.mon.MergedEventProcessorInfo;
import com.yeon.mom.rabbitmq.consumer.mon.MessageConsumerContextMonitor;
import com.yeon.mom.util.EventProcessorValidationUtil;
import com.yeon.monitor.MomScheduledExecutor;
import com.yeon.monitor.collector.LocalMonitorablePropertyCollector;
import com.yeon.remote.RemoteContext;
import com.yeon.server.Solution;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author pulsarang
 */
public class MessageConsumerContext {
	private Map<String, MessageConsumerGroup> messageConsumerGroupMap;
	private EventProcessorMonitorManager eventProcessorMonitorManager;
	private LocalEventReceiver eventReceiver;
	private boolean forceSubscribeEvent;

	private MessageConsumerFactory messageConsumerFactory;
	private MessageConsumerContextMonitor messageConsumerContextMonitor;

	public void init(boolean forceSubscribeEvent) {
		this.forceSubscribeEvent = forceSubscribeEvent;

		YeonEnvironment environment = YeonContext.getEnvironment();
		RemoteContext remoteContext = YeonContext.getRemoteContext();

		eventProcessorMonitorManager = new EventProcessorMonitorManager();
		remoteContext.setRemoteService(EventProcessorMonitorManager.NAME, eventProcessorMonitorManager);

		switch (environment) {
		case TEST:
			break;
		case LOCAL:
		case CI:
			eventReceiver = new LocalEventReceiver();
			remoteContext.setRemoteService(LocalEventReceiver.NAME, eventReceiver);
			break;
		default:
			LocalMonitorablePropertyCollector collector = LocalMonitorablePropertyCollector.getInstance();
			collector.setMonitorable(EventProcessorMonitorManager.NAME, eventProcessorMonitorManager);
			break;
		}

		messageConsumerGroupMap = new ConcurrentHashMap<String, MessageConsumerGroup>();
		MergedEventProcessorInfo.registReloadService(Collections.unmodifiableMap(messageConsumerGroupMap));

		messageConsumerFactory = createMessageConsumerFactory();

		messageConsumerContextMonitor = new MessageConsumerContextMonitor(this);
		remoteContext.setRemoteService(MessageConsumerContextMonitor.NAME, messageConsumerContextMonitor);
		MomScheduledExecutor.scheduleAtFixedRate(messageConsumerContextMonitor, 1, 60, TimeUnit.SECONDS);
	}

	public Map<String, MessageConsumerGroup> getMessageConsumerGroupMap() {
		return Collections.unmodifiableMap(messageConsumerGroupMap);
	}

	public MessageConsumerGroup getMessageConsumerGroup(String processorName) {
		return messageConsumerGroupMap.get(processorName);
	}

	public EventProcessorMonitorManager getEventProcessorMonitorManager() {
		return eventProcessorMonitorManager;
	}

	public LocalEventReceiver getEventReceiver() {
		return eventReceiver;
	}

	public boolean isForceSubscribeEvent() {
		return forceSubscribeEvent;
	}

	public MessageConsumerFactory getMessageConsumerFactory() {
		return messageConsumerFactory;
	}

	private MessageConsumerFactory createMessageConsumerFactory() {
		YeonEnvironment environment = YeonContext.getEnvironment();

		if (environment == YeonEnvironment.TEST || Solution.SOLUTION_DUMMY.equals(YeonContext.getSolutionName())) {
			return new MessageConsumerFactory() {
				@Override
				public MessageConsumer createMomMessageConsumer(int id, EventProcessorExecutionInfo executionInfo) {
					return new NullMessageConsumer(executionInfo.getEventProcessor());
				}
			};
		} else if (!forceSubscribeEvent && (environment == YeonEnvironment.LOCAL || environment == YeonEnvironment.CI)) {
			return new MessageConsumerFactory() {
				@Override
				public MessageConsumer createMomMessageConsumer(int id, EventProcessorExecutionInfo executionInfo) {
					LocalMessageConsumer localMessageConsumer = new LocalMessageConsumer(executionInfo.getEventProcessor());
					eventReceiver.addMessageConsumer(localMessageConsumer);
					return localMessageConsumer;
				}
			};
		} else {
			return new MessageConsumerFactory() {
				@Override
				public MessageConsumer createMomMessageConsumer(int id, EventProcessorExecutionInfo executionInfo) {
					return new RabbitMqMessageConsumer(executionInfo);
				}
			};
		}
	}

	public MessageConsumerGroup add(EventProcessor eventProcessor) {
		EventProcessorValidationUtil.checkValid(eventProcessor);

		SkipCheckEventProcessor skipCheckEventProcessor = new SkipCheckEventProcessor(eventProcessor);
		EventProcessorMonitor monitoredEventProcessor = eventProcessorMonitorManager.add(skipCheckEventProcessor);
		EventProcessorExecutionInfo executionInfo = new EventProcessorExecutionInfo(monitoredEventProcessor);
		YeonContext.getResourceContext().addListener(executionInfo);

		MessageConsumerGroup messageConsumerGroup = new MessageConsumerGroup(executionInfo, messageConsumerFactory);
		messageConsumerGroup.start();

		messageConsumerGroupMap.put(eventProcessor.getName(), messageConsumerGroup);

		return messageConsumerGroup;
	}

	public void refreshMessageConsumer(EventProcessorInfo newEventProcessorInfo, EventProcessorInfo oldEventProcessorInfo) {
		String simpleEventProcessorName = EventProcessorInfoId.getSimpleEventProcessorName(newEventProcessorInfo.getName());
		MessageConsumerGroup messageConsumer = messageConsumerGroupMap.get(simpleEventProcessorName);
		if (messageConsumer == null) {
			return;
		}

		if (oldEventProcessorInfo.isSubscribeEnabled()) {
			if (!newEventProcessorInfo.isSubscribeEnabled()) {
				messageConsumer.stop();
				return;
			}
		} else {
			if (newEventProcessorInfo.isSubscribeEnabled()) {
				messageConsumer.start();
				return;
			}
		}

		if (oldEventProcessorInfo.getThreadCount() != newEventProcessorInfo.getThreadCount()) {
			messageConsumer.setThreadCount(newEventProcessorInfo.getThreadCount());
		}
	}

	public synchronized void attachMessageConsumerAll() {
		for (MessageConsumerGroup messageConsumerGroup : messageConsumerGroupMap.values()) {
			messageConsumerGroup.start();
		}
	}

	public synchronized void detachMessageConsumerAll() {
		for (MessageConsumerGroup messageConsumerGroup : messageConsumerGroupMap.values()) {
			messageConsumerGroup.stop();
		}
	}

	public void reconnectMessageConsumerAll() {
		for (MessageConsumerGroup messageConsumerGroup : messageConsumerGroupMap.values()) {
			messageConsumerGroup.reconnect();
		}
	}

	public EventProcessor getEventProcessor(String name) {
		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("Empty event processor name.");
		}

		MessageConsumerGroup messageConsumerGroup = messageConsumerGroupMap.get(name);
		if (messageConsumerGroup == null) {
			return null;
		}

		return messageConsumerGroup.getEventProcessor();
	}

	public List<EventProcessor> getEventProcessors() {
		List<EventProcessor> list = new ArrayList<EventProcessor>(messageConsumerGroupMap.size());
		for (MessageConsumerGroup messageConsumerGroup : messageConsumerGroupMap.values()) {
			list.add(messageConsumerGroup.getEventProcessor());
		}

		return list;
	}

	public void stop() {
		for (MessageConsumerGroup messageConsumerGroup : messageConsumerGroupMap.values()) {
			messageConsumerGroup.stop();
		}
	}
}
