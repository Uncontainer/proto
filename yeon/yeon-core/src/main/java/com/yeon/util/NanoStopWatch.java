package com.yeon.util;

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
