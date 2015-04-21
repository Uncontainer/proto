package com.pulsarang.mom.common;

import com.pulsarang.infra.config.Config;
import com.pulsarang.infra.config.category.ConfigCategory;
import com.pulsarang.infra.mom.MomInfo;
import com.pulsarang.mom.common.cluster.ClusterInfo;
import com.pulsarang.mom.common.cluster.DbInfo;

public enum MomConfigCategory implements ConfigCategory {
	MOM("mom", MomInfo.class), //
	CLUSTER("cluster", ClusterInfo.class), //
	EVENT_QUEUE("eventQueue", EventQueueInfo.class), //
	DB("db", DbInfo.class);

	final String name;
	final Class<? extends Config> configurationClass;

	private MomConfigCategory(String name, Class<? extends Config> configurationClass) {
		this.name = name;
		this.configurationClass = configurationClass;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<? extends Config> getConfigClass() {
		return configurationClass;
	}
}
