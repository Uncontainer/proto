package com.pulsarang.infra.server;

import java.util.List;

public interface ProjectService {
	List<ProjectEntity> listAll();

	ProjectEntity getProject(Long id);

	void addProject(ProjectEntity project);

	void modifyProject(ProjectEntity project);

	void removeProject(ProjectEntity project);
}
