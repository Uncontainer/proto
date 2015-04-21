package com.yeon.monitor.alarm;

import com.yeon.util.MapModel;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.Date;

public class AlarmLog extends MapModel {
	protected static final String PARAM_TYPE = "type";
	protected static final String PARAM_SOURCE = "source";
	protected static final String PARAM_MESSAGE = "message";
	protected static final String PARAM_CREATE_DATE = "create_date";
	protected static final String PARAM_SOLUTION = "solution";
	protected static final String PARAM_PROJECT = "project";

	public AlarmLog() {
	}

	public AlarmLog(String type) {
		this(type, null, null, null);
	}

	public AlarmLog(String type, Throwable throwable) {
		this(type, null, throwable, null);
	}

	public AlarmLog(String type, Throwable throwable, String source) {
		this(type, null, throwable, source);
	}

	public AlarmLog(String type, String message, Throwable throwable, String source) {
		setType(type);
		setThrowable(message, throwable);
		setSource(source);
		setCreateDate(new Date());
	}

	public String getType() {
		return getString(PARAM_TYPE);
	}

	public void setType(String type) {
		setString(PARAM_TYPE, type);
	}

	public String getSource() {
		return getString(PARAM_SOURCE);
	}

	public void setSource(String source) {
		setString(PARAM_SOURCE, source);
	}

	public String getMessage() {
		return getString(PARAM_MESSAGE);
	}

	public void setMessage(String exception) {
		setString(PARAM_MESSAGE, exception);
	}

	public void setThrowable(String message, Throwable throwable) {
		if (throwable == null) {
			if (message == null) {
				setMessage(null);
			} else {
				setMessage(message);
			}
		} else {
			if (message == null) {
				setMessage(ExceptionUtils.getFullStackTrace(throwable));
			} else {
				setMessage(message + " \n" + ExceptionUtils.getFullStackTrace(throwable));
			}
		}
	}

	public String getSolutionName() {
		return getString(PARAM_SOLUTION);
	}

	public void setSolutionName(String solution) {
		setString(PARAM_SOLUTION, solution);
	}

	public String getProjectName() {
		return getString(PARAM_PROJECT);
	}

	public void setProjectName(String project) {
		setString(PARAM_PROJECT, project);
	}

	public Date getCreateDate() {
		return getDate(PARAM_CREATE_DATE);
	}

	public void setCreateDate(Date createDate) {
		setDate(PARAM_CREATE_DATE, createDate);
	}
}
