package com.yeon.mom.event;

import com.yeon.mom.util.EventTypeUtils;
import com.yeon.util.MapModel;

import java.util.Date;

public class EventProcessInfo extends MapModel {
	protected static final String PARAM_PROCESSOR_NAME = "proc_nm";
	protected static final String PARAM_SERVER_IP = "svr_ip";
	protected static final String PARAM_PROCESS_START_TIME = "prcs_strt_ymdt";
	protected static final String PARAM_PROCESS_FINISH_TIME = "prcs_end_ymdt";
	protected static final String PARAM_EVNET_ID = "evt_id";
	protected static final String PARAM_SUCCESS = "success";
	protected static final String PARAM_EXCEPTION = "exception";
	protected static final String PARAM_SKIPPED = "skipped";

	private transient EventType eventType;

	public EventProcessInfo() {
	}

	public EventProcessInfo(EventProcessInfo log) {
		super(log.getValues());
	}

	public EventType getEventType() {
		if (eventType == null) {
			eventType = EventTypeUtils.decode(getString(Event.PARAM_CLASS), getString(Event.PARAM_OPERATION));
		}

		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;

		setString(Event.PARAM_CLASS, eventType.getClassName());
		setString(Event.PARAM_OPERATION, eventType.getOperationName());
	}

	public String getEventClassName() {
		return getString(Event.PARAM_CLASS);
	}

	public void setEventClassName(String eventClassName) {
		setString(Event.PARAM_CLASS, eventClassName);
		this.eventType = null;
	}

	public String getEventOperationName() {
		return getString(Event.PARAM_OPERATION);
	}

	public void setEventOperationName(String eventOperationName) {
		setString(Event.PARAM_OPERATION, eventOperationName);
		this.eventType = null;
	}

	public String getProcessorName() {
		return getString(PARAM_PROCESSOR_NAME);
	}

	public void setProcessorName(String processorName) {
		this.setString(PARAM_PROCESSOR_NAME, processorName);
	}

	public String getServerIp() {
		return getString(PARAM_SERVER_IP);
	}

	public void setServerIp(String serverIp) {
		this.setString(PARAM_SERVER_IP, serverIp);
	}

	public Date getProcessStartTime() {
		return getDate(PARAM_PROCESS_START_TIME);
	}

	public void setProcessStartTime(Date processStartTime) {
		this.setDate(PARAM_PROCESS_START_TIME, processStartTime);
	}

	public Date getProcessFinishTime() {
		return getDate(PARAM_PROCESS_FINISH_TIME);
	}

	public void setProcessFinishTime(Date processFinishTime) {
		this.setDate(PARAM_PROCESS_FINISH_TIME, processFinishTime);
	}

	public Long getEventId() {
		return getLong(PARAM_EVNET_ID);
	}

	public void setEventId(Long eventId) {
		this.setLong(PARAM_EVNET_ID, eventId);
	}

	public Boolean getSuccess() {
		return getBoolean(PARAM_SUCCESS, true);
	}

	public void setSuccess(Boolean success) {
		this.setBoolean(PARAM_SUCCESS, success);
	}

	public String getException() {
		return getString(PARAM_EXCEPTION);
	}

	public void setException(String exception) {
		this.setString(PARAM_EXCEPTION, exception);
	}

	public boolean getSkipped() {
		return getBoolean(PARAM_SKIPPED, false);
	}

	public void setSkipped(boolean skipped) {
		setBoolean(PARAM_SKIPPED, skipped);
	}
}
