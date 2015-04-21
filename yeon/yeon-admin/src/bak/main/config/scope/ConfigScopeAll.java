package com.pulsarang.infra.config.scope;

public class ConfigScopeAll implements ConfigScope {
	public ConfigScopeType getScopeType() {
		return ConfigScopeType.ALL;
	}
}
