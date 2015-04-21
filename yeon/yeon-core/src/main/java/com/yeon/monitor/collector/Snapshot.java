package com.yeon.monitor.collector;

import com.yeon.util.MapModel;

import java.util.Map;

public class Snapshot extends MapModel {
	public static final String PARAM_CREATE_TIME = "_create_time";

	long createTime;

	public Snapshot() {
	}

	public Snapshot(Map<String, Object> properties) {
		super(properties);
	}

	void reset(long createTime) {
		data.clear();
		setCreateTime(createTime);
	}

	public long getCreateTime() {
		return getLong(PARAM_CREATE_TIME, 0L);
	}

	public void setCreateTime(long startTime) {
		this.createTime = startTime;
		setLong(PARAM_CREATE_TIME, startTime);
	}
}
