package com.pulsarang.mom.queue;

import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.mom.store.EventEnvelope;

public interface EventQueue {
	void push(Event event) throws InterruptedException;

	EventEnvelope pop() throws InterruptedException;

	EventEnvelope popRunningEvent();

	void close();
}
