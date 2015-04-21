package com.yeon.mom.rabbitmq.producer.mon;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author EC셀러서비스개발팀
 */
public class EventPublishStat {
	private final String id;
	private Date lastPushTime = new Date(0);
	private final AtomicLong createCount = new AtomicLong();
	private final AtomicLong successCount = new AtomicLong();
	private final AtomicLong queuingCount = new AtomicLong();
	private final AtomicLong failCount = new AtomicLong();
	private final AtomicLong removeCount = new AtomicLong();

	public EventPublishStat(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public Date getLastPushTime() {
		return lastPushTime;
	}

	public void increaseCreateCount() {
		createCount.incrementAndGet();
	}

	public void increaseQueuingCount() {
		queuingCount.incrementAndGet();
	}

	public void increaseSuccessCount() {
		successCount.incrementAndGet();
		lastPushTime = new Date();
	}

	public void increaseFailCount() {
		failCount.incrementAndGet();
	}

	public void increaseRemoveCount() {
		removeCount.incrementAndGet();
	}

	public long getCreateCount() {
		return createCount.get();
	}

	public long getSuccessCount() {
		return successCount.get();
	}

	public long getQueuingCount() {
		return queuingCount.get();
	}

	public long getFailCount() {
		return failCount.get();
	}

	public long getRemoveCount() {
		return removeCount.get();
	}

	public void clearCount() {
		createCount.set(0L);
		successCount.set(0L);
		queuingCount.set(0L);
		failCount.set(0L);
		removeCount.set(0L);
	}

	public EventPublishStatEntry toEntry() {
		return new EventPublishStatEntry(this);
	}
}
