package com.yeon.mom.rabbitmq.consumer;

import com.yeon.mom.event.Event;

/**
 * 
 * @author pulsarang
 */
@SuppressWarnings("serial")
public class ExpiredEventException extends SkipProcessException {
	public static final String REASON = "_expried_event";

	public ExpiredEventException(Event event) {
		super(event, REASON);
	}
}
