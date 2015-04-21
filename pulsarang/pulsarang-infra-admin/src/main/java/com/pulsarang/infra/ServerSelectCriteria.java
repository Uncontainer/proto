package com.pulsarang.infra;

public class ServerSelectCriteria {
	String solutionName = "pulsarang";
	String projectName;
	String serverName;

	public ServerSelectCriteria() {
	}

	public ServerSelectCriteria(String solutionName, String projectName, String serverName) {
		super();
		this.solutionName = solutionName;
		this.projectName = projectName;
		this.serverName = serverName;
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

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Override
	public String toString() {
		return solutionName + "." + projectName + "." + serverName;
	}
}
