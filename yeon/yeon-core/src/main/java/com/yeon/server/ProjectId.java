package com.yeon.server;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author pulsarang
 */
public class ProjectId implements Comparable<ProjectId> {
	public static final ProjectId INFRA_ADMIN = new ProjectId("yeon", "infra-admin");
	public static final ProjectId MOM_ADMIN = new ProjectId("yeon", "mom-admin");

	String solutionName;
	String projectName;
	// cached string
	private transient String id;

	public ProjectId() {
	}

	public ProjectId(String solutionName, String projectName) {
		super();
		this.solutionName = solutionName;
		this.projectName = projectName;
	}

	public ProjectId(String id) {
		setId(id);
	}

	public String getId() {
		String str = id;
		if (str == null) {
			str = id = toString(solutionName, projectName);
		}

		return id;
	}

	public void setId(String id) {
		String[] tokens = StringUtils.split(id, ".");
		this.id = id;
		this.solutionName = tokens[0];
		this.projectName = tokens[1];
	}

	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
		this.id = null;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
		this.id = null;
	}

	public boolean isSameProject(String projectName) {
		return this.projectName.equals(projectName);
	}

	@Override
	public String toString() {
		return getId();
	}

	@Override
	public int compareTo(ProjectId other) {
		if (other == null) {
			return 1;
		}

		return toString().compareTo(other.toString());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass() && !(obj instanceof String)) {
			return false;
		}

		return this.toString().equals(obj.toString());
	}

	public static String toString(String solutionName, String projectName) {
		return solutionName + "." + projectName;
	}

	public static ProjectId parseProjectId(String strProjectId) {
		return new ProjectId(strProjectId);
	}
}
