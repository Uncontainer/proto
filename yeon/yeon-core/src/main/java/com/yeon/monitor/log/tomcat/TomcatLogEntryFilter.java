package com.yeon.monitor.log.tomcat;

import ch.qos.logback.classic.Level;
import com.yeon.monitor.common.ffm.FilterFieldMatcher;
import com.yeon.monitor.common.ffm.MatchExpressionParser;
import com.yeon.monitor.log.ServerLogEntryFilter;
import com.yeon.util.MapModel;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

public class TomcatLogEntryFilter extends MapModel implements ServerLogEntryFilter<TomcatLogEntry> {
	private static final String PARAM_LOG_LEVEL = "log_level";
	private static final String PARAM_MESSAGE = "message";
	private static final String PARAM_PATH = "path";
	private static final String PARAM_THREAD_NAME = "thread_name";

	private FilterFieldMatcher threadNameMatcher = FilterFieldMatcher.TRUE;
	private Level logLevel;
	private FilterFieldMatcher pathMatcher = FilterFieldMatcher.TRUE;
	private FilterFieldMatcher messageMatcher = FilterFieldMatcher.TRUE;

	public TomcatLogEntryFilter() {
	}

	public TomcatLogEntryFilter(Map<String, Object> properties) {
		super(properties);
		this.threadNameMatcher = MatchExpressionParser.parse(getThreadName());
		this.logLevel = getLogLevel();
		this.pathMatcher = MatchExpressionParser.parse(getPath());
		this.messageMatcher = MatchExpressionParser.parse(getMessage());
	}

	public boolean isEmptyFilter() {
		return StringUtils.isBlank(getThreadName())
			&& StringUtils.isBlank(getPath())
			&& StringUtils.isBlank(getMessage())
			&& (getLogLevel() == null || getLogLevel() == Level.ALL);
	}

	public String getThreadName() {
		return getString(PARAM_THREAD_NAME);
	}

	public void setThreadName(String threadName) {
		if (StringUtils.isBlank(threadName)) {
			threadName = null;
		}

		this.threadNameMatcher = MatchExpressionParser.parse(threadName);
		setString(PARAM_THREAD_NAME, threadName);
	}

	public Level getLogLevel() {
		String strLogLevel = getString(PARAM_LOG_LEVEL);
		if (strLogLevel == null) {
			return null;
		}

		return Level.toLevel(strLogLevel);
	}

	public void setLogLevelCode(String logLevelCode) {
		if (StringUtils.isBlank(logLevelCode)) {
			this.logLevel = null;
		} else {
			this.logLevel = Level.toLevel(logLevelCode);
		}
		setString(PARAM_LOG_LEVEL, logLevelCode);
	}

	public String getLogLevelCode() {
		return getString(PARAM_LOG_LEVEL);
	}

	public String getPath() {
		return getString(PARAM_PATH);
	}

	public void setPath(String path) {
		if (StringUtils.isBlank(path)) {
			path = null;
		}

		this.pathMatcher = MatchExpressionParser.parse(path);
		setString(PARAM_PATH, path);
	}

	public String getMessage() {
		return getString(PARAM_MESSAGE);
	}

	public void setMessage(String message) {
		if (StringUtils.isBlank(message)) {
			message = null;
		}

		this.messageMatcher = MatchExpressionParser.parse(message);
		setString(PARAM_MESSAGE, message);
	}

	@Override
	public boolean accept(TomcatLogEntry logEntry) {
		if (logEntry == null) {
			return false;
		}

		// 성능에 많은 영향을 주므로 연산이 무거운 비교는 최대한 뒤로 미룬다.
		if (logLevel != null) {
			if (logLevel.levelInt > logEntry.getLogLevel()) {
				return false;
			}
		}

		return threadNameMatcher.match(logEntry.getThreadName())
			&& pathMatcher.match(logEntry.getPath())
			&& messageMatcher.match(logEntry.getMessage());
	}
}
