package com.yeon.monitor.logger;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class LogbackLoggerContext implements MomLoggerContext {
	@Override
	public List<LoggerInfo> getLoggerList() {
		List<ch.qos.logback.classic.Logger> loggers = getLoggerContext().getLoggerList();

		List<LoggerInfo> loggerInfos = new ArrayList<LoggerInfo>(loggers.size());
		for (ch.qos.logback.classic.Logger logger : loggers) {
			if (logger.getLevel() == null) {
				continue;
			}

			loggerInfos.add(new LoggerInfo(logger.getName(), logger.getLevel().toString()));
		}

		return loggerInfos;
	}

	@Override
	public boolean changeLevel(LoggerInfo paramLogger) {
		ch.qos.logback.classic.LoggerContext ctx = getLoggerContext();
		ch.qos.logback.classic.Logger logger = ctx.getLogger(paramLogger.getName());
		if (logger == null) {
			return false;
		}

		String strLevel = paramLogger.getLevel();
		ch.qos.logback.classic.Level level = StringUtils.isBlank(strLevel) ? null : ch.qos.logback.classic.Level.toLevel(strLevel);

		if (logger.getLevel() == level) {
			return false;
		}

		logger.setLevel(level);

		return true;
	}

	private ch.qos.logback.classic.LoggerContext getLoggerContext() {
		return (ch.qos.logback.classic.LoggerContext)org.slf4j.LoggerFactory.getILoggerFactory();
	}
}
