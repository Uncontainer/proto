package com.pulsarang.infra.mom.event;

import java.util.Date;

import com.pulsarang.infra.config.Config;

public class EventProcessInfo extends Config {
	protected static final String PROCESSOR_NAME = "proc_nm";
	protected static final String SERVER_IP = "svr_ip";
	protected static final String PROCESS_START_TIME = "prc_strt_ymdt";
	protected static final String PROCESS_FINISH_TIME = "prc_end_ymdt";
	protected static final String EVNET_ID = "evt_id";
	protected static final String SUCCESS = "success";
	protected static final String EXCEPTION = "exception";

	private transient EventType eventType;

	public EventProcessInfo() {
	}

	public EventProcessInfo(EventProcessInfo log) {
		super(log.getProperties());
	}

	public EventType getEventType() {
		if (eventType == null) {
			eventType = EventTypeUtils.decode((String) getProperty(Event.PARAM_CLASS), (String) getProperty(Event.PARAM_OPERATION));
		}

		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;

		setProperty(Event.PARAM_CLASS, eventType.getClassName());
		setProperty(Event.PARAM_OPERATION, eventType.getOperationName());
	}

	public String getProcessorName() {
		return getProperty(PROCESSOR_NAME);
	}

	public void setProcessorName(String processorName) {
		this.setProperty(PROCESSOR_NAME, processorName);
	}

	public String getServerIp() {
		return getProperty(SERVER_IP);
	}

	public void setServerIp(String serverIp) {
		this.setProperty(SERVER_IP, serverIp);
	}

	public Date getProcessStartTime() {
		return getDate(PROCESS_START_TIME);
	}

	public void setProcessStartTime(Date processStartTime) {
		this.setDate(PROCESS_START_TIME, processStartTime);
	}

	public Date getProcessFinishTime() {
		return getDate(PROCESS_FINISH_TIME);
	}

	public void setProcessFinishTime(Date processFinishTime) {
		this.setDate(PROCESS_FINISH_TIME, processFinishTime);
	}

	public Long getEventId() {
		return getLong(EVNET_ID);
	}

	public void setEventId(Long eventId) {
		this.setProperty(EVNET_ID, eventId);
	}

	public Boolean getSuccess() {
		return getProperty(SUCCESS);
	}

	public void setSuccess(Boolean success) {
		this.setProperty(SUCCESS, success);
	}

	public String getException() {
		return getProperty(EXCEPTION);
	}

	public void setException(String exception) {
		this.setProperty(EXCEPTION, exception);
	}
}
