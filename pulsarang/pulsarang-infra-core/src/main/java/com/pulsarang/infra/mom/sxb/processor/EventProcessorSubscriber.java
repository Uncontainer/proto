package com.pulsarang.infra.mom.sxb.processor;

import com.pulsarang.infra.mom.EventProcessor;

public interface EventProcessorSubscriber {
	EventProcessor getEventProcessor();

	void open();

	void close();

	boolean isClosed();
}