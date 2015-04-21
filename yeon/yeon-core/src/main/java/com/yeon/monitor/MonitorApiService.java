package com.yeon.monitor;

import com.yeon.monitor.alarm.AlarmLog;

public interface MonitorApiService {
	String NAME = "_MONITOR_API";

	void logAlarm(AlarmLog alarmLog);
}