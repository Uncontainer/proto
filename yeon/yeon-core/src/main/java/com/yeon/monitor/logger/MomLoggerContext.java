package com.yeon.monitor.logger;

import java.util.List;

public interface MomLoggerContext {
	List<LoggerInfo> getLoggerList();

	boolean changeLevel(LoggerInfo paramLogger);
}
