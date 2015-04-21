package com.pulsarang.infra.config.prop;

import java.util.List;

import com.pulsarang.infra.config.ConfigEntityId;

public interface ConfigPropertyRepositoryCustom {
	List<ConfigProperty> findConfigProperties(ConfigEntityId configEntityId);

	int deleteAll(ConfigEntityId configId);
}
