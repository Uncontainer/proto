package com.naver.mage4j.core.mage.core.model.design;


public enum ThemeType {
	LAYOUT, TEMPLATE, SKIN, LOCALE, DEFAULT;

	final String code;

	private ThemeType() {
		this.code = this.name().toLowerCase();
	}

	public String getCode() {
		return code;
	}

	public static ThemeType fromCode(String code) {
		for (ThemeType type : ThemeType.values()) {
			if (type.code.equals(code)) {
				return type;
			}
		}

		throw new IllegalArgumentException("Invalid ThemeType code.(" + code + ")");
	}
}
