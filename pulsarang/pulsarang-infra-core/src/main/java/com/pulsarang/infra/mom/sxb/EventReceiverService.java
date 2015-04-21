package com.pulsarang.infra.mom.sxb;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.mom.sxb.processor.EventProcessRequest;
import com.pulsarang.infra.mom.sxb.processor.SxbEventDispatcher;
import com.pulsarang.infra.remote.annotation.RemoteService;
import com.pulsarang.infra.remote.reload.AbstractReloadService;

@RemoteService(name = EventReceiverService.NAME)
public class EventReceiverService extends AbstractReloadService {
	public static final String NAME = "EventReceiver";

	private SxbEventDispatcher eventDispatcher;

	public EventReceiverService(SxbEventDispatcher eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}

	@Override
	public Object add(MapModel option) {
		EventProcessRequest request = MapModel.convert(option, EventProcessRequest.class);

		return eventDispatcher.onEvent(request);
	}
}
