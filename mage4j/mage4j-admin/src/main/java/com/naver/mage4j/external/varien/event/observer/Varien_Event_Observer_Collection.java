package com.naver.mage4j.external.varien.event.observer;

import java.util.HashMap;
import java.util.Map;

import com.naver.mage4j.core.mage.core.model.event.Varien_Event_Observer;

public class Varien_Event_Observer_Collection {
	/**
	 * Array of observers
	 */
	private final Map<String, Varien_Event_Observer> _observers;

	public Varien_Event_Observer_Collection() {
		_observers = new HashMap<String, Varien_Event_Observer>();
	}

	public Map<String, Varien_Event_Observer> getAllObservers() {
		return _observers;
	}

	/**
	* Adds an observer to the collection
	*
	* @param Varien_Event_Observer $observer
	* @return Varien_Event_Observer_Collection
	*/
	public void addObserver(Varien_Event_Observer observer) {
		_observers.put(observer.getName(), observer);
	}
}
