package com.pulsarang.infra.update;

import com.pulsarang.infra.config.ConfigEntityId;
import com.pulsarang.infra.config.scope.ConfigScopeType;
import com.yeon.server.ProjectId;
import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConfigUpdateRequest {
	private String configCategory;
	private String configName;

	private ConfigScopeType scope;
	private List<String> solutionNames;
	private List<ProjectId> projectIds;
	private String callbackUrl;

	private int timeout = 5000;

	public ConfigUpdateRequest() {
	}

	public ConfigUpdateRequest(ConfigEntityId configEntityId) {
		this.configCategory = configEntityId.getCategory();
		this.configName = configEntityId.getName();
	}

	public String getConfigCategory() {
		return configCategory;
	}

	public void setConfigCategory(String configCategory) {
		this.configCategory = configCategory;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public void setScopeAllSolution() {
		scope = ConfigScopeType.ALL;
	}

	public void setScopeSolution(String... solutionNames) {
		scope = ConfigScopeType.SOLUTION;
		this.solutionNames = Arrays.asList(solutionNames);
	}

	public void setScopeProject(ProjectId... pairs) {
		scope = ConfigScopeType.PROJECT;
		this.projectIds = Arrays.asList(pairs);
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public ConfigScopeType getScope() {
		return scope;
	}

	public void setScope(ConfigScopeType scope) {
		this.scope = scope;
	}

	public List<String> getSolutionNames() {
		return solutionNames;
	}

	public List<ProjectId> getProjectNames() {
		return projectIds;
	}

	public void setStrProjectNames(String strProjectNames) {
		if (StringUtils.isEmpty(strProjectNames)) {
			projectIds = Collections.emptyList();
			return;
		}

		String[] tokens = StringUtils.split(strProjectNames, ",");
		projectIds = new ArrayList<ProjectId>(tokens.length);
		for (String token : tokens) {
			projectIds.add(new ProjectId(token));
		}
	}

	public void setStrSolutionNames(String strSolutionNames) {
		if (StringUtils.isEmpty(strSolutionNames)) {
			solutionNames = Collections.emptyList();
			return;
		}

		solutionNames = Arrays.asList(StringUtils.split(strSolutionNames, ","));
	}

	public void setScopeName(String scopeName) {
		this.scope = ConfigScopeType.valueOf(scopeName);
	}

	public String redirect(ModelMap model) {
		model.addAttribute("configCategory", configCategory);
		model.addAttribute("configName", configName);
		model.addAttribute("callbackUrl", callbackUrl);

		model.addAttribute("scopeName", scope.name());
		switch (scope) {
		case PROJECT:
			model.addAttribute("strProjectNames", StringUtils.join(projectIds, ","));
			break;
		case SOLUTION:
			model.addAttribute("strSolutionNames", StringUtils.join(solutionNames, ","));
			break;
		case ALL:
			break;
		default:
			throw new UnsupportedOperationException("Unsupport configUpdateScope.(" + scope + ")");
		}

		return "redirect:/update/refresh";
	}
}
