package com.yeon.mom.rabbitmq.consumer;

import com.yeon.mom.event.Event;

/**
 * 
 * @author pulsarang
 */
@SuppressWarnings("serial")
public class UnsubscribeEventException extends SkipProcessException {
	public static final String REASON = "_unsubscribe_event";

	public UnsubscribeEventException(Event event) {
		super(event, REASON);
	}
}
