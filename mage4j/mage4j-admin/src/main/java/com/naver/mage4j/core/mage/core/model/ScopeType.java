package com.naver.mage4j.core.mage.core.model;

import org.apache.commons.lang3.StringUtils;

public enum ScopeType {
	STORE, GROUP, WEBSITE, DEFAULT;

	public static ScopeType fromName(String name) {
		return ScopeType.valueOf(name.toUpperCase());
	}

	public static ScopeType fromName(String name, ScopeType defaultType) {
		if (StringUtils.isBlank(name)) {
			return defaultType;
		}

		try {
			return fromName(name);
		} catch (IllegalArgumentException e) {
			return defaultType;
		}
	}
}
