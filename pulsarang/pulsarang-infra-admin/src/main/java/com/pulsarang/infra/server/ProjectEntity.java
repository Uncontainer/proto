package com.pulsarang.infra.server;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "sxb_pjt_info")
public class ProjectEntity {
	private Long id;
	private String solutionName;
	private String projectName;
	private int portPrefix = 117;

	@Transient
	private Project project;

	@Transient
	public Project getProject() {
		return project;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getPortPrefix() {
		return portPrefix;
	}

	public void setPortPrefix(int portPrefix) {
		this.portPrefix = portPrefix;
	}

	public void setProject(Project project) {
		this.project = project;
	}
}
