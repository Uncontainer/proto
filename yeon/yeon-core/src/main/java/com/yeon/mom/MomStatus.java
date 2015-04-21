package com.yeon.mom;

/**
 * 
 * @author pulsarang
 */
public enum MomStatus {
	NORMAL(true, true), PUBLISH_ONLY(true, false), PROCESS_ONLY(false, true), PAUSE(false, false);

	final boolean publishEnabled;
	final boolean processEnabled;

	private MomStatus(boolean publishEnabled, boolean processEnabled) {
		this.publishEnabled = publishEnabled;
		this.processEnabled = processEnabled;
	}

	public boolean isPublishEnabled() {
		return publishEnabled;
	}

	public boolean isPublishDisabled() {
		return !publishEnabled;
	}

	public boolean isProcessEnabled() {
		return processEnabled;
	}

	public boolean isProcessDisabled() {
		return !processEnabled;
	}
}
