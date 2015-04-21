package com.pulsarang.infra.config.prop;

import com.pulsarang.infra.config.ConfigEntityId;

import java.util.List;

public interface ConfigPropertyRepositoryCustom {
	List<ConfigProperty> findConfigProperties(ConfigEntityId configEntityId);

	int deleteAll(ConfigEntityId configId);
}
