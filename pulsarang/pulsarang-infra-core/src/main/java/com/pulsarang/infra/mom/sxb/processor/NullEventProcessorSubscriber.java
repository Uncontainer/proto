package com.pulsarang.infra.mom.sxb.processor;

import com.pulsarang.infra.mom.EventProcessor;

public class NullEventProcessorSubscriber implements EventProcessorSubscriber {

	@Override
	public EventProcessor getEventProcessor() {
		return null;
	}

	@Override
	public void open() {
	}

	@Override
	public void close() {
	}

	@Override
	public boolean isClosed() {
		return false;
	}

}
