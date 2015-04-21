package com.pulsarang.infra.config.scope;

public class ConfigScopeSolution implements ConfigScope {
	String solutionName;

	public ConfigScopeSolution(String solutionName) {
		this.solutionName = solutionName;
	}

	public ConfigScopeType getScopeType() {
		return ConfigScopeType.SOLUTION;
	}

	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

}
