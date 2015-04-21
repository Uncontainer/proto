package com.pulsarang.infra.mom.receiver;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.infra.remote.annotation.RemoteService;
import com.pulsarang.infra.remote.reload.AbstractReloadService;

/**
 * 
 * @author pulsarang
 */
@RemoteService(name = EventReceiverRemoteService.NAME)
public class EventReceiverRemoteService extends AbstractReloadService {
	public static final String NAME = "EVENT_RECEIVER";

	private EventReceiver eventReceiver;

	public EventReceiverRemoteService(EventReceiver eventReceiver) {
		this.eventReceiver = eventReceiver;
	}

	@Override
	public Object add(MapModel option) {
		Event event = option.getMapModel("event", Event.class);

		EventPushResult pushResult = eventReceiver.push(event);

		return pushResult;
	}
}
