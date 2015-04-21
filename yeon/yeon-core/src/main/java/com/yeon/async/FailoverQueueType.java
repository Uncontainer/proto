package com.yeon.async;

public enum FailoverQueueType {
	MEMORY(false),
	FILE(true),
	HYBRID(true);

	final boolean persist;

	private FailoverQueueType(boolean persist) {
		this.persist = persist;
	}

	public boolean isPersist() {
		return persist;
	}
}
