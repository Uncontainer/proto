package com.pulsarang.infra.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Override
	public List<ProjectEntity> listAll() {
		return projectRepository.findAll();
	}

	@Override
	public ProjectEntity getProject(Long id) {
		return projectRepository.findOne(id);
	}

	@Override
	public void addProject(ProjectEntity project) {
		if (project.getId() != null) {
			throw new IllegalArgumentException("Project already exist.(" + project.getId() + ")");
		}

		projectRepository.save(project);
	}

	@Override
	public void modifyProject(ProjectEntity project) {
		if (project.getId() == null) {
			throw new IllegalArgumentException("Project not exist.(" + project.getId() + ")");
		}

		projectRepository.save(project);
	}

	@Override
	public void removeProject(ProjectEntity project) {
		projectRepository.delete(project.getId());
	}

}
