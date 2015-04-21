package com.pulsarang.infra.update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;

import com.pulsarang.infra.config.ConfigEntityId;
import com.pulsarang.infra.config.scope.ConfigScopeType;
import com.pulsarang.infra.server.SolutionProjectPair;

public class ConfigUpdateRequest {
	private String configCategory;
	private String configName;

	private ConfigScopeType scope;
	private List<String> solutionNames;
	private List<SolutionProjectPair> projectNames;
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

	public void setScopeProject(SolutionProjectPair... pairs) {
		scope = ConfigScopeType.PROJECT;
		this.projectNames = Arrays.asList(pairs);
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

	public List<SolutionProjectPair> getProjectNames() {
		return projectNames;
	}

	public void setStrProjectNames(String strProjectNames) {
		if (StringUtils.isEmpty(strProjectNames)) {
			projectNames = Collections.emptyList();
			return;
		}

		String[] tokens = StringUtils.split(strProjectNames, ",");
		projectNames = new ArrayList<SolutionProjectPair>(tokens.length);
		for (String token : tokens) {
			projectNames.add(new SolutionProjectPair(token));
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
			model.addAttribute("strProjectNames", StringUtils.join(projectNames, ","));
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
