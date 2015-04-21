package com.pulsarang.mom.store;

import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.infra.mom.processor.EventProcessorInfo;

public interface EventEnvelope {
	Event getEvent();

	boolean mark();

	void acknowlege();

	void acknowlege(Exception e);

	void postpone(long time);

	void postpone(long time, EventProcessorInfo processorInfo);

	void expire(EventProcessorInfo processorInfo);

	void discard(EventProcessorInfo processorInfo);

	void cancel();
}
