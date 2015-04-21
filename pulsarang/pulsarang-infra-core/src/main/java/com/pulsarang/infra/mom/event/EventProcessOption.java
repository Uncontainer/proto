package com.pulsarang.infra.mom.event;

import java.util.Date;
import java.util.Map;

import com.pulsarang.core.util.MapModel;

public class EventProcessOption extends MapModel {
	private static final String PARAM_TRY_COUNT = "try_count";
	private static final String PARAM_ARGUMENTS = "args";
	private static final String PARAM_OPERATION = "operation";
	private static final String PARAM_PROCESSOR_NAME = "proc_nm";
	private static final String PARAM_LAST_TRY_TIME = "lst_try_ymdt";

	public static final String OP_VERIFY = "verify";
	public static final String OP_REPROCESS = "reprocess";
	public static final String OP_FAIL = "fail";

	public EventProcessOption(String processorName) {
		setProcessorName(processorName);
	}

	public EventProcessOption(Map<String, Object> properties) {
		super(properties);

		if (getProcessorName() == null) {
			throw new IllegalArgumentException("processorName is empty.");
		}
	}

	public String getOperation() {
		return getProperty(PARAM_OPERATION);
	}

	public void setOperation(String operation) {
		setProperty(PARAM_OPERATION, operation);
	}

	public int getTryCount() {
		return getProperty(PARAM_TRY_COUNT, 0);
	}

	public void setTryCount(int tryCount) {
		setProperty(PARAM_TRY_COUNT, tryCount);
	}

	public void increaseTryCount() {
		setTryCount(getTryCount() + 1);
	}

	public String getProcessorName() {
		return getProperty(PARAM_PROCESSOR_NAME);
	}

	public void setProcessorName(String processorName) {
		setProperty(PARAM_PROCESSOR_NAME, processorName);
	}

	public Map<String, Object> getArguments() {
		return getProperty(PARAM_ARGUMENTS);
	}

	public void setArguments(Map<String, Object> options) {
		setProperty(PARAM_ARGUMENTS, options);
	}

	public void setLastTryTime(Date date) {
		setDate(PARAM_LAST_TRY_TIME, date);
	}

	public Date getLastTryTime() {
		return getDate(PARAM_LAST_TRY_TIME);
	}
}
