package com.pulsarang.infra.mom.processor;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.remote.annotation.RemoteService;
import com.pulsarang.infra.remote.reload.AbstractReloadService;
import com.pulsarang.infra.server.Server;

/**
 * 
 * @author pulsarang
 */
@RemoteService(name = EventProcessorSubscriberRemoteService.NAME)
public class EventProcessorSubscriberRemoteService extends AbstractReloadService {
	public static final String NAME = "EVENT_PROCESSOR_SUBSCRIBER";
	public static final String PARAM_EVENT_PROCESSOR_INFO = "eventProcessorInfo";
	public static final String PARAM_EVENT_PROCESSOR_NAME = "eventProcessorName";
	public static final String PARAM_SUBSCRIBE_SERVER = "subscribeServer";

	private EventProcessorSubscriber eventReceiver;

	public EventProcessorSubscriberRemoteService(EventProcessorSubscriber eventReceiver) {
		this.eventReceiver = eventReceiver;
	}

	@Override
	public Object add(MapModel option) {
		Server server = option.getMapModel(PARAM_SUBSCRIBE_SERVER, Server.class);
		EventProcessorInfo eventProcessorInfo = option.getMapModel(PARAM_EVENT_PROCESSOR_INFO, EventProcessorInfo.class);

		eventReceiver.subscribe(server, eventProcessorInfo);

		return null;
	}

	@Override
	public int remove(MapModel option) {
		Server server = option.getMapModel(PARAM_SUBSCRIBE_SERVER, Server.class);
		String eventProcessorName = option.getString(PARAM_EVENT_PROCESSOR_NAME);

		eventReceiver.unsubscribe(server, eventProcessorName);

		return 1;
	}

}
