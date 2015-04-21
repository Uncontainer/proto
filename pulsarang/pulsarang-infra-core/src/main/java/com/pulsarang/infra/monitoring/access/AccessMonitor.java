/*
 * TPSMonitor.java $version 2010. 10. 15
 * 
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.pulsarang.infra.monitoring.access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.pulsarang.core.util.Tracable;

/**
 * 
 * @author pulsarang
 */
public class AccessMonitor {
	private final AtomicLong total;

	private volatile int count = 0;
	private volatile int duration = 0;
	private volatile Entry[] record;

	public long getTotal() {
		return total.get();
	}

	public int getCount() {
		return count;
	}

	public int getDuration() {
		return duration;
	}

	public AccessMonitor(int count, int duration) {
		total = new AtomicLong();

		setCountAndDuration(count, duration);
	}

	public void setCountAndDuration(int newCount, int newDuration) {
		Entry[] newRecord = new Entry[newCount];
		for (int i = 0; i < newCount; i++) {
			newRecord[i] = new Entry();
		}

		// 값 변경 중에 발생할 수 있는 약간의 오차는 무시함으로써 전체적으로 동기화가 필요한 구간을 줄인다.
		if (newCount > count) {
			this.record = newRecord;
			this.count = newCount;
			this.duration = newDuration;
		} else {
			this.count = newCount;
			this.record = newRecord;
			this.duration = newDuration;
		}
	}

	public void addResult(Tracable tracable, boolean success) {
		total.incrementAndGet();

		long startTime = tracable.getStartTime() / duration;
		int index = (int)(startTime % count);
		startTime = startTime * duration;

		synchronized (this) {
			if (startTime > record[index].startTime) {
				record[index].reset(startTime);
			}
		}

		// // 최대 기록 유지 시간(count*period) 이상의 응답시간을 경우
		// else if (startTime < record[index].startTime) {
		// // do nothing
		// }

		record[index].addResponse(tracable, success);
	}

	public List<Entry> getCurrentStatus(long fromTime) {
		List<Entry> list = new ArrayList<Entry>(count);
		long currentTime = System.currentTimeMillis() / duration;
		int index = (int)(currentTime % count);
		currentTime *= duration;
		long outdatedTime = currentTime - count * duration;

		synchronized (this) {
			if (record[index].getStartTime() != currentTime) {
				record[index].reset(currentTime);
			}
		}

		for (int i = 0; i < count; i++) {
			if (record[index].getStartTime() <= outdatedTime) {
				break;
			}

			if (record[index].getStartTime() < fromTime) {
				break;
			}

			list.add(record[index]);
			index = (index - 1 + count) % count;
		}

		return list;
	}

	public List<Map<String, Object>> getCurrentStatusByMap(long fromTime) {
		List<Entry> currentStatus = getCurrentStatus(fromTime);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(currentStatus.size());
		for (Entry entry : currentStatus) {
			list.add(Entry.toMap(entry, duration));
		}

		return list;
	}

	/**
	 * 
	 * @author pulsarang
	 */
	public static class Entry {
		private long startTime;
		private AtomicInteger successCount = new AtomicInteger(0);
		private AtomicInteger failCount = new AtomicInteger(0);
		private AtomicLong totalSpentTime = new AtomicLong(0);
		private final SlowestRequest slowestRequest = new SlowestRequest(3);

		void reset(long startTime) {
			this.startTime = startTime;
			successCount = new AtomicInteger(0);
			failCount = new AtomicInteger(0);
			totalSpentTime = new AtomicLong(0);
			slowestRequest.reset();
		}

		public int getMeanResponseTime() {
			if (totalSpentTime.get() == 0) {
				return -1;
			}

			return (int)(totalSpentTime.get() / (successCount.get() + failCount.get()));
		}

		public void addResponse(Tracable tracable, boolean success) {
			totalSpentTime.addAndGet(tracable.getTimeInMillies());
			if (success) {
				successCount.incrementAndGet();
			} else {
				failCount.incrementAndGet();
			}

			slowestRequest.add(tracable);
		}

		public int getSuccessCount() {
			return successCount.get();
		}

		public int getFailCount() {
			return failCount.get();
		}

		public long getTotalSpentTime() {
			return totalSpentTime.get();
		}

		public long getStartTime() {
			return startTime;
		}

		public List<Long> getSlowestTimes() {
			return slowestRequest.getTimes();
		}

		public static Map<String, Object> toMap(Entry entry, int duration) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("startTime", entry.startTime);
			map.put("duration", duration);
			map.put("successCount", entry.successCount.get());
			map.put("failCount", entry.failCount.get());
			map.put("totalSpentTime", entry.totalSpentTime.get());
			map.put("slowestTimes", entry.getSlowestTimes());

			return map;
		}
	}

}
