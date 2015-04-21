/*
 * EventProcessScheduleContext.java $version 2010. 10. 15
 * 
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.pulsarang.mom.dispatcher.processor;

import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.infra.mom.event.EventProcessOption;
import com.pulsarang.mom.store.EventEnvelope;

/**
 * 
 * @author pulsarang
 */
public class EventProcessContext {
	private EventEnvelope eventEnvelope;
	private ProcessorEventQueue processorEventQueue;

	public EventProcessContext(EventEnvelope eventEnvelope, ProcessorEventQueue processorEventQueue) {
		if (eventEnvelope == null) {
			throw new IllegalArgumentException();
		}

		EventProcessOption eventProcessOption = eventEnvelope.getEvent().getEventProcessOption();
		if (eventProcessOption == null) {
			throw new IllegalArgumentException();
		}

		this.eventEnvelope = eventEnvelope;
		this.processorEventQueue = processorEventQueue;
	}

	public ProcessorEventQueue getProcessorEventQueue() {
		return processorEventQueue;
	}

	public EventEnvelope getEventEnvelope() {
		return eventEnvelope;
	}

	public Event getEvent() {
		return eventEnvelope.getEvent();
	}

	public <T extends Event> T getEvent(Class<T> clazz) {
		Event event = getEvent();
		return event.convert(clazz);
	}

	public boolean isReprocess() {
		return getEvent().getEventProcessOption() != null;
	}
}
