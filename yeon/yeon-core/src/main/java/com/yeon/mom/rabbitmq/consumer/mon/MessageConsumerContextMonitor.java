package com.yeon.mom.rabbitmq.consumer.mon;

import com.yeon.YeonParameters;
import com.yeon.mom.rabbitmq.consumer.MessageConsumerContext;
import com.yeon.mom.rabbitmq.consumer.MessageConsumerGroup;
import com.yeon.remote.reload.AbstractReloadService;
import com.yeon.util.MapModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author pulsarang
 */
public class MessageConsumerContextMonitor extends AbstractReloadService implements Runnable {
	public static final String NAME = "MESSAGE_CONSUMER_CONTEXT";

	private final MessageConsumerContext messageConsumerContext;

	public MessageConsumerContextMonitor(MessageConsumerContext messageConsumerContext) {
		super();
		this.messageConsumerContext = messageConsumerContext;
	}

	@Override
	public void run() {
		Map<String, MessageConsumerGroup> messageConsumerGroupMap = messageConsumerContext.getMessageConsumerGroupMap();

		for (Entry<String, MessageConsumerGroup> entry : messageConsumerGroupMap.entrySet()) {
			MessageConsumerGroup messageConsumerGroup = entry.getValue();
			messageConsumerGroup.ensureLiveThreadCount();
		}
	}

	@Override
	public List<MapModel> list(MapModel optionMap) {
		List<MapModel> result = new ArrayList<MapModel>();
		Map<String, MessageConsumerGroup> messageConsumerGroupMap = messageConsumerContext.getMessageConsumerGroupMap();

		for (Entry<String, MessageConsumerGroup> entry : messageConsumerGroupMap.entrySet()) {
			result.add(new MessageConsumerGroupMonitorEntry(entry.getKey(), entry.getValue()));
		}

		return result;
	}

	@Override
	public int listCount(MapModel optionMap) {
		return messageConsumerContext.getMessageConsumerGroupMap().size();
	}

	@Override
	public MapModel get(MapModel optionMap) {
		String processorName = (String) optionMap.get(YeonParameters.PROCESSOR_NAME);
		MessageConsumerGroup messageConsumerGroup = messageConsumerContext.getMessageConsumerGroup(processorName);
		if (messageConsumerGroup == null) {
			return null;
		}

		return new MessageConsumerGroupMonitorEntry(processorName, messageConsumerGroup);
	}

	@Override
	public void refresh(MapModel optionMap) {
		String procesorName = (String) optionMap.get(YeonParameters.PROCESSOR_NAME);
		MessageConsumerGroup messageConsumerGroup = messageConsumerContext.getMessageConsumerGroup(procesorName);

		if (messageConsumerGroup != null) {
			messageConsumerGroup.reconnect();
		}
	}
}
