package com.pulsarang.infra.mom;

import com.pulsarang.infra.mom.event.EventProcessOption;

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
