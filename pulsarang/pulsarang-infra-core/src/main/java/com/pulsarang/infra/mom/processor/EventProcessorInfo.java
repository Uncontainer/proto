/*
 * EventProcessorInfo.java $version 2010. 10. 15
 * 
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.pulsarang.infra.mom.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.pulsarang.infra.config.Config;
import com.pulsarang.infra.config.ConfigId;
import com.pulsarang.infra.config.ConfigListener;
import com.pulsarang.infra.config.category.ConfigCategory;
import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.infra.mom.event.EventType;
import com.pulsarang.infra.mom.event.EventTypeUtils;

/**
 * 
 * @author pulsarang
 */
public class EventProcessorInfo extends Config implements ConfigListener {
	public static final ConfigCategory CATEGORY = new ConfigCategory() {
		@Override
		public String getName() {
			return "eventProcessor";
		}

		@Override
		public Class<? extends Config> getConfigClass() {
			return EventProcessorInfo.class;
		}
	};

	public static final String PARAM_USE_PROCESSOR = "use_processor";
	public static final String PARAM_EVENT_TTL = "event_ttl";
	public static final String PARAM_SUBSCRIBE_EVENT_CODES = "subscribe_event_codes";

	// reprocess config
	public static final String PARAM_REPROCESS_TRY_INTERVAL = "reprocess_try_interval";
	public static final String PARAM_REPROCESS_TRY_COUNT = "reprocess_try_count";
	public static final String PARAM_PROCESSOR_STATUS = "processor_status";
	public static final String PARAM_PROCESSOR_EXPECTED_ACTIVATE_DATE = "processor_expected_activate_date";

	// cache
	private volatile EventProcessorStatus processorStatus;
	private volatile long nextActivateTime;
	private volatile List<EventType> subscribeEventTypes;

	public EventProcessorInfo() {
		super();
		setConfigCategoryName(CATEGORY.getName());
		// TODO eventProcessorInfo 초기화
		// this.processor = processor;
	}

	public String getName() {
		return getConfigName();
	}

	public boolean useProcessor() {
		return getBoolean(PARAM_USE_PROCESSOR, false);
	}

	public boolean isExpiredEvent(Event event) {
		Date eventTime = event.getEventDate();
		long eventTtl = getLong(PARAM_EVENT_TTL);
		return (eventTime.getTime() + eventTtl) < System.currentTimeMillis();
	}

	public boolean isPostponeStatus(boolean reprocess) {
		switch (getProcessorStatus()) {
		case STOP:
			return true;
		case REPROCESS_ONLY:
			return !reprocess;
		default:
			return System.currentTimeMillis() <= nextActivateTime;
		}
	}

	public long getNextActivateTime(long expectedNextActivateTime) {
		if (expectedNextActivateTime < nextActivateTime) {
			return nextActivateTime;
		} else {
			return expectedNextActivateTime;
		}
	}

	@Override
	public String getConfigCategoryName() {
		return CATEGORY.getName();
	}

	// @Override
	// public String toString() {
	// return name;
	// }

	public void postpone(long duration) {
		long candidate = System.currentTimeMillis() + duration;
		if (candidate > nextActivateTime) {
			nextActivateTime = candidate;
		}
	}

	@Override
	public List<ConfigId> getIds() {
		return Arrays.asList(new ConfigId(CATEGORY, getConfigName()));
	}

	@Override
	public void validate(ConfigId configId, Config config) throws Exception {
		EventProcessorInfo eventProcessorInfo = (EventProcessorInfo) config;
		EventProcessorStatus.valueOf(eventProcessorInfo.getString(PARAM_PROCESSOR_STATUS).toUpperCase());
	}

	@Override
	public void changed(ConfigId configId, Config config) {
		processorStatus = null;
	}

	private EventProcessorStatus getProcessorStatus() {
		if (processorStatus == null) {
			processorStatus = EventProcessorStatus.valueOf(getString(PARAM_PROCESSOR_STATUS).toUpperCase());

			Date expectedActivateDate = getDate(PARAM_PROCESSOR_EXPECTED_ACTIVATE_DATE);
			if (expectedActivateDate != null) {
				if (expectedActivateDate.getTime() > nextActivateTime) {
					nextActivateTime = expectedActivateDate.getTime();
				}
			}
		}

		return processorStatus;
	}

	public long getRetryInterval() {
		return getLong(PARAM_REPROCESS_TRY_INTERVAL);
	}

	public int getRetryCount() {
		return getInteger(PARAM_REPROCESS_TRY_COUNT);
	}

	public List<EventType> getSubscribeEventTypes() {
		if (subscribeEventTypes == null) {
			List<String> eventCodes = getList(PARAM_SUBSCRIBE_EVENT_CODES);
			if (CollectionUtils.isEmpty(eventCodes)) {
				this.subscribeEventTypes = Collections.emptyList();
			} else {
				List<EventType> subscribeEventTypes = new ArrayList<EventType>(eventCodes.size());
				for (String eventCode : eventCodes) {
					subscribeEventTypes.add(EventTypeUtils.decode(eventCode));
				}
				this.subscribeEventTypes = subscribeEventTypes;
			}
		}

		return subscribeEventTypes;
	}

	public void setSubscribeEventTypes(List<EventType> subscribeEventTypes) {
		if (CollectionUtils.isEmpty(subscribeEventTypes)) {
			removeProperty(PARAM_SUBSCRIBE_EVENT_CODES);
			this.subscribeEventTypes = Collections.emptyList();
		} else {
			List<String> eventCodes = new ArrayList<String>(subscribeEventTypes.size());
			for (EventType eventType : subscribeEventTypes) {
				eventCodes.add(eventType.getCode());
			}
			setList(PARAM_SUBSCRIBE_EVENT_CODES, eventCodes);
			this.subscribeEventTypes = subscribeEventTypes;
		}

	}
}
