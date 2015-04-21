package com.yeon.mom.rabbitmq.consumer;

import com.yeon.mom.event.Event;

/**
 * 
 * @author pulsarang
 */
@SuppressWarnings("serial")
public class StoppedProcessorEventException extends SkipProcessException {
	public static final String REASON = "_stopped_processor";

	public StoppedProcessorEventException(Event event) {
		super(event, REASON);
	}
}
