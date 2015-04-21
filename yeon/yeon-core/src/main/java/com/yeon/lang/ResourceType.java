package com.yeon.lang;

public enum ResourceType {
	RESOURCE('R'),
	CLASS('C'),
	PROPERTY('P');

	final char code;

	ResourceType(char code) {
		this.code = code;
	}

	public char getCode() {
		return code;
	}

	public String buildId(String postfix) {
		return code + postfix;
	}

	public String buildId(long serial) {
		return code + String.valueOf(serial);
	}

	public static ResourceType fromCode(char code) {
		switch (code) {
		case 'R':
			return ResourceType.RESOURCE;
		case 'C':
			return ResourceType.CLASS;
		case 'P':
			return ResourceType.PROPERTY;
		default:
			throw new IllegalArgumentException("Unsupported resource type code.(" + code + ")");
		}
	}

	public static ResourceType fromCode(String code) {
		return fromCode(code.charAt(0));
	}

	public static ResourceType fromResourceId(String resourceId) {
		return fromCode(resourceId.charAt(0));
	}
}
