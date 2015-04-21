package com.pulsarang.infra.mom.event;

import java.util.Date;
import java.util.Map;

import com.pulsarang.core.util.MapModel;

public class Event extends MapModel {
	private transient EventType eventType;

	protected static final String PARAM_EVENT_ID = "_evnt_id";
	protected static final String PARAM_CLASS = "_class";
	protected static final String PARAM_OPERATION = "_op";
	protected static final String PARAM_TARGET = "_target";
	protected static final String PARAM_DATE = "_date";
	protected static final String PARAM_EVENT_PROCESS_OPTION = "_proc_opt";

	public Event() {
		super();
	}

	public Event(EventType eventType) {
		super();
		setEventType(eventType);
	}

	public Event(Event event) {
		this(event.getProperties());
	}

	public Event(Map<String, Object> properties) {
		super(properties);
	}

	public long getEventId() {
		return getLong(PARAM_EVENT_ID, 0L);
	}

	public void setEventId(Long id) {
		setProperty(PARAM_EVENT_ID, id);
	}

	public EventType getEventType() {
		if (eventType == null) {
			eventType = EventTypeUtils.decode(getString(PARAM_CLASS), getString(PARAM_OPERATION));
		}

		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;

		setString(PARAM_CLASS, eventType.getClassName());
		setString(PARAM_OPERATION, eventType.getOperationName());
	}

	public void setEventType(String eventClass, String eventOperation) {
		eventType = EventTypeUtils.decode(eventClass, eventOperation);
		setString(PARAM_CLASS, eventType.getClassName());
		setString(PARAM_OPERATION, eventType.getOperationName());
	}

	public String getTarget() {
		return getString(PARAM_TARGET);
	}

	public void setTarget(String target) {
		setString(PARAM_TARGET, target);
	}

	public Date getEventDate() {
		return super.getDate(PARAM_DATE);
	}

	public void setEventDate(Date date) {
		super.setDate(PARAM_DATE, date);
	}

	public boolean hasEventProcessOption() {
		return getProperty(PARAM_EVENT_PROCESS_OPTION) != null;
	}

	public EventProcessOption getEventProcessOption() {
		Map<String, Object> map = getProperty(PARAM_EVENT_PROCESS_OPTION);
		if (map == null) {
			return null;
		}

		return new EventProcessOption(map);
	}

	public void setEventProcessOption(EventProcessOption option) {
		setProperty(PARAM_EVENT_PROCESS_OPTION, option.getProperties());
	}

	public String getEventProcessorName() {
		EventProcessOption eventProcessOption = getEventProcessOption();
		if (eventProcessOption == null) {
			return null;
		}

		return eventProcessOption.getProcessorName();
	}

	public int getTableNo() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public void setTableNo(int tableNo) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public long getChannelNo() {
		return getChannelNo(this);
	}

	public static long getChannelNo(Event event) {
		// TODO channel 번호 생성 로직 변경.
		int hash = event.getEventType().getCode().hashCode();
		String target = event.getTarget();
		if (target != null) {
			hash += target.hashCode();
		}

		return hash;
	}
}