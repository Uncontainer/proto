package com.yeon.mom.rabbitmq.consumer;

import com.yeon.mom.EventProcessContext;
import com.yeon.mom.EventProcessor;
import com.yeon.mom.EventProcessorInfo;
import com.yeon.mom.EventProcessorInfoId;
import com.yeon.mom.event.Event;
import com.yeon.mom.event.EventType;
import com.yeon.mom.service.ServiceStatus;
import com.yeon.mom.util.TicketUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author pulsarang
 */
public class SkipCheckEventProcessor implements EventProcessor {
	private final EventProcessor eventProcessor;
	private final EventProcessorInfo eventProcessorInfo;

	private final Set<String> subscribeEventCodes;

	public SkipCheckEventProcessor(EventProcessor eventProcessor) {
		this.eventProcessor = eventProcessor;
		this.eventProcessorInfo = TicketUtils.getEventProcessorInfo(EventProcessorInfoId.getCanonicalEventProcessorName(eventProcessor.getName()));

		subscribeEventCodes = new HashSet<String>();
		for (EventType subscribeEventType : eventProcessor.getSubscribeEvents()) {
			subscribeEventCodes.add(subscribeEventType.getCode());
		}
	}

	public EventProcessor getEventProcessor() {
		return eventProcessor;
	}

	@Override
	public String getName() {
		return eventProcessor.getName();
	}

	@Override
	public List<? extends EventType> getSubscribeEvents() {
		return eventProcessor.getSubscribeEvents();
	}

	@Override
	public void process(EventProcessContext context) {
		checkSkip(context);

		eventProcessor.process(context);
	}

	@Override
	public void reprocess(EventProcessContext context) {
		checkSkip(context);

		eventProcessor.reprocess(context);
	}

	@Override
	public void verify(EventProcessContext context) {
		checkSkip(context);

		eventProcessor.verify(context);
	}

	@Override
	public void fail(EventProcessContext context) {
		checkSkip(context);

		eventProcessor.fail(context);
	}

	private void checkSkip(EventProcessContext context) {
		Event event = context.getEvent();
		if (!subscribeEventCodes.contains(event.getEventType().getCode())) {
			throw new UnsubscribeEventException(event);
		}

		if (eventProcessorInfo.getStatus() == ServiceStatus.STOP) {
			throw new StoppedProcessorEventException(event);
		}

		if (eventProcessorInfo.isExpired(event)) {
			throw new ExpiredEventException(event);
		}
	}
}
