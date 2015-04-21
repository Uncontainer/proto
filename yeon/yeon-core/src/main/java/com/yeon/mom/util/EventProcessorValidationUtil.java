package com.yeon.mom.util;

import com.yeon.mom.EventProcessor;
import com.yeon.mom.event.EventType;
import com.yeon.util.NameUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author pulsarang
 */
public class EventProcessorValidationUtil {
	public static void checkValid(EventProcessor eventProcessor) {
		if (eventProcessor == null) {
			throw new IllegalArgumentException("Null eventProcessor.");
		}

		String name = eventProcessor.getName();
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Emtpy eventProcessorName.(" + eventProcessor.getClass().getCanonicalName() + ")");
		}

		if (NameUtils.isInvalidJavaIdentifier(name)) {
			throw new IllegalArgumentException("EventProcessorName must be a java identifier.(" + eventProcessor.getClass().getCanonicalName() + ")");
		}

		Set<String> subscribeEventCodes = new HashSet<String>();
		List<? extends EventType> subscribeEventTypes = eventProcessor.getSubscribeEvents();
		if (subscribeEventTypes == null || subscribeEventTypes.isEmpty()) {
			throw new IllegalArgumentException("Subscribe event has not specified. (" + name + ")");
		}

		for (EventType subscribeEventType : subscribeEventTypes) {
			String eventCode = subscribeEventType.getCode();
			if (subscribeEventCodes.contains(eventCode)) {
				throw new IllegalArgumentException("EventProcessor '" + name + "' subscribes duplicated event.(" + eventCode + ")");
			}

			subscribeEventCodes.add(eventCode);
		}
	}
}
