package com.pulsarang.infra.server;

import com.pulsarang.core.data.BaseCustomRepository;

public class ProjectRepositoryImpl extends BaseCustomRepository<ProjectEntity, Long> implements ProjectRepositoryCustom {
	QProjectEntity qItem = QProjectEntity.projectEntity;

}
