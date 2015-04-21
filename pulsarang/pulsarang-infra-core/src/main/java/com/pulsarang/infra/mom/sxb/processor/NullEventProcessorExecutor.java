package com.pulsarang.infra.mom.sxb.processor;

import com.pulsarang.infra.mom.EventProcessor;

public class NullEventProcessorExecutor implements EventProcessorExecutor {

	private EventProcessor eventProcessor;

	public NullEventProcessorExecutor(EventProcessor eventProcessor) {
		this.eventProcessor = eventProcessor;
	}

	@Override
	public EventProcessor getEventProcessor() {
		return eventProcessor;
	}

	@Override
	public EventProcessResponse execute(EventProcessRequest request) {
		// TODO 다른 response로 주는 것이 나아 보임...
		return EventProcessResponse.SUCCESS;
	}

}
