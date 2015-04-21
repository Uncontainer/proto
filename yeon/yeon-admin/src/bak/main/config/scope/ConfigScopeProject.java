package com.pulsarang.infra.config.scope;

public class ConfigScopeProject implements ConfigScope {
	String solutionName;
	String projectName;

	public ConfigScopeProject(String solutionName, String projectName) {
		super();
		this.solutionName = solutionName;
		this.projectName = projectName;
	}

	public ConfigScopeType getScopeType() {
		return ConfigScopeType.PROJECT;
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

}
