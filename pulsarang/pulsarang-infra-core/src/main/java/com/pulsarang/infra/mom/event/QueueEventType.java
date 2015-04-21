package com.pulsarang.infra.mom.event;

import org.apache.commons.lang.StringUtils;

public class QueueEventType implements EventType {
	private final String queuenName;
	private String code;

	public QueueEventType(String queuenName) {
		super();
		if (StringUtils.isEmpty(queuenName)) {
			throw new IllegalArgumentException("Empty queue name");
		}

		this.queuenName = queuenName;
	}

	@Override
	public String getClassName() {
		return EventType.CLASS_QUEUE;
	}

	@Override
	public String getOperationName() {
		return queuenName;
	}

	public String getQueueName() {
		return queuenName;
	}

	@Override
	public String getCode() {
		if (code == null) {
			code = EventTypeUtils.encode(this);
		}

		return code;
	}

	@Override
	public String toString() {
		return getCode();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof QueueEventType)) {
			return false;
		}

		return this.queuenName.equals(((QueueEventType) obj).queuenName);
	}
}
