package com.yeon.monitor.logger;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class JavaLoggerContext implements MomLoggerContext {
	@Override
	public List<LoggerInfo> getLoggerList() {
		List<LoggerInfo> loggerInfos = new ArrayList<LoggerInfo>();

		java.util.logging.LogManager logManager = java.util.logging.LogManager.getLogManager();
		Enumeration<String> loggerNames = logManager.getLoggerNames();
		while (loggerNames.hasMoreElements()) {
			String loggerName = loggerNames.nextElement();
			java.util.logging.Logger logger = logManager.getLogger(loggerName);
			if (logger == null) {
				continue;
			}

			loggerInfos.add(new LoggerInfo(logger.getName(), logger.getLevel().toString()));
		}

		return loggerInfos;
	}

	@Override
	public boolean changeLevel(LoggerInfo paramLogger) {
		java.util.logging.LogManager logManager = java.util.logging.LogManager.getLogManager();

		String strLevel = paramLogger.getLevel();
		java.util.logging.Level level = StringUtils.isBlank(strLevel) ? null : java.util.logging.Level.parse(strLevel);

		java.util.logging.Logger logger = logManager.getLogger(paramLogger.getName());
		if (logger == null) {
			logger = new java.util.logging.Logger(paramLogger.getName(), null) {
			};
			logger.setLevel(level);
			logManager.addLogger(logger);
		} else {
			if (logger.getLevel() == level) {
				return false;
			}

			logger.setLevel(level);
		}

		return true;
	}
}
