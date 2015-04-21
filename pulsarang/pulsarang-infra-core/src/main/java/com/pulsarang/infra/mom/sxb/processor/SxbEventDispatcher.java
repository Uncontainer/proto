package com.pulsarang.infra.mom.sxb.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.infra.InfraContext;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.InfraEnvironment;
import com.pulsarang.infra.mom.EventProcessor;
import com.pulsarang.infra.mom.sxb.SxbDestinationUtils;
import com.pulsarang.infra.server.Solution;
import com.pulsarang.infra.util.NameUtils;

public class SxbEventDispatcher {
	private final Logger log = LoggerFactory.getLogger(SxbEventDispatcher.class);

	Map<String, EventProcessorExecutor> executors = new HashMap<String, EventProcessorExecutor>();

	public static final String NAME = "EventReceiver";

	private InfraEnvironment environment;

	private boolean forceSubscribeEvent;

	public SxbEventDispatcher(InfraEnvironment environment, boolean forceSubscribeEvent) {
		this.environment = environment;
		this.forceSubscribeEvent = forceSubscribeEvent;
	}

	public void destroy() {
		// TODO 리소스 정리 코드 추가.
		// for (MomEventProcessorSubscriber subscriber : pool.getSubscribers()) {
		// subscriber.close();
		// }
	}

	public void addEventProcessor(EventProcessor eventProcessor) {
		if (eventProcessor == null) {
			throw new IllegalArgumentException("EventProcessor is null.");
		}

		if (NameUtils.isInvalidJavaIdentifier(eventProcessor.getName())) {
			throw new IllegalArgumentException("EventProcessor's name must be a java identifier.(" + eventProcessor.getClass().getCanonicalName()
					+ ")");
		}

		if (CollectionUtils.isEmpty(eventProcessor.getSubscribeEventTypes())) {
			throw new IllegalArgumentException("EventProcessor must subscribe more than one event required.(" + eventProcessor.getName() + ")");
		}

		String canonicalEventProcessorName = SxbDestinationUtils.getCanonicalEventProcessorName(eventProcessor.getName());
		EventProcessorExecutor eventProcessorExecutor = executors.get(canonicalEventProcessorName);
		if (eventProcessorExecutor != null) {
			throw new IllegalArgumentException("EventProcessor has alreay added.(" + eventProcessor.getName() + ")");
		}

		EventProcessorExecutor executor = createEventProcessorExecutor(eventProcessor);
		executors.put(canonicalEventProcessorName, executor);

		// TODO 구독 코드 추가.
		// List<? extends EventType> subscribeEventTypes = eventProcessor.getSubscribeEvents();
		// if (CollectionUtils.isEmpty(subscribeEventTypes)) {
		// log.warn("[MOM] Subscribe event has not specified. ({})", simpleName);
		// return;
		// }
		//
		// MomServerApi momServerApi = MomServerApi.getInstance();
		//
		// momServerApi.subscribe(canonicalName, subscribeEventTypes);
		// log.info("[MOM] '{}' connecects to mom-server...({})", canonicalName, subscribeEventTypes);
	}

	private EventProcessorExecutor createEventProcessorExecutor(EventProcessor eventProcessor) {
		EventProcessorExecutor eventDispatcher;
		InfraContext infraContext = InfraContextFactory.getInfraContext();

		if (environment == InfraEnvironment.TEST || Solution.SOLUTION_DUMMY.equals(infraContext.getSolutionName())) {
			eventDispatcher = new NullEventProcessorExecutor(eventProcessor);
		} else if (!forceSubscribeEvent && (environment == InfraEnvironment.LOCAL || environment == InfraEnvironment.CI)) {
			eventDispatcher = new LocalEventProcessorExecutor(eventProcessor);
		} else {
			eventDispatcher = new BasicEventProcessorExecutor(eventProcessor);
		}

		return eventDispatcher;
	}

	public void pauseEventProcessor(EventProcessor eventProcessor) {
//		MomEventProcessorSubscriber eventProcessorSubscriber = pool.getByName(eventProcessor.getName());
//		if (eventProcessorSubscriber == null) {
//			// TODO 로깅 추가.
//			return;
//		}

		log.info("[MOM] '{}' disconnecects to mom-server...", eventProcessor.getName());
		// TODO unsubscribe 코드 추가.
		// momServerApi.unsubscribe(canonicalName);
		// eventProcessorSubscriber.close();
	}

	public EventProcessor get(String eventProcessorName) {
		String canonicalEventProcessorName = SxbDestinationUtils.getCanonicalEventProcessorName(eventProcessorName);
		EventProcessorExecutor eventProcessorExecutor = executors.get(canonicalEventProcessorName);
		if (eventProcessorExecutor == null) {
			return null;
		}

		return eventProcessorExecutor.getEventProcessor();
	}

	public List<EventProcessor> list() {
		List<EventProcessor> eventProcessors = new ArrayList<EventProcessor>(executors.size());
		for (EventProcessorExecutor eventProcessorExecutor : executors.values()) {
			eventProcessors.add(eventProcessorExecutor.getEventProcessor());
		}

		return eventProcessors;
	}

	public EventProcessResponse onEvent(EventProcessRequest request) {
		EventProcessorExecutor eventProcessorExecutor = executors.get(request.getProcessorName());
		if (eventProcessorExecutor == null) {
			return new EventProcessResponse(EventProcessResponse.RC_NO_PROCESSOR);
		}

		return eventProcessorExecutor.execute(request);
	}
}
