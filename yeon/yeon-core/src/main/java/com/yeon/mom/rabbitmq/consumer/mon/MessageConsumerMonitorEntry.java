package com.yeon.mom.rabbitmq.consumer.mon;

import com.yeon.mom.rabbitmq.consumer.MessageConsumer;
import com.yeon.util.MapModel;

import java.util.Date;
import java.util.Map;

/**
 * 
 * @author pulsarang
 */
public class MessageConsumerMonitorEntry extends MapModel {
	protected static final String PARAM_ID = "id";
	protected static final String PARAM_STATUS = "s";
	protected static final String PARAM_IDLE_SINCE = "is";
	protected static final String PARAM_RECEIVE_COUNT = "rc";

	public MessageConsumerMonitorEntry() {
	}

	public MessageConsumerMonitorEntry(Map<String, Object> properties) {
		super(properties);
	}

	public MessageConsumerMonitorEntry(int id, MessageConsumer messageConsumer) {
		if (messageConsumer == null) {
			throw new IllegalArgumentException("Null MessageConsumer.");
		}

		setId(id);
		setStatus(messageConsumer.getStatus(false).name());
		setIdleSinceTime(messageConsumer.getIdleSinceTime());
		setReceiveCount(messageConsumer.getReceiveCount());
	}

	public int getId() {
		return getInteger(PARAM_ID);
	}

	public void setId(int id) {
		setInteger(PARAM_ID, id);
	}

	public String getStatus() {
		return (String) getValue(PARAM_STATUS);
	}

	public void setStatus(String status) {
		setString(PARAM_STATUS, status);
	}

	public Date getIdleSince() {
		return getDate(PARAM_IDLE_SINCE, null);
	}

	public void setIdleSince(Date lastReceiveMessageDate) {
		setDate(PARAM_IDLE_SINCE, lastReceiveMessageDate);
	}

	public long getIdleSinceTime() {
		return getLong(PARAM_IDLE_SINCE, 0L);
	}

	public void setIdleSinceTime(long lastReceiveMessageTime) {
		if (lastReceiveMessageTime == 0L) {
			removeValue(PARAM_IDLE_SINCE);
		} else {
			setLong(PARAM_IDLE_SINCE, lastReceiveMessageTime);
		}
	}

	public int getReceiveCount() {
		return getValue(PARAM_RECEIVE_COUNT, 0);
	}

	public void setReceiveCount(int receiveCount) {
		setInteger(PARAM_RECEIVE_COUNT, receiveCount);
	}
}
