package com.pulsarang.infra.server;

import org.apache.commons.lang.StringUtils;

public class SolutionProjectPair {
	public static final SolutionProjectPair INFRA_ADMIN = new SolutionProjectPair("pulsarang", "config-admin");

	String solutionName;
	String projectName;

	public SolutionProjectPair(String solutionName, String projectName) {
		super();
		this.solutionName = solutionName;
		this.projectName = projectName;
	}

	public SolutionProjectPair(String str) {
		String[] tokens = StringUtils.split(str, "$");
		this.solutionName = tokens[0];
		this.projectName = tokens[1];
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

	@Override
	public String toString() {
		return solutionName + "$" + projectName;
	}
}
