package com.naver.mage4j.core.mage.core.model.event;

import java.util.Map;

import com.naver.mage4j.external.varien.Varien_Object;
import com.naver.mage4j.external.varien.event.observer.Varien_Event_Observer_Collection;

public class Varien_Event extends Varien_Object {
	private final Varien_Event_Observer_Collection _observers;

	public Varien_Event(Map<String, Object> args) {
		super(args);
		_observers = new Varien_Event_Observer_Collection();
	}

	public String getName() {
		return (String)_data.get("name");
	}

	public void setName(String name) {
		_data.put("name", name);
	}

	public Varien_Event_Observer_Collection getObservers() {
		return _observers;
	}

	public void addObserver(Varien_Event_Observer observer) {
		_observers.addObserver(observer);
	}
}
