package com.yeon.mom.util;

import com.yeon.mom.event.Event;
import com.yeon.mom.event.EventType;
import com.yeon.mom.event.SimpleEventType;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventTypeUtils {
	static final Map<String, EventTypeInfo> EVENT_TYPE_INFOS;

	static {
		EVENT_TYPE_INFOS = new ConcurrentHashMap<String, EventTypeInfo>();
	}

	public static final boolean isQueueEventType(EventType eventType) {
		return EventType.CLASS_QUEUE.equals(eventType.getClassName());
	}

	public static void registEventType(EventType eventType, Class<? extends Event> clazz) {
		if (eventType == null || clazz == null) {
			throw new IllegalArgumentException();
		}

		EventTypeInfo eventTypeInfo = new EventTypeInfo(eventType, clazz);
		EVENT_TYPE_INFOS.put(EventTypeUtils.encode(eventType), eventTypeInfo);
	}

	public static final String encode(EventType eventType) {
		return encode(eventType.getClassName(), eventType.getOperationName());
	}

	public static String encode(String eventClassName, String eventOperationName) {
		return eventClassName + ":" + eventOperationName;
	}

	public static final EventType decode(String eventClassName, String eventOperationName) {
		if (eventClassName == null) {
			throw new IllegalArgumentException("Null eventClassName.");
		}
		if (eventOperationName == null) {
			throw new IllegalArgumentException("Null eventOperatioName.");
		}

		String eventCode = EventTypeUtils.encode(eventClassName, eventOperationName);
		return decode(eventCode);
	}

	public static final EventType decode(String eventCode) {
		if (eventCode == null) {
			throw new IllegalArgumentException("Null eventCode.");
		}

		EventTypeInfo eventTypeInfo = EVENT_TYPE_INFOS.get(eventCode);

		if (eventTypeInfo != null) {
			return eventTypeInfo.eventType;
		} else {
			String[] tokens = StringUtils.split(eventCode, "@:");
			if (tokens == null || tokens.length != 2) {
				throw new IllegalArgumentException("Invalid eventType format.(" + eventCode + ")");
			}

			return new SimpleEventType(tokens[0], tokens[1]);
		}
	}

	/**
	 * 
	 * @author pulsarang
	 */
	static class EventTypeInfo {
		final EventType eventType;
		final Class<? extends Event> clazz;

		private EventTypeInfo(EventType eventType, Class<? extends Event> clazz) {
			super();
			this.eventType = eventType;
			this.clazz = clazz;
		}
	}
}
