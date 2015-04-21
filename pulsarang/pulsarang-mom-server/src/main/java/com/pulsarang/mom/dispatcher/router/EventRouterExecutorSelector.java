package com.pulsarang.mom.dispatcher.router;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.pulsarang.infra.mom.event.Event;

@Component()
@Scope("prototype")
public class EventRouterExecutorSelector {
	@Autowired
	private ApplicationContext applicationContext;

	private int bufferSize = 11;
	private EventRouterExecutor[] eventRouterExecutors;

	public void init() {
		// TODO ApplicationContext dependency 제거...
		eventRouterExecutors = new EventRouterExecutor[bufferSize];
		for (int i = 0; i < bufferSize; i++) {
			eventRouterExecutors[i] = applicationContext.getBean(EventRouterExecutor.class);
		}
	}

	public void destory() {
	}

	public EventRouterExecutor select(Event event) {
		long channelNo = event.getChannelNo();

		// TODO bucket, table의 분산 로직과 겹친다.
		int index = (int) (channelNo % bufferSize);
		return eventRouterExecutors[index];
	}
}
