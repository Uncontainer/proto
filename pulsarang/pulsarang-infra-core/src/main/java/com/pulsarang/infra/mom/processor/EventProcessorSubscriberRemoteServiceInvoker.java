package com.pulsarang.infra.mom.processor;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.remote.RemoteServiceProxy;
import com.pulsarang.infra.remote.reload.ReloadService;
import com.pulsarang.infra.server.Server;

public class EventProcessorSubscriberRemoteServiceInvoker implements EventProcessorSubscriber {
	private ReloadService reloadService;

	public EventProcessorSubscriberRemoteServiceInvoker(Server server) {
		reloadService = RemoteServiceProxy.newServerProxy(ReloadService.class, EventProcessorSubscriberRemoteService.NAME, server, 3000);
	}

	public void subscribe(EventProcessorInfo eventProcessorInfo) {
		Server server = InfraContextFactory.getInfraContext().getServerContext().getLocalServer();
		subscribe(server, eventProcessorInfo);
	}

	@Override
	public void subscribe(Server server, EventProcessorInfo eventProcessorInfo) {
		MapModel option = new MapModel(4);
		option.setMapModel(EventProcessorSubscriberRemoteService.PARAM_SUBSCRIBE_SERVER, server);
		option.setMapModel(EventProcessorSubscriberRemoteService.PARAM_EVENT_PROCESSOR_INFO, eventProcessorInfo);
		try {
			reloadService.add(option);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void unsubscribe(String name) {
		Server server = InfraContextFactory.getInfraContext().getServerContext().getLocalServer();
		unsubscribe(server, name);
	}

	@Override
	public void unsubscribe(Server server, String name) {
		MapModel option = new MapModel(4);
		option.setMapModel(EventProcessorSubscriberRemoteService.PARAM_SUBSCRIBE_SERVER, server);
		option.setString(EventProcessorSubscriberRemoteService.PARAM_EVENT_PROCESSOR_NAME, name);

		try {
			reloadService.remove(option);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
