package com.pulsarang.infra.mom.sxb;

import com.pulsarang.infra.InfraContext;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.mom.event.EventType;
import com.pulsarang.infra.mom.event.EventTypeUtils;

public class SxbDestinationUtils {
	public static String getExchangeName(EventType eventType) {
		return eventType.getCode();
	}

	public static String getCanonicalEventProcessorName(String eventProcessorName) {
		InfraContext infraContext = InfraContextFactory.getInfraContext();
		return infraContext.getSolutionName() + "." + infraContext.getProjectName() + "." + eventProcessorName;
	}

	public static String getQueueName(EventType eventType, String eventProcessorName) {
		if (EventTypeUtils.isQueueEventType(eventType)) {
			return getExchangeName(eventType);
		} else {
			String canonicalEventProcessorName;
			if (eventProcessorName.contains(".")) {
				canonicalEventProcessorName = eventProcessorName;
			} else {
				canonicalEventProcessorName = getCanonicalEventProcessorName(eventProcessorName);
			}

			return canonicalEventProcessorName + "@" + getExchangeName(eventType);
		}
	}
}
