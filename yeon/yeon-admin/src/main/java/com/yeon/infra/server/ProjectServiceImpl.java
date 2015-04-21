package com.yeon.infra.server;

import com.yeon.server.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectDao projectDao;

	@Override
	public List<Project> listAll() {
		return projectDao.selectAll();
	}

	@Override
	public void addProject(Project project) {
		if (project.getId() != null) {
			throw new IllegalArgumentException("Project already exist.(" + project.getId() + ")");
		}

		projectDao.insert(project);
	}

	@Override
	public void modifyProject(Project project) {
		if (project.getId() == null) {
			throw new IllegalArgumentException("Project not exist.(" + project.getId() + ")");
		}

		projectDao.update(project);
	}

	@Override
	public void removeProject(Project project) {
		projectDao.delete(project.getId());
	}

}
