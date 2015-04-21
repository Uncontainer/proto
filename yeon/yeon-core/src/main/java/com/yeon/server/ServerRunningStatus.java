package com.yeon.server;

/**
 * @author EC셀러서비스개발팀
 * 
 */
public enum ServerRunningStatus {
	RUNNING(true), NO_REPLY(false), SHUTDOWN(false);

	final boolean alive;

	private ServerRunningStatus(boolean alive) {
		this.alive = alive;
	}

	public boolean isAlive() {
		return alive;
	}
}
