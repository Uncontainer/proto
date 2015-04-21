package com.yeon.mom.rabbitmq.consumer;

import com.yeon.mom.EventProcessContext;
import com.yeon.mom.event.Event;

import java.util.Map;

/**
 * 
 * @author pulsarang
 */
public class SimpleEventProcessContext implements EventProcessContext {
	Event event;
	int tryCount;
	Map<String, Object> arguments;

	public SimpleEventProcessContext(Event event) {
		this(event, null);
	}

	public SimpleEventProcessContext(Event event, Map<String, Object> arguments) {
		super();
		this.event = event;
		this.arguments = arguments;
	}

	@Override
	public Event getEvent() {
		return event;
	}

	@Override
	public Map<String, Object> getReprocessArguments() {
		return arguments;
	}

	@Override
	public boolean isReprocess() {
		return arguments != null;
	}

	@Override
	public int getTryCount() {
		return tryCount;
	}

	@Override
	public void setTryCount(int tryCount) {
		this.tryCount = tryCount;
	}

	@Override
	public int getMaxTryCount() {
		return 1;
	}
}
