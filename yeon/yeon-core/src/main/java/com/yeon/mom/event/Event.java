package com.yeon.mom.event;

import com.yeon.mom.util.EventTypeUtils;
import com.yeon.util.MapModel;

import java.util.Date;
import java.util.Map;

public class Event extends MapModel {
	private transient EventType eventType;

	public static final String PARAM_EVENT_ID = "_evnt_id";
	public static final String PARAM_CLASS = "_class";
	public static final String PARAM_OPERATION = "_op";
	public static final String PARAM_TARGET = "_target";
	public static final String PARAM_DATE = "_date";
	public static final String PARAM_EVENT_PROCESS_OPTION = "_prcs_opt";
	public static final String PARAM_PROCESS_OPERATION = "_prcs_op";
	public static final String PARAM_SERVER_IP = "_svr_ip";
	public static final String PARAM_ENQUEUE_DATE = "_enque";

	public Event() {
		super();
	}

	public Event(EventType eventType) {
		super();
		setEventType(eventType);
	}

	public Event(Event event) {
		this(event.getValues());
	}

	public Event(Map<String, Object> properties) {
		super(properties);
	}

	public long getEventId() {
		return getLong(PARAM_EVENT_ID, 0L);
	}

	public void setEventId(Long id) {
		setValue(PARAM_EVENT_ID, id);
	}

	public EventType getEventType() {
		if (eventType == null) {
			eventType = EventTypeUtils.decode(getEventClassName(), getEventOperationName());
		}

		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;

		setValue(PARAM_CLASS, eventType.getClassName());
		setValue(PARAM_OPERATION, eventType.getOperationName());
	}

	public void setEventType(String eventClass, String eventOperation) {
		eventType = EventTypeUtils.decode(eventClass, eventOperation);
		setValue(PARAM_CLASS, eventType.getClassName());
		setValue(PARAM_OPERATION, eventType.getOperationName());
	}

	public String getEventClassName() {
		return getString(PARAM_CLASS);
	}

	public void setEventClassName(String eventClassName) {
		setString(PARAM_CLASS, eventClassName);
		this.eventType = null;
	}

	public String getEventOperationName() {
		return getString(PARAM_OPERATION);
	}

	public void setEventOperationName(String eventOperationName) {
		setString(PARAM_OPERATION, eventOperationName);
		this.eventType = null;
	}

	public String getTarget() {
		return (String) getValue(PARAM_TARGET);
	}

	public void setTarget(String target) {
		if (target != null && target.length() > 20) {
			throw new IllegalArgumentException("Max event target length is 20.");
		}

		setValue(PARAM_TARGET, target);
	}

	public Date getEventDate() {
		return super.getDate(PARAM_DATE);
	}

	public void setEventDate(Date date) {
		super.setDate(PARAM_DATE, date);
	}

	public EventProcessOperation getProcessOperation() {
		String strEventProcessOperation = getString(PARAM_PROCESS_OPERATION);
		if (strEventProcessOperation == null) {
			return EventProcessOperation.PROCESS;
		} else {
			return EventProcessOperation.valueOf(strEventProcessOperation);
		}
	}

	public void setEventProcessOperation(EventProcessOperation eventProcessOperation) {
		if (eventProcessOperation == null) {
			removeValue(PARAM_PROCESS_OPERATION);
		} else {
			setString(PARAM_PROCESS_OPERATION, eventProcessOperation.name());
		}
	}

	public boolean hasEventProcessOption() {
		return getValue(PARAM_EVENT_PROCESS_OPTION) != null;
	}

	public EventProcessOption getEventProcessOption() {
		Map<String, Object> map = getValue(PARAM_EVENT_PROCESS_OPTION);
		if (map == null) {
			return null;
		}

		return new EventProcessOption(map);
	}

	public void setEventProcessOption(EventProcessOption option) {
		setValue(PARAM_EVENT_PROCESS_OPTION, option.getValues());
	}

	public String getEventProcessorName() {
		EventProcessOption eventProcessOption = getEventProcessOption();
		if (eventProcessOption == null) {
			return null;
		}

		return eventProcessOption.getProcessorName();
	}

	public String getServerIp() {
		return getString(PARAM_SERVER_IP);
	}

	public void setServerIp(String serverIp) {
		setString(PARAM_SERVER_IP, serverIp);
	}

	public Date getEnqueDate() {
		return getDate(PARAM_ENQUEUE_DATE);
	}

	public void setEnqueDate(Date enqueDate) {
		setDate(PARAM_ENQUEUE_DATE, enqueDate);
	}
}