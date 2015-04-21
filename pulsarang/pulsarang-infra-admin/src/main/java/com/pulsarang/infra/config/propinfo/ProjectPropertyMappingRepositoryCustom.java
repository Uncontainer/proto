package com.pulsarang.infra.config.propinfo;

import java.util.List;


public interface ProjectPropertyMappingRepositoryCustom {
	List<String> findProjectNames(ConfigPropertyInfoId configPropertyInfoId);
}
