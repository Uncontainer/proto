package com.yeon.monitor.logger;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author pulsarang
 */
public class MomLoggerContextFactory {
	@SuppressWarnings("unchecked")
	private final List<Class<? extends MomLoggerContext>> candidates = Arrays.asList(LogbackLoggerContext.class, JavaLoggerContext.class);

	private MomLoggerContext loggerContext;

	public MomLoggerContextFactory() {
		for (Class<? extends MomLoggerContext> candidate : candidates) {
			loggerContext = newInstance(candidate);
			if (loggerContext != null) {
				return;
			}
		}

		loggerContext = new NullLoggerContext();
	}

	public MomLoggerContext getLoggerContext() {
		return loggerContext;
	}

	private MomLoggerContext newInstance(Class<? extends MomLoggerContext> clazz) {
		try {
			MomLoggerContext momLoggerContext = clazz.newInstance();
			List<LoggerInfo> loggerList = momLoggerContext.getLoggerList();

			return loggerList.isEmpty() ? null : momLoggerContext;
		} catch (Throwable ignore) {
			return null;
		}
	}
}
