package com.pulsarang.infra.mom.sxb.processor;

import java.util.HashSet;
import java.util.Set;

import com.pulsarang.infra.mom.EventProcessor;
import com.pulsarang.infra.mom.event.EventType;

public class LocalEventProcessorExecutor implements EventProcessorExecutor {

	private BasicEventProcessorExecutor eventProcessorExecutor;
	private Set<String> subscribeeventCodes;

	public LocalEventProcessorExecutor(EventProcessor eventProcessor) {
		this.eventProcessorExecutor = new BasicEventProcessorExecutor(eventProcessor);
		subscribeeventCodes = new HashSet<String>();

		for (EventType eventType : eventProcessorExecutor.getEventProcessor().getSubscribeEventTypes()) {
			subscribeeventCodes.add(eventType.getCode());
		}
	}

	@Override
	public EventProcessor getEventProcessor() {
		return eventProcessorExecutor.getEventProcessor();
	}

	@Override
	public EventProcessResponse execute(EventProcessRequest request) {
		String eventCode = request.getEvent().getEventType().getCode();
		if (!subscribeeventCodes.contains(eventCode)) {
			// TODO 다른 코드로...
			return EventProcessResponse.SUCCESS;
		}

		return eventProcessorExecutor.execute(request);
	}

}
