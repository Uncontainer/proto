package com.yeon.mom.rabbitmq.consumer;

import com.yeon.mom.event.Event;

/**
 * 
 * @author pulsarang
 */
@SuppressWarnings("serial")
public class SkipProcessException extends RuntimeException {
	protected final Event event;
	protected final String reason;

	public SkipProcessException(Event event, String reason) {
		this.event = event;
		this.reason = reason;
	}

	public Event getEvent() {
		return event;
	}

	public String getReason() {
		return reason;
	}
}
