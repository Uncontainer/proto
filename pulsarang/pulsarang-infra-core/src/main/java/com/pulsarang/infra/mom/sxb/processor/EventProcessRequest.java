package com.pulsarang.infra.mom.sxb.processor;

import java.util.Map;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.infra.mom.event.EventUtils;

public class EventProcessRequest extends MapModel {
	public static final String PARAM_PROCESSOR_NAME = "proc_name";
	public static final String PARAM_EVENT = "event";

	public String getProcessorName() {
		return getString(PARAM_PROCESSOR_NAME);
	}

	public void setProcessorName(String processorName) {
		setString(PARAM_PROCESSOR_NAME, processorName);
	}

	public Event getEvent() {
		Map<String, Object> mapEvent = getMap(PARAM_EVENT);
		return EventUtils.convertEvent(new Event(mapEvent));
	}

	public void setEvent(Event event) {
		setMap(PARAM_EVENT, event);
	}
}
