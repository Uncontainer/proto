/*
 * PeriodicTouchViolationChecker.java $version 2010. 10. 15
 * 
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.pulsarang.core.util;

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
		if (duration < 1 || maxAcceptCount < 1) {
			throw new IllegalArgumentException();
		}

		this.duration = duration;
		this.maxAcceptCount = maxAcceptCount;
		this.points = new long[maxAcceptCount];
	}

	/**
	 * touch를 추가한다.
	 * @return 지정된 시간 동안 지정한 횟수 이상의 touch가 있었다면 true, 그렇지 않으면 false
	 */
	public synchronized boolean touch() {
		long now = System.currentTimeMillis();
		index = (index + 1) % maxAcceptCount;
		points[index] = now;

		// maxAcceptCount 이전에 발생한 예외의 시간의 현재 시간의 차이가 지정된 시간보다 작다면 조건을 만족한다.  
		return (now - points[(index + 1) % maxAcceptCount]) < duration;
	}

	public synchronized boolean isViolated() {
		long now = System.currentTimeMillis();
		return (now - points[(index + 1) % maxAcceptCount]) < duration;
	}
}
