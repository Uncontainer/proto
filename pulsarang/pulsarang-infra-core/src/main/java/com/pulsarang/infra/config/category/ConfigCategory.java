package com.pulsarang.infra.config.category;

import com.pulsarang.infra.config.Config;

public interface ConfigCategory {
	String getName();

	Class<? extends Config> getConfigClass();
}