package com.yeon.infra.server;

import com.yeon.server.Project;

import java.util.List;

public interface ProjectService {
	List<Project> listAll();

	void addProject(Project project);

	void modifyProject(Project project);

	void removeProject(Project project);
}
