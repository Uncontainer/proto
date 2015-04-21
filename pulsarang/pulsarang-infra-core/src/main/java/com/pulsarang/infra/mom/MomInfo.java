package com.pulsarang.infra.mom;

import java.util.Collection;
import java.util.List;

import com.pulsarang.infra.config.Config;
import com.pulsarang.infra.config.ConfigId;
import com.pulsarang.infra.config.category.ConfigCategory;

public class MomInfo extends Config {
	public static final ConfigCategory CATEGORY = new ConfigCategory() {
		@Override
		public String getName() {
			return "momInfo";
		}

		@Override
		public Class<? extends Config> getConfigClass() {
			return MomInfo.class;
		}
	};

	public static final ConfigId CONFIGURATION_ID = new ConfigId(CATEGORY, "mom");

	protected static final String CLUSTER_NOS = "cluster_nos";
	protected static final String EVENT_SUBSCRIBER_NAMES = "event_subscriber_names";

	protected static final String PARAM_SERVER_ADDRESS = "server_address";

	public Collection<String> getClusterNos() {
		return getProperty(CLUSTER_NOS);
	}

	public List<String> getEventSubscriberNames() {
		return getProperty(EVENT_SUBSCRIBER_NAMES);
	}

	public String getServerAddress() {
		return getString(PARAM_SERVER_ADDRESS);
	}

	public void setServerAddress(String serverAddress) {
		setString(PARAM_SERVER_ADDRESS, serverAddress);
	}
}
