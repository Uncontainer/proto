package com.yeon.mom.rabbitmq.consumer;

import com.yeon.mom.EventProcessor;
import com.yeon.mom.event.Event;
import com.yeon.mom.event.EventType;
import com.yeon.mom.service.ServiceWorkerStatus;
import com.yeon.mom.util.EventProcessorValidationUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author pulsarang
 */
public class LocalMessageConsumer implements MessageConsumer {
	private final EventProcessor eventProcessor;

	private final Set<String> subscribeEventTypeCodes;

	private long lastReceiveMessageTime;
	private int receiveMessageCount;

	public LocalMessageConsumer(EventProcessor eventProcessor) {
		super();
		this.eventProcessor = eventProcessor;
		EventProcessorValidationUtil.checkValid(eventProcessor);

		this.subscribeEventTypeCodes = new HashSet<String>();
		for (EventType eventType : eventProcessor.getSubscribeEvents()) {
			subscribeEventTypeCodes.add(eventType.getCode());
		}
	}

	public void process(Event event) {
		if (!subscribeEventTypeCodes.contains(event.getEventType().getCode())) {
			return;
		}

		lastReceiveMessageTime = System.currentTimeMillis();
		receiveMessageCount++;

		eventProcessor.process(new SimpleEventProcessContext(event));
	}

	@Override
	public boolean start() {
		return true;
	}

	@Override
	public void stop() {
	}

	public EventProcessor getEventProcessor() {
		return eventProcessor;
	}

	@Override
	public ServiceWorkerStatus getStatus(boolean checkAlive) {
		return ServiceWorkerStatus.MSG_WAIT;
	}

	@Override
	public long getIdleSinceTime() {
		return lastReceiveMessageTime;
	}

	@Override
	public int getReceiveCount() {
		return receiveMessageCount;
	}
}
