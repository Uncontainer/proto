package com.yeon.mom.util;

import com.yeon.mom.event.Event;

/**
 * 
 * @author pulsarang
 */
public class EventUtils {
	public static final Event convertEvent(Event event) {
		if (event == null) {
			return null;
		}

		EventTypeUtils.EventTypeInfo eventTypeInfo = EventTypeUtils.EVENT_TYPE_INFOS.get(event.getEventType().getCode());
		if (eventTypeInfo == null) {
			return event;
		}

		if (eventTypeInfo.clazz == event.getClass()) {
			return event;
		}

		return Event.fromMap(event.getValues(), eventTypeInfo.clazz);
	}

}
