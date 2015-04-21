package com.pulsarang.infra.monitoring;

import java.util.List;

import com.pulsarang.infra.config.Config;
import com.pulsarang.infra.config.category.ConfigCategory;
import com.pulsarang.infra.config.category.ConfigCategoryPool;

public class ProjectInfo extends Config {
	public static final ConfigCategory CATEGORY = new ConfigCategory() {
		@Override
		public String getName() {
			return "project";
		}

		@Override
		public Class<? extends Config> getConfigClass() {
			return ProjectInfo.class;
		}
	};

	static {
		// TODO 별도의 class로 분리
		ConfigCategoryPool.add(CATEGORY);
	}

	public static final String PARAM_MONITORING_COUNT = "monitoring_count";
	public static final String PARAM_MONITORING_DURATION = "monitoring_duration";

	public static final String PARAM_MONITORING_PROPERTY_NAMES = "monitoring_property_names";

	public static final String PARAM_USE_RESOURCE_MONITOR = "use_resource_monitor";
	public static final String PARAM_USE_ACCESS_MONITOR = "use_access_monitor";

	public enum Status {
		NORMAL, PAUSE;
	}

	private transient Status status;

	public int getMonitoringCount() {
		return getInteger(PARAM_MONITORING_COUNT, 10);
	}

	public int getMonitoringDuration() {
		return getInteger(PARAM_MONITORING_DURATION, 10 * 1000);
	}

	public List<String> getMonitoringProperties() {
		return getList(PARAM_MONITORING_PROPERTY_NAMES);
	}

	public boolean isUseAccessMonitor() {
		return getBoolean(PARAM_USE_ACCESS_MONITOR, false);
	}

	public boolean isUseResourceMonitor() {
		return getBoolean(PARAM_USE_RESOURCE_MONITOR, false);
	}

	public Status getStatus() {
		if (status == null) {
			status = getEnum(Status.class, getString(PARAM_STATUS), Status.NORMAL);
			setProperty(PARAM_STATUS, status);
		}

		return status;
	}

}
