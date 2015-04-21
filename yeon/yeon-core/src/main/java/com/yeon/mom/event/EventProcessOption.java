package com.yeon.mom.event;

import com.yeon.util.MapModel;

import java.util.Date;
import java.util.Map;

public class EventProcessOption extends MapModel {
	private static final String PARAM_PROCESSOR_NAME = "proc_nm";
	private static final String PARAM_OPERATION = "op";
	private static final String PARAM_ARGUMENTS = "args";
	private static final String PARAM_TRY_COUNT = "try_cnt";
	private static final String PARAM_LAST_TRY_TIME = "lst_try_ymdt";

	public EventProcessOption(String processorName) {
		setProcessorName(processorName);
	}

	public EventProcessOption(Map<String, Object> properties) {
		super(properties);

		if (getProcessorName() == null) {
			throw new IllegalArgumentException("processorName is empty.");
		}
	}

	public EventProcessOperation getOperation() {
		String strOperation = getString(PARAM_OPERATION);
		if (strOperation == null) {
			return EventProcessOperation.PROCESS;
		} else {
			return EventProcessOperation.valueOf(strOperation);
		}
	}

	public void setOperation(EventProcessOperation eventProcessOperation) {
		if (eventProcessOperation == null) {
			removeValue(PARAM_OPERATION);
		} else {
			setString(PARAM_OPERATION, eventProcessOperation.name());
		}
	}

	public int getTryCount() {
		return getInteger(PARAM_TRY_COUNT, 1);
	}

	public void setTryCount(int tryCount) {
		setInteger(PARAM_TRY_COUNT, tryCount);
	}

	public void increaseTryCount() {
		setTryCount(getTryCount() + 1);
	}

	public String getProcessorName() {
		return getString(PARAM_PROCESSOR_NAME);
	}

	public void setProcessorName(String processorName) {
		setString(PARAM_PROCESSOR_NAME, processorName);
	}

	public Map<String, Object> getArguments() {
		return getMap(PARAM_ARGUMENTS);
	}

	public void setArguments(Map<String, Object> options) {
		setMap(PARAM_ARGUMENTS, options);
	}

	public void setLastTryTime(Date date) {
		setDate(PARAM_LAST_TRY_TIME, date);
	}

	public Date getLastTryTime() {
		return getDate(PARAM_LAST_TRY_TIME);
	}
}
