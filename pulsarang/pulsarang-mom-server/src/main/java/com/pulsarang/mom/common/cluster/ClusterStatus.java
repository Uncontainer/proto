/*
 * ClusterStatus.java $version 2010. 10. 15
 * 
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.pulsarang.mom.common.cluster;

/**
 * 
 * @author pulsarang
 */
public enum ClusterStatus {
	NORMAL(true, true, true), //
	ACCEPTOR_ONLY(true, true, false), //
	DISPATCHER_ONLY(true, false, true), //
	// DB_FAIL(false, false, false), //
	STOP(true, false, false);

	private ClusterStatus(boolean refreshable, boolean acceptorActive, boolean dispatcherActive) {
		this.refreshable = refreshable;
		this.acceptorActive = acceptorActive;
		this.dispatcherActive = dispatcherActive;
	}

	final boolean refreshable;
	final boolean acceptorActive;
	final boolean dispatcherActive;

	public boolean isRefreshable() {
		return refreshable;
	}

	public boolean isAcceptorActive() {
		return acceptorActive;
	}

	public boolean isDispatcherActive() {
		return dispatcherActive;
	}
}
