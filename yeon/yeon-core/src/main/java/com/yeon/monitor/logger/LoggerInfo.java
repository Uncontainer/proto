package com.yeon.monitor.logger;

import com.yeon.util.MapModel;

import java.util.Map;

public class LoggerInfo extends MapModel {
	public static final String PARAM_LOGGER_NAME = "name";
	public static final String PARAM_LOGGER_LEVEL = "level";

	public LoggerInfo() {
	}

	public LoggerInfo(String name, String level) {
		super();
		setName(name);
		setLevel(level);
	}

	public LoggerInfo(Map<String, Object> properties) {
		super(properties);
	}

	public String getName() {
		return getString(PARAM_LOGGER_NAME);
	}

	public void setName(String name) {
		setString(PARAM_LOGGER_NAME, name);
	}

	public String getLevel() {
		return getString(PARAM_LOGGER_LEVEL);
	}

	public void setLevel(String level) {
		setString(PARAM_LOGGER_LEVEL, level);
	}
}
