package com.yeon.remote.bulk;

/**
 * 
 * @author pulsarang
 */
public enum RemoteServiceInvokeSkipReason {
	LOCALHOST("localhost"), NOT_ALIVE("not alive");

	final String text;

	private RemoteServiceInvokeSkipReason(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
