package com.naver.fog;

public enum ResourceType {
	FIELD(1L, 1000000000L),
	FRAME(1000000001L, 2000000000L),
	TEMPLATE(2000000001L, 3000000000L),
	CONTENT(10000000001L, Long.MAX_VALUE);

	final long minId;
	final long maxId;

	private ResourceType(long minId, long maxId) {
		this.minId = minId;
		this.maxId = maxId;
	}

	public boolean matchs(long resourceId) {
		return minId <= resourceId && resourceId <= maxId;
	}

	public static ResourceType fromResourceId(long resourceId) {
		if (resourceId < FIELD.minId) {
			return null;
		} else if (resourceId <= FIELD.maxId) {
			return FIELD;
		} else if (resourceId <= FRAME.maxId) {
			return FRAME;
		} else if (resourceId <= TEMPLATE.maxId) {
			return TEMPLATE;
		} else if (resourceId >= CONTENT.minId) {
			return CONTENT;
		} else {
			return null;
		}
	}
}
