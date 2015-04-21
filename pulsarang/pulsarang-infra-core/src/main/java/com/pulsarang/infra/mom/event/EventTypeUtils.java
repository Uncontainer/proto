package com.pulsarang.infra.mom.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

public class EventTypeUtils {
	static final Map<String, EventTypeInfo> eventTypeInfos;

	static {
		eventTypeInfos = new ConcurrentHashMap<String, EventTypeInfo>();
	}

	public static void registEventType(EventType eventType, Class<? extends Event> clazz) {
		if (eventType == null || clazz == null) {
			throw new IllegalArgumentException();
		}

		EventTypeInfo eventTypeInfo = new EventTypeInfo(eventType, clazz);
		eventTypeInfos.put(EventTypeUtils.encode(eventType), eventTypeInfo);
	}

	public static final String encode(EventType eventType) {
		return encode(eventType.getClassName(), eventType.getOperationName());
	}

	public static String encode(String eventClassName, String eventOperationName) {
		return eventClassName + ":" + eventOperationName;
	}

	public static final EventType decode(String eventClassName, String eventOperationName) {
		if (eventClassName == null || eventOperationName == null) {
			throw new IllegalArgumentException();
		}

		String eventCode = EventTypeUtils.encode(eventClassName, eventOperationName);
		return decode(eventCode);
	}

	public static final EventType decode(String eventCode) {
		if (eventCode == null) {
			throw new IllegalArgumentException("Null eventCode.");
		}

		EventTypeInfo eventTypeInfo = eventTypeInfos.get(eventCode);

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

	static class EventTypeInfo {
		final EventType eventType;
		final Class<? extends Event> clazz;

		private EventTypeInfo(EventType eventType, Class<? extends Event> clazz) {
			super();
			this.eventType = eventType;
			this.clazz = clazz;
		}
	}

	public static boolean isQueueEventType(EventType eventType) {
		// TODO Auto-generated method stub
		return false;
	}
}
