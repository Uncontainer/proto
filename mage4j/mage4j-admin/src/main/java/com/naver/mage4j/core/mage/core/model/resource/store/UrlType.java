package com.naver.mage4j.core.mage.core.model.resource.store;

public enum UrlType {
	LINK,
	DIRECT_LINK,
	WEB,
	SKIN,
	JS,
	MEDIA;

	final String code;

	UrlType() {
		this.code = this.name().toLowerCase();
	}

	public String getCode() {
		return code;
	}

	public static UrlType fromCode(String code) {
		return UrlType.valueOf(code.toUpperCase());
	}
}
