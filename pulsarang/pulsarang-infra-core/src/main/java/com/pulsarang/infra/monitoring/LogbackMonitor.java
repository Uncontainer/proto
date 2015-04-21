package com.pulsarang.infra.monitoring;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.remote.reload.AbstractReloadService;

public class LogbackMonitor extends AbstractReloadService {
	public static final String NAME = "LOGGER";

	public static final String PARAM_LOGGER_NAME = "name";
	public static final String PARAM_LOGGER_LEVEL = "level";

	@Override
	public List<MapModel> list(MapModel option) {
		List<Logger> loggers = getLoggerContext().getLoggerList();

		List<MapModel> result = new ArrayList<MapModel>(loggers.size());
		for (Logger logger : loggers) {
			Level level = logger.getLevel();
			if (level == null) {
				continue;
			}

			MapModel item = new MapModel(4);
			item.setString(PARAM_LOGGER_NAME, logger.getName());
			item.setString(PARAM_LOGGER_LEVEL, level.toString());
			result.add(item);
		}

		return result;
	}

	@Override
	public int listCount(MapModel option) {
		return getLoggerContext().getLoggerList().size();
	}

	@Override
	public int modify(MapModel option) {
		String name = option.getString(PARAM_LOGGER_NAME);
		if (StringUtils.isEmpty(name)) {
			return 0;
		}
		LoggerContext ctx = getLoggerContext();
		Logger logger = ctx.getLogger(name);
		if (logger == null) {
			return 0;
		}

		String strLevel = option.getString(PARAM_LOGGER_LEVEL);
		Level level = Level.toLevel(strLevel);
		if (logger.getLevel() == level) {
			return 0;
		}

		logger.setLevel(level);

		return 1;
	}

	private LoggerContext getLoggerContext() {
		return (LoggerContext) LoggerFactory.getILoggerFactory();
	}
}
