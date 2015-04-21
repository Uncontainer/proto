package com.yeon.mom.rabbitmq.consumer.mon;

import com.yeon.YeonParameters;
import com.yeon.mom.rabbitmq.consumer.MessageConsumer;
import com.yeon.mom.rabbitmq.consumer.MessageConsumerGroup;
import com.yeon.util.MapModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author pulsarang
 */
public class MessageConsumerGroupMonitorEntry extends MapModel {
	protected static final String PARAM_STATUS = "s";
	protected static final String PARAM_ADD_COUNT = "ac";
	protected static final String PARAM_REMOVE_COUNT = "rc";
	protected static final String PARAM_SUBSCRIBE_ENABLED = "se";
	protected static final String PARAM_PROCESSOR_SUBSCRIBE_ENABLED = "pse";
	protected static final String PARAM_MESSAGE_CONSUMERS = "mcs";

	public MessageConsumerGroupMonitorEntry() {
	}

	public MessageConsumerGroupMonitorEntry(Map<String, Object> properties) {
		super(properties);
	}

	public MessageConsumerGroupMonitorEntry(String processorName, MessageConsumerGroup messageConsumerGroup) {
		if (messageConsumerGroup == null) {
			throw new IllegalArgumentException("Null messageConsumerGroup");
		}

		setProcessorName(processorName);
		setStatus(messageConsumerGroup.getStatus().name());
		setAddCount(messageConsumerGroup.getAddCount());
		setRemoveCount(messageConsumerGroup.getRemoveCount());
		setSubscribeEnabled(messageConsumerGroup.isSubscribeEnabled());
		setProcessorSubscribeEnabled(messageConsumerGroup.getEventProcessorInfo().isSubscribeEnabled());

		List<MessageConsumerMonitorEntry> messageCosumers = new ArrayList<MessageConsumerMonitorEntry>();
		for (Entry<Integer, MessageConsumer> mcEntry : messageConsumerGroup.getMessageConsumerMap().entrySet()) {
			MessageConsumerMonitorEntry mcme = new MessageConsumerMonitorEntry(mcEntry.getKey(), mcEntry.getValue());
			messageCosumers.add(mcme);
		}
		setMessageConsumers(messageCosumers);
	}

	public String getProcessorName() {
		return getString(YeonParameters.PROCESSOR_NAME);
	}

	public void setProcessorName(String processorName) {
		setString(YeonParameters.PROCESSOR_NAME, processorName);
	}

	public String getStatus() {
		return getString(PARAM_STATUS);
	}

	public void setStatus(String status) {
		setString(PARAM_STATUS, status);
	}

	public int getAddCount() {
		return getInteger(PARAM_ADD_COUNT, -1);
	}

	public void setAddCount(int count) {
		setInteger(PARAM_ADD_COUNT, count);
	}

	public int getRemoveCount() {
		return getInteger(PARAM_REMOVE_COUNT, -1);
	}

	public void setRemoveCount(int count) {
		setInteger(PARAM_REMOVE_COUNT, count);
	}

	public Boolean getSubscribeEnabled() {
		return getBoolean(PARAM_SUBSCRIBE_ENABLED);
	}

	public void setSubscribeEnabled(Boolean subscribeEnabled) {
		setBoolean(PARAM_SUBSCRIBE_ENABLED, subscribeEnabled);
	}

	public Boolean getProcessorSubscribeEnabled() {
		return getBoolean(PARAM_PROCESSOR_SUBSCRIBE_ENABLED);
	}

	public void setProcessorSubscribeEnabled(Boolean subscribeEnabled) {
		setBoolean(PARAM_PROCESSOR_SUBSCRIBE_ENABLED, subscribeEnabled);
	}

	public List<MessageConsumerMonitorEntry> getMessageConsumers() {
		List<Map<String, Object>> mapList = getList(PARAM_MESSAGE_CONSUMERS);

		if (mapList == null) {
			return null;
		}

		List<MessageConsumerMonitorEntry> messageConsumeres = new ArrayList<MessageConsumerMonitorEntry>(mapList.size());
		for (Map<String, Object> map : mapList) {
			messageConsumeres.add(new MessageConsumerMonitorEntry(map));
		}

		return messageConsumeres;
	}

	public void setMessageConsumers(List<MessageConsumerMonitorEntry> messageConsumeres) {
		if (messageConsumeres == null) {
			removeValue(PARAM_MESSAGE_CONSUMERS);
		} else {
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>(messageConsumeres.size());
			for (MessageConsumerMonitorEntry messageConsumer : messageConsumeres) {
				mapList.add(messageConsumer.getValues());
			}

			setList(PARAM_MESSAGE_CONSUMERS, mapList);
		}
	}
}
