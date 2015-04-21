package com.pulsarang.infra.mom.sxb.processor;

import com.pulsarang.infra.mom.EventProcessor;

public interface EventProcessorExecutor {

	EventProcessor getEventProcessor();

	EventProcessResponse execute(EventProcessRequest request);

}