package com.yeon.mom.rabbitmq.producer.mon;

import com.yeon.util.MapModel;

import java.util.Date;
import java.util.Map;

public class EventPublishStatEntry extends MapModel {
	private static final String PARAM_ID = "id";
	private static final String PARAM_REMOVE_COUNT = "rc";
	private static final String PARAM_FAIL_COUNT = "fc";
	private static final String PARAM_QUEUING_COUNT = "qc";
	private static final String PARAM_SUCCESS_COUNT = "sc";
	private static final String PARAM_CREATE_COUNT = "cc";
	private static final String PARAM_LAST_PUSH_TIME = "lpt";

	public EventPublishStatEntry() {
	}

	public EventPublishStatEntry(Map<String, Object> properties) {
		super(properties);
	}

	public EventPublishStatEntry(EventPublishStat eventPublishStat) {
		setId(eventPublishStat.getId());
		setCreateCount(eventPublishStat.getCreateCount());
		setSuccessCount(eventPublishStat.getSuccessCount());
		setQueuingCount(eventPublishStat.getQueuingCount());
		setFailCount(eventPublishStat.getFailCount());
		setRemoveCount(eventPublishStat.getRemoveCount());
		setLastPushTime(eventPublishStat.getLastPushTime());
	}

	public String getId() {
		return getString(PARAM_ID);
	}

	public void setId(String id) {
		setString(PARAM_ID, id);
	}

	public Date getLastPushTime() {
		Long lastPopTime = getLong(PARAM_LAST_PUSH_TIME);
		if (lastPopTime == null) {
			return new Date(0);
		} else {
			return new Date(lastPopTime);
		}
	}

	public void setLastPushTime(Date lastPopTime) {
		if (lastPopTime == null) {
			removeValue(PARAM_LAST_PUSH_TIME);
		} else {
			setValue(PARAM_LAST_PUSH_TIME, lastPopTime.getTime());
		}
	}

	public void updateLastPushTime(Date other) {
		if (other == null) {
			return;
		}

		if (getLastPushTime().before(other)) {
			setLastPushTime(other);
		}
	}

	public long getCreateCount() {
		return getLong(PARAM_CREATE_COUNT, 0L);
	}

	public void setCreateCount(long createCount) {
		setLong(PARAM_CREATE_COUNT, createCount);
	}

	public void addCreateCount(long count) {
		setCreateCount(getCreateCount() + count);
	}

	public long getSuccessCount() {
		return getLong(PARAM_SUCCESS_COUNT, 0L);
	}

	public void setSuccessCount(long successCount) {
		setLong(PARAM_SUCCESS_COUNT, successCount);
	}

	public void addSuccessCount(long count) {
		setSuccessCount(count);
	}

	public long getQueuingCount() {
		return getLong(PARAM_QUEUING_COUNT, 0L);
	}

	public void setQueuingCount(long queuingCount) {
		setLong(PARAM_QUEUING_COUNT, queuingCount);
	}

	public void addQueuingCount(long count) {
		setQueuingCount(getQueuingCount() + count);
	}

	public long getFailCount() {
		return getLong(PARAM_FAIL_COUNT, 0L);
	}

	public void setFailCount(long failCount) {
		setLong(PARAM_FAIL_COUNT, failCount);
	}

	public void addFailCount(long count) {
		setFailCount(getFailCount() + count);
	}

	public long getRemoveCount() {
		return getLong(PARAM_REMOVE_COUNT, 0L);
	}

	public void setRemoveCount(long removeCount) {
		setLong(PARAM_REMOVE_COUNT, removeCount);
	}

	public void addRemoveCount(long count) {
		setRemoveCount(getRemoveCount() + count);
	}

	public void merge(EventPublishStatEntry other) {
		if (!getId().equals(other.getId())) {
			return;
		}

		forceMerge(other);
	}

	public void forceMerge(EventPublishStatEntry other) {
		addCreateCount(other.getCreateCount());
		addSuccessCount(other.getSuccessCount());
		addQueuingCount(other.getQueuingCount());
		addFailCount(other.getFailCount());
		addRemoveCount(other.getRemoveCount());
		updateLastPushTime(other.getLastPushTime());
	}

	public EventPublishStatEntry diff(EventPublishStatEntry old) {
		if (old == null) {
			throw new IllegalArgumentException("Null entry");
		}

		if (getId() != null && !getId().equals(old.getId())) {
			throw new IllegalArgumentException("Id is different.");
		}

		EventPublishStatEntry result = new EventPublishStatEntry();
		result.setId(getId());
		result.setLastPushTime(getLastPushTime());
		result.setCreateCount(getCreateCount() - old.getCreateCount());
		result.setSuccessCount(getSuccessCount() - old.getSuccessCount());
		result.setQueuingCount(getQueuingCount() - old.getQueuingCount());
		result.setFailCount(getFailCount() - old.getFailCount());
		result.setRemoveCount(getRemoveCount() - old.getRemoveCount());

		return result;
	}
}
