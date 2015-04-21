package com.pulsarang.mom;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.pulsarang.infra.InfraContext;
import com.pulsarang.infra.mom.processor.EventProcessorSubscriberRemoteService;
import com.pulsarang.infra.mom.receiver.EventReceiver;
import com.pulsarang.infra.mom.receiver.EventReceiverRemoteService;
import com.pulsarang.mom.receiver.TableEventScannerSelector;

@Component
public class MomServerContext implements InitializingBean, DisposableBean, ApplicationListener<ApplicationEvent> {
	static boolean contextRefreshedEventReceived = false;

	@Autowired
	private ProcessorEventQueueManager processorEventQueueManager;

	@Autowired
	private InfraContext infraConext;

	@Autowired
	private EventReceiver eventReceiver;

	@Autowired
	private TableEventScannerSelector tableSelector;

	@Override
	public void afterPropertiesSet() throws Exception {
		processorEventQueueManager.init();

		tableSelector.init();
	}

	@Override
	public void destroy() throws Exception {
		tableSelector.destroy();

		processorEventQueueManager.destroy();
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			synchronized (MomServerContext.class) {
				if (contextRefreshedEventReceived) {
					throw new IllegalStateException("ContextRefreshedEvent has already received.");
				}

				contextRefreshedEventReceived = true;
			}

			EventReceiverRemoteService eventReceiverRemoteService = new EventReceiverRemoteService(eventReceiver);
			infraConext.getRemoteContext().setRemoteService(eventReceiverRemoteService);

			EventProcessorSubscriberRemoteService eventProcessorSubscriberRemoteService = new EventProcessorSubscriberRemoteService(
					processorEventQueueManager);
			infraConext.getRemoteContext().setRemoteService(eventProcessorSubscriberRemoteService);
		}
	}
}
