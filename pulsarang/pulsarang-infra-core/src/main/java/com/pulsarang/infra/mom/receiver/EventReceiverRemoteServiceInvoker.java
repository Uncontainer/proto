package com.pulsarang.infra.mom.receiver;

import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.infra.remote.RemoteServiceProxy;
import com.pulsarang.infra.remote.RemoteServiceRequest;
import com.pulsarang.infra.remote.reload.ReloadService;
import com.pulsarang.infra.server.Server;

public class EventReceiverRemoteServiceInvoker implements EventReceiver {
	private ReloadService reloadService;

	public EventReceiverRemoteServiceInvoker(Server server) {
		reloadService = RemoteServiceProxy.newServerProxy(ReloadService.class, EventReceiverRemoteService.NAME, server, 3000);
	}

	@Override
	public EventPushResult push(Event event) {
		RemoteServiceRequest option = new RemoteServiceRequest();
		option.setMapModel("event", event);

		EventPushResult eventPushResult;
		try {
			eventPushResult = (EventPushResult) reloadService.add(option);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return eventPushResult;
	}

}
