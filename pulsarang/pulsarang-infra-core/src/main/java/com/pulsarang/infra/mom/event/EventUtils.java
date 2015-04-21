package com.pulsarang.infra.mom.event;

public class EventUtils {
	public static final Event fromJson(String json) {
		return convertEvent(Event.fromJson(json, Event.class));
	}

	public static final Event convertEvent(Event event) {
		EventTypeUtils.EventTypeInfo eventTypeInfo = EventTypeUtils.eventTypeInfos.get(event.getEventType().getCode());
		if (eventTypeInfo == null) {
			return event;
		}

		if (eventTypeInfo.clazz == event.getClass()) {
			return event;
		}

		return Event.fromMap(event.getProperties(), eventTypeInfo.clazz);
	}

}
