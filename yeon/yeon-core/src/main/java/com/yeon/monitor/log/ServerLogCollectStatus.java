package com.yeon.monitor.log;

import com.yeon.util.MapModel;

import java.util.Map;

public class ServerLogCollectStatus extends MapModel {
	public static final int EOF = -100;
	public static final int BUFFER_OVERFLOW = -200;
	public static final int TIMEOUT = -300;
	public static final int STOP = -400;
	public static final int SERVER_TOO_BUSY = -500;

	public static final String PARAM_ID = "id";
	public static final String PARAM_EMPLOYEE_ID = "employee_id";
	private static final String PARAM_START_TIME = "start_time";
	private static final String PARAM_SCAN_FILE_SIZE = "scan_file_size";
	private static final String PARAM_SCAN_LINE_COUNT = "scan_line_count";
	private static final String PARAM_SCAN_LOG_COUNT = "scan_log_count";
	private static final String PARAM_MATCH_LOG_COUNT = "match_log_count";
	private static final String PARAM_CPU_USAGE = "cpu_usage";

	public ServerLogCollectStatus() {
	}

	public ServerLogCollectStatus(Map<String, Object> properties) {
		super(properties);
	}

	public int getId() {
		return getInteger(PARAM_ID, -1);
	}

	public void setId(int id) {
		setInteger(PARAM_ID, id);
	}

	public String getCallerIp() {
		return getString(ServerLogCollectOption.PARAM_CALLER_IP);
	}

	public void setCallerIp(String ip) {
		setString(ServerLogCollectOption.PARAM_CALLER_IP, ip);
	}

	public long getStartTime() {
		return getLong(PARAM_START_TIME, -1L);
	}

	public void setStartTime(long startTime) {
		setLong(PARAM_START_TIME, startTime);
	}

	public long getScanFileSize() {
		return getLong(PARAM_SCAN_FILE_SIZE, -1L);
	}

	public void setScanFileSize(long scanFileSize) {
		setLong(PARAM_SCAN_FILE_SIZE, scanFileSize);
	}

	public long getScanLogCount() {
		return getLong(PARAM_SCAN_LOG_COUNT, -1L);
	}

	public void setScanLogCount(long scanLogCount) {
		setLong(PARAM_SCAN_LOG_COUNT, scanLogCount);
	}

	public long getScanLineCount() {
		return getLong(PARAM_SCAN_LINE_COUNT, -1L);
	}

	public void setScanLineCount(long scanLineCount) {
		setLong(PARAM_SCAN_LINE_COUNT, scanLineCount);
	}

	public long getMatchLogCount() {
		return getLong(PARAM_MATCH_LOG_COUNT, -1L);
	}

	public void setMatchLogCount(long matchLogCount) {
		setLong(PARAM_MATCH_LOG_COUNT, matchLogCount);
	}

	public double getCpuUsage() {
		return getDouble(PARAM_CPU_USAGE, -1.0);
	}

	public void setCpuUsage(double cpuUsage) {
		setDouble(PARAM_CPU_USAGE, cpuUsage);
	}
}
