package com.yeon.monitor.logger;

import java.util.Collections;
import java.util.List;

/**
 * 
 * @author pulsarang
 */
public class NullLoggerContext implements MomLoggerContext {
	@Override
	public List<LoggerInfo> getLoggerList() {
		return Collections.emptyList();
	}

	@Override
	public boolean changeLevel(LoggerInfo paramLogger) {
		return false;
	}
}
