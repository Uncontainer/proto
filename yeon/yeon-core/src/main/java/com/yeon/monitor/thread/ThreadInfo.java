package com.yeon.monitor.thread;

import com.yeon.util.MapModel;

import java.util.Map;

/**
 * 
 * @author pulsarang
 */
public class ThreadInfo extends MapModel {
	public static final String PARAM_ID = "id";
	public static final String PARAM_NAME = "name";
	public static final String PARAM_PRIORITY = "priority";
	public static final String PARAM_STATE = "state";
	public static final String PARAM_STACKTRACE = "stack_trace";

	public ThreadInfo() {
	}

	public ThreadInfo(Map<String, Object> properties) {
		super(properties);
	}

	public ThreadInfo(Thread thread, StackTraceElement[] stackTraceElements) {
		setId(thread.getId());
		setName(thread.getName());
		setPriority(thread.getPriority());
		setState(thread.getState().name());

		StringBuilder sb = new StringBuilder();
		for (StackTraceElement stackTraceElement : stackTraceElements) {
			sb.append(stackTraceElement.toString()).append("\n");
		}
		setStackTrace(sb.toString());
	}

	public long getId() {
		return getLong(PARAM_ID);
	}

	public void setId(long id) {
		setLong(PARAM_ID, id);
	}

	public String getName() {
		return getString(PARAM_NAME);
	}

	public void setName(String name) {
		setString(PARAM_NAME, name);
	}

	public int getPriority() {
		return getInteger(PARAM_PRIORITY);
	}

	public void setPriority(int priority) {
		setInteger(PARAM_PRIORITY, priority);
	}

	public String getState() {
		return getString(PARAM_STATE);
	}

	public void setState(String state) {
		setString(PARAM_STATE, state);
	}

	public String getStackTrace() {
		return getString(PARAM_STACKTRACE);
	}

	public void setStackTrace(String stackTrace) {
		setString(PARAM_STACKTRACE, stackTrace);
	}
}
