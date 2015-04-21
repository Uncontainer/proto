package com.pulsarang.infra.config.category;

import com.pulsarang.infra.config.Config;

public enum PredefinedConfigCategory implements ConfigCategory {
	DEFAULT("default", Config.class) //
	;

	final String name;
	final Class<? extends Config> configClass;

	PredefinedConfigCategory(String name, Class<? extends Config> configClass) {
		this.name = name;
		this.configClass = configClass;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<? extends Config> getConfigClass() {
		return configClass;
	}
}
