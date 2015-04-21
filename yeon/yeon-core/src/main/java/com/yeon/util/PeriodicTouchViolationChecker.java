package com.yeon.util;

/**
 * 지정된 시간 간격 동간 지정한 횟수 이상의 touch가 있었는지 검사한다.
 * 
 * @author pulsarang
 */
public class PeriodicTouchViolationChecker {
	private final int maxAcceptCount;
	private final long duration;
	private final long points[];

	private int index = 0;

	public PeriodicTouchViolationChecker(int maxAcceptCount, long duration) {
		if (duration < 1) {
			throw new IllegalArgumentException();
		}

		this.duration = duration;
		this.maxAcceptCount = maxAcceptCount;
		if (maxAcceptCount >= 0) {
			this.points = new long[maxAcceptCount];
		} else {
			this.points = null;
		}
	}

	public int getMaxAcceptCount() {
		return maxAcceptCount;
	}

	public long getDuration() {
		return duration;
	}

	/**
	 * touch를 추가한다.
	 * @return 지정된 시간 동안 지정한 횟수 이상의 touch가 있었다면 true, 그렇지 않으면 false
	 */
	public synchronized boolean touch() {
		if (maxAcceptCount == 0) {
			return true;
		} else if (maxAcceptCount < 0) {
			return false;
		}

		long now = System.currentTimeMillis();
		index = (index + 1) % maxAcceptCount;
		points[index] = now;

		// maxAcceptCount 이전에 발생한 예외의 시간의 현재 시간의 차이가 지정된 시간보다 작다면 조건을 만족한다.  
		return (now - points[(index + 1) % maxAcceptCount]) < duration;
	}

	public synchronized boolean isViolated() {
		if (maxAcceptCount == 0) {
			return true;
		} else if (maxAcceptCount < 0) {
			return false;
		}

		long now = System.currentTimeMillis();
		return (now - points[(index + 1) % maxAcceptCount]) < duration;
	}

	public synchronized void ensureIntervalAndTouch() throws InterruptedException {
		if (maxAcceptCount == 0) {
			throw new IllegalStateException();
		} else if (maxAcceptCount < 0) {
			return;
		}

		long now = System.currentTimeMillis();
		int oldestTouchTimeIndex = (index + 1) % maxAcceptCount;
		long maxTouchInterval = now - points[oldestTouchTimeIndex];

		if (maxTouchInterval < duration) {
			Thread.sleep(duration - maxTouchInterval);
			now = System.currentTimeMillis();
		}

		index = oldestTouchTimeIndex;
		points[index] = now;
	}
}
