package com.pulsarang.infra.config.propinfo;

import java.util.List;

import com.mysema.query.jpa.impl.JPAQuery;
import com.pulsarang.core.data.BaseCustomRepository;

public class ProjectPropertyMappingRepositoryImpl extends BaseCustomRepository<ProjectPropertyMapping, ProjectPropertyMappingId> implements
		ProjectPropertyMappingRepositoryCustom {

	QProjectPropertyMapping projectPropertyMapping = QProjectPropertyMapping.projectPropertyMapping;

	@Override
	public List<String> findProjectNames(ConfigPropertyInfoId configPropertyInfoId) {
		JPAQuery query = createJPAQuery().from(projectPropertyMapping).where(
				projectPropertyMapping.id.configPropertyInfoId.eq(configPropertyInfoId));
		return query.list(projectPropertyMapping.id.projectName);
	}

}
