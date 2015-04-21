package com.yeon.monitor.logger;

import com.yeon.monitor.resource.annotation.MonitoringBean;
import com.yeon.monitor.resource.annotation.MonitoringProperty;
import com.yeon.remote.reload.AbstractReloadService;
import com.yeon.util.MapModel;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author pulsarang
 */
@MonitoringBean(inclusive = false)
public class LoggerMonitor extends AbstractReloadService {
	public static final String NAME = "LOGGER";

	private final MomLoggerContextFactory momLoggerContextFactory = new MomLoggerContextFactory();

	@Override
	public List<MapModel> list(MapModel optionMap) {
		List<LoggerInfo> loggers = momLoggerContextFactory.getLoggerContext().getLoggerList();

		List<MapModel> result = new ArrayList<MapModel>(loggers.size());
		for (LoggerInfo logger : loggers) {
			result.add(logger);
		}

		return result;
	}

	@Override
	public int listCount(MapModel optionMap) {
		return momLoggerContextFactory.getLoggerContext().getLoggerList().size();
	}

	@Override
	public int modify(MapModel optionMap) {
		LoggerInfo paramLogger = new LoggerInfo(optionMap);

		if (StringUtils.isEmpty(paramLogger.getName())) {
			return 0;
		}

		MomLoggerContext ctx = momLoggerContextFactory.getLoggerContext();
		if (ctx.changeLevel(paramLogger)) {
			return 1;
		} else {
			return 0;
		}
	}

	@MonitoringProperty(name = "contextName")
	public String getLoggerContextName() {
		return momLoggerContextFactory.getLoggerContext().getClass().getSimpleName();
	}
}
