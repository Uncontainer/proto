package com.yeon.mom;

import com.yeon.YeonContext;
import com.yeon.server.ProjectId;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author pulsarang
 */
public class EventProcessorInfoId {
	private final ProjectId projectId;
	private final String name;

	public EventProcessorInfoId(String canonicalName) {
		String[] tokens = StringUtils.split(canonicalName, ".");
		if (tokens.length != 3) {
			throw new IllegalArgumentException("'" + canonicalName + "' is not a canonicalEventProcessorName.");
		}

		this.projectId = new ProjectId(tokens[0], tokens[1]);
		this.name = tokens[2];
	}

	public EventProcessorInfoId(String solutionName, String projectName, String name) {
		this(new ProjectId(solutionName, projectName), name);
	}

	public EventProcessorInfoId(ProjectId projectId, String name) {
		super();
		this.projectId = projectId;
		this.name = name;
	}

	public ProjectId getProjectId() {
		return projectId;
	}

	public String getSolutionName() {
		return projectId.getSolutionName();
	}

	public String getProjectName() {
		return projectId.getProjectName();
	}

	public String getName() {
		return name;
	}

	public static String toString(String solutionName, String projectName, String simpleName) {
		return solutionName + "." + projectName + "." + simpleName;
	}

	public static boolean isCanonicalEventProcessorName(String eventProcessorName) {
		return eventProcessorName.contains(".");
	}

	public static String getCanonicalEventProcessorName(String eventProcessorName) {
		if (eventProcessorName.contains(".")) {
			throw new IllegalArgumentException("'" + eventProcessorName + "' is not a simpleEventProcessorName.");
		}
		
		

		return toString(YeonContext.getSolutionName(), YeonContext.getProjectName(), eventProcessorName);
	}

	public static ProjectId getProjectId(String canonicalName) {
		String[] tokens = StringUtils.split(canonicalName, ".");
		if (tokens.length != 3) {
			throw new IllegalArgumentException("'" + canonicalName + "' is not a canonicalEventProcessorName.");
		}

		return new ProjectId(tokens[0], tokens[1]);
	}

	public static String getSimpleEventProcessorName(String eventProcessorName) {
		int lastDotIndex = eventProcessorName.lastIndexOf('.');
		if (lastDotIndex == -1) {
			throw new IllegalArgumentException("'" + eventProcessorName + "' is not a canonicalEventProcessorName.");
		} else {
			return eventProcessorName.substring(lastDotIndex + 1);
		}
	}
}
