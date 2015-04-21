package com.yeon.monitor.log;

import com.yeon.server.Server;
import com.yeon.util.MapModel;

import java.util.Map;

public class ServerLog extends MapModel {
	private static final String PARAM_SUCCESS = "success";
	private static final String PARAM_MESSAGE = "message";
	private static final String PARAM_CONTENT = "content";
	private static final String PARAM_OPTION = "option";

	private static final String PARAM_SCAN_FILE_SIZE = "scan_file_size";
	private static final String PARAM_SCAN_LINE_COUNT = "scan_line_count";
	private static final String PARAM_SCAN_LOG_COUNT = "scan_log_count";
	private static final String PARAM_MATCH_LOG_COUNT = "match_log_count";
	private static final String PARAM_SCAN_SPENT_TIME = "scan_spent_time";
	private Server server;

	private volatile ServerLogCollectOption option;

	public ServerLog() {
	}

	public ServerLog(Map<String, Object> properties) {
		super(properties);
	}

	public ServerLogCollectOption getOption() {
		if (option != null) {
			return option;
		}

		Map<String, Object> mapOption = getMap(PARAM_OPTION);
		if (mapOption == null) {
			return null;
		}

		option = new ServerLogCollectOption(mapOption);

		return option;
	}

	public void setOption(ServerLogCollectOption option) {
		this.option = option;
		if (option == null) {
			removeValue(PARAM_OPTION);
		} else {
			setMap(PARAM_OPTION, option.getValues());
		}
	}

	public boolean isSuccess() {
		return getBoolean(PARAM_SUCCESS, false);
	}

	public void setSuccess(boolean success) {
		setBoolean(PARAM_SUCCESS, success);
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

	public void setMessage(String message) {
		setString(PARAM_MESSAGE, message);
	}

	public String getMessage() {
		return getString(PARAM_MESSAGE);
	}

	public void setContent(String content) {
		setString(PARAM_CONTENT, content);
	}

	public String getContent() {
		return getString(PARAM_CONTENT);
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public long getMatchLogCount() {
		return getLong(PARAM_MATCH_LOG_COUNT, -1L);
	}

	public void setMatchLogCount(long matchLogCount) {
		setLong(PARAM_MATCH_LOG_COUNT, matchLogCount);
	}

	public long getScanSpentTime() {
		return getLong(PARAM_SCAN_SPENT_TIME, -1L);
	}

	public void setScanSpentTime(long scanSpentTime) {
		setLong(PARAM_SCAN_SPENT_TIME, scanSpentTime);
	}

	public static ServerLog createFail(String message) {
		ServerLog content = new ServerLog();
		content.setSuccess(false);
		content.setMessage(message);

		return content;
	}
}
