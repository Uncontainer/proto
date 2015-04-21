package com.pulsarang.infra;

import java.util.List;

import com.pulsarang.infra.config.Config;
import com.pulsarang.infra.config.ConfigId;
import com.pulsarang.infra.config.category.ConfigCategory;

public class InfraInfo extends Config {
	public static final ConfigCategory CATEGORY = new ConfigCategory() {
		@Override
		public String getName() {
			return "infra-info";
		}

		@Override
		public Class<? extends Config> getConfigClass() {
			return InfraInfo.class;
		}
	};

	public static final ConfigId CONFIG_ID = new ConfigId(InfraInfo.CATEGORY, "infra");

	private static final String PARAM_ALLOW_SERVER_IPS = "allow_server_ips";
	private static final String PARAM_ENVIRONMENT = "env";

	public List<String> getAllowServerIps() {
		return getProperty(PARAM_ALLOW_SERVER_IPS, EMPTY_LIST);
	}

	public void setAllowServerIps(List<String> allowServerIps) {
		setProperty(PARAM_ALLOW_SERVER_IPS, allowServerIps);
	}

	public String getEnvironment() {
		return getProperty(PARAM_ENVIRONMENT);
	}

	public void setEnvironment(String environment) {
		setProperty(PARAM_ENVIRONMENT, environment);
	}
}
