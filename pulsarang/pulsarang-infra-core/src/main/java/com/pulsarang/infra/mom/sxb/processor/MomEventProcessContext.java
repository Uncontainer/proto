package com.pulsarang.infra.mom.sxb.processor;

import java.util.Map;

import com.pulsarang.infra.mom.EventProcessContext;
import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.infra.mom.event.EventProcessOption;

/**
 * 
 * @author pulsarang
 */
public class MomEventProcessContext implements EventProcessContext {

	private final Event event;

	public MomEventProcessContext(Event event) {
		super();
		if (event == null) {
			throw new IllegalArgumentException();
		}

		this.event = event;
	}

	@Override
	public Event getEvent() {
		return event;
	}

	@Override
	public Map<String, Object> getReprocessArguments() {
		EventProcessOption eventProcessOption = event.getEventProcessOption();
		if (eventProcessOption == null) {
			return null;
		}

		return eventProcessOption.getArguments();
	}

	@Override
	public boolean isReprocess() {
		return event.getEventProcessOption() != null;
	}
}
