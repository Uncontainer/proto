/*
 * NanoStopWatch.java $version 2010. 10. 15
 * 
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.pulsarang.core.util;

/**
 * 
 * @author pulsarang
 */
public class NanoStopWatch implements Tracable {
	private long startTime;
	private long statTimeInNanos;
	private long stopTimeInNanos;

	public void start() {
		startTime = System.currentTimeMillis();
		statTimeInNanos = System.nanoTime();
	}

	public void stop() {
		stopTimeInNanos = System.nanoTime();
	}

	public long getTimeInNanos() {
		return stopTimeInNanos - statTimeInNanos;
	}

	public long getTimeInMillies() {
		return (stopTimeInNanos - statTimeInNanos) / 1000000L;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return startTime + getTimeInMillies();
	}
}
