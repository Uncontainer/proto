package com.yeon.mom;

import com.yeon.mom.event.EventProcessOption;

/**
 * 
 * @author pulsarang
 */
@SuppressWarnings("serial")
public class ProcessWaitTimeoutException extends ReprocessableException {

	public ProcessWaitTimeoutException(EventProcessContext context, String message) {
		super(message);

		EventProcessOption eventProcessOption = context.getEvent().getEventProcessOption();
		if (eventProcessOption != null) {
			setArguments(eventProcessOption.getArguments());
		}
	}
}
