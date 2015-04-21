package com.pulsarang.infra.mom.processor;

import com.pulsarang.infra.server.Server;

public interface EventProcessorSubscriber {
	void subscribe(Server server, EventProcessorInfo eventProcessorInfo);

	void unsubscribe(Server server, String name);
}
