package com.pulsarang.infra.config;

import java.util.List;

import com.pulsarang.infra.config.category.ConfigCategory;

public interface ConfigApiService {
	String NAME = "ConfigApiService";

	Config get(ConfigId configId);

	Config add(Config config);

	Config set(Config config);

	void remove(ConfigId configId);

	List<ConfigId> getIds(ConfigCategory configCategory);

	<T extends Config> List<T> listByCategory(ConfigCategory configCategory);

	void refresh(ConfigId configId);
}
