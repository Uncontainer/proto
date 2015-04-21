package com.pulsarang.infra.mom.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author pulsarang
 */
public class SubscriberPool<S extends Subscribable<T>, T> {
	private Map<String, S> subscriberByName;
	private Map<T, List<S>> subscriberByTarget;

	public SubscriberPool() {
		subscriberByName = new HashMap<String, S>();
		subscriberByTarget = new HashMap<T, List<S>>();
	}

	public void init(List<? extends S> eventProcessors) {
		Map<String, S> subscriberByName = new HashMap<String, S>();
		Map<T, List<S>> subscribersByTarget = new HashMap<T, List<S>>();

		Iterator<? extends S> iter = eventProcessors.iterator();
		while (iter.hasNext()) {
			S eventProcessor = iter.next();
			add(subscriberByName, subscribersByTarget, eventProcessor);
		}

		this.subscriberByName = subscriberByName;
		this.subscriberByTarget = subscribersByTarget;
	}

	public int size() {
		return subscriberByName.size();
	}

	public Collection<S> getSubscribers() {
		return subscriberByName.values();
	}

	public void add(S eventProcessor) {
		add(subscriberByName, subscriberByTarget, eventProcessor);
	}

	public S getByName(String processorName) {
		return subscriberByName.get(processorName);
	}

	public List<S> getByTarget(T type) {
		return subscriberByTarget.get(type);
	}

	private void add(Map<String, S> subscriberByName, Map<T, List<S>> subscribersByTarget, S eventProcessor) {
		List<? extends T> subscribeEventTargets = eventProcessor.getSubscribeTargets();
		for (T subscribeEventTarget : subscribeEventTargets) {
			List<S> subscribers = subscribersByTarget.get(subscribeEventTarget);
			if (subscribers == null) {
				subscribers = new ArrayList<S>();
				subscribersByTarget.put(subscribeEventTarget, subscribers);
			}

			subscriberByName.put(eventProcessor.getName(), eventProcessor);
			subscribers.add(eventProcessor);
		}
	}
}
