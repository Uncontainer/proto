package com.pulsarang.infra.config.category;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigCategoryPool {

	private static final Map<String, ConfigCategory> configCategories;

	static {
		configCategories = new ConcurrentHashMap<String, ConfigCategory>();
		for (ConfigCategory category : PredefinedConfigCategory.values()) {
			configCategories.put(category.getName(), category);
		}
	}

	public static void add(ConfigCategory configCategory) {
		if (configCategory == null) {
			throw new IllegalArgumentException();
		}

		ConfigCategory savedConfigCategory = get(configCategory.getName());
		if (savedConfigCategory instanceof PredefinedConfigCategory) {
			throw new IllegalArgumentException("Cannot redefine predefined config category.(" + configCategory.getName() + ")");
		}

		configCategories.put(configCategory.getName(), configCategory);
	}

	public static ConfigCategory get(String name) {
		return configCategories.get(name);
	}

	public static ConfigCategory getSafely(String name) {
		ConfigCategory configCategory = configCategories.get(name);
		if (configCategory == null) {
			throw new IllegalArgumentException("Fail to find category.(" + name + ")");
		}

		return configCategory;
	}
}
