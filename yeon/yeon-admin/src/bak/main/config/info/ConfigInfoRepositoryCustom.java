package com.pulsarang.infra.config.info;

import com.pulsarang.infra.config.ConfigEntityId;
import com.pulsarang.infra.config.category.ConfigCategory;

import java.util.List;

public interface ConfigInfoRepositoryCustom {
	List<String> findSuperConfigNames(ConfigEntityId configEntityId);

	List<String> findSubConfigNames(ConfigEntityId configEntityId);

	List<String> findConfigNamesByCategory(ConfigCategory configCategory);
}
