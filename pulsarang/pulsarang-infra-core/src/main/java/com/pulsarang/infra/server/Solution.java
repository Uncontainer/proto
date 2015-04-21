package com.pulsarang.infra.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Solution implements Comparable<Solution> {
	public static final String SOLUTION_DUMMY = "_dummy";

	private final String name;
	private final Map<String, Project> projects;

	public Solution(String name, List<Project> projects) {
		super();
		if (name == null) {
			throw new IllegalArgumentException("Empty solution name.");
		}
		this.name = name;

		this.projects = new HashMap<String, Project>();
		if (projects != null) {
			for (Project project : projects) {
				this.projects.put(project.getName(), project);
			}
		}
	}

	public String getName() {
		return name;
	}

	public Set<String> getProjectNames() {
		return projects.keySet();
	}

	public Collection<Project> getProjects() {
		return projects.values();
	}

	public Project getProject(String projectName) {
		return projects.get(projectName);
	}

	public List<Server> getServers() {
		List<Server> servers = new ArrayList<Server>();
		for (Project project : getProjects()) {
			servers.addAll(project.getServers());
		}

		return servers;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return name.toString();
	}

	@Override
	public int compareTo(Solution o) {
		if (o == null) {
			return 1;
		}

		return this.name.compareTo(o.name);
	}
}
