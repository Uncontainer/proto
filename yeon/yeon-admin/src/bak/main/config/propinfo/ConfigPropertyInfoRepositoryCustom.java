package com.pulsarang.infra.config.propinfo;

import java.util.List;

public interface ConfigPropertyInfoRepositoryCustom {
	List<ConfigPropertyInfo> findConfigPropertyInfos(String configCategory);
}
