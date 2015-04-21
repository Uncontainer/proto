package com.naver.mage4j.external.varien.event;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.naver.mage4j.core.mage.core.model.event.Varien_Event;
import com.naver.mage4j.core.mage.core.model.event.Varien_Event_Observer;
import com.naver.mage4j.external.varien.event.observer.Varien_Event_Observer_Collection;

public class Varien_Event_Collection {
	/**
	 * Array of events in the collection
	 *
	 * @var array
	 */
	private Map<String, Varien_Event> _events;

	/**
	 * Global observers
	 * 
	 * For example regex observers will watch all events that 
	 */
	private Varien_Event_Observer_Collection _observers;

	private Varien_Event_Observer_Collection _globalObservers;

	/**
	 * Initializes global observers collection
	 * 
	 */
	public Varien_Event_Collection() {
		this._events = new HashMap<String, Varien_Event>();
		this._globalObservers = new Varien_Event_Observer_Collection();
	}

	/**
	* Register an observer
	* 
	* If observer has event_name property it will be regitered for this specific event.
	* If not it will be registered as global observer
	*/
	public void addObserver(Varien_Event_Observer observer) {
		String eventName = observer.getEventName();
		if (eventName != null) {
			getEventByName(eventName).addObserver(observer);
		} else {
			getGlobalObservers().addObserver(observer);
		}
	}

	/**
	 * Returns all registered global observers for the collection of events
	 *
	 * @return Varien_Event_Observer_Collection
	 */
	public Varien_Event_Observer_Collection getGlobalObservers() {
		return _globalObservers;
	}

	/**
	 * Returns event by its name
	 *
	 * If event doesn't exist creates new one and returns it
	 * 
	 * @param string $eventName
	 * @return Varien_Event
	 */
	public Varien_Event getEventByName(String eventName) {
		Varien_Event event = _events.get(eventName);
		if (event == null) {
			event = new Varien_Event(Collections.singletonMap("name", (Object)eventName));
			addEvent(event);
		}

		return event;
	}

	public void addEvent(Varien_Event event) {
		_events.put(event.getName(), event);
	}
}
