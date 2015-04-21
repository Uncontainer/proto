package com.pulsarang.infra.config.info;

import java.util.List;

import com.pulsarang.infra.config.ConfigEntityId;
import com.pulsarang.infra.config.category.ConfigCategory;

public interface ConfigInfoRepositoryCustom {
	List<String> findSuperConfigNames(ConfigEntityId configEntityId);

	List<String> findSubConfigNames(ConfigEntityId configEntityId);

	List<String> findConfigNamesByCategory(ConfigCategory configCategory);
}
