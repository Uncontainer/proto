package com.yeon.mom.rabbitmq.consumer;

import com.yeon.mom.event.Event;
import com.yeon.mom.util.EventUtils;
import com.yeon.remote.reload.AbstractReloadService;
import com.yeon.util.MapModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author pulsarang
 */
public class LocalEventReceiver extends AbstractReloadService {
	public static final String NAME = "LocalEventReceiver";

	private final List<LocalMessageConsumer> messageConsumers = new ArrayList<LocalMessageConsumer>();

	public void addMessageConsumer(LocalMessageConsumer messageConsumer) {
		for (LocalMessageConsumer executor : messageConsumers) {
			if (executor.getEventProcessor() == messageConsumer.getEventProcessor()) {
				return;
			}
		}

		messageConsumers.add(messageConsumer);
	}

	@Override
	public Object add(MapModel option) {
		Map<String, Object> mapEvent = option.getMap("event");
		Event event = EventUtils.convertEvent(new Event(mapEvent));
		if (event == null) {
			throw new IllegalArgumentException();
		}

		for (LocalMessageConsumer messageConsumer : messageConsumers) {
			messageConsumer.process(event);
		}

		return null;
	}
}
