package com.naver.mage4j.core.mage.core.model.event;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Config;
import com.naver.mage4j.core.mage.core.model.ModelInstanceLoader;
import com.naver.mage4j.core.mage.core.model.ModelLoader;
import com.naver.mage4j.core.mage.core.model.app.Mage_Core_Model_App_Area.AreaType;
import com.naver.mage4j.external.varien.Varien_Profiler;
import com.naver.mage4j.external.varien.event.Varien_Event_Collection;
import com.naver.mage4j.external.varien.event.observer.Varien_Event_Observer_Collection;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;

@Component
public class EventDispatcher {
	@Autowired
	private Mage_Core_Model_Config modelConfig;

	@Autowired
	private ModelInstanceLoader modelInstanceLoader;

	@Autowired
	private ModelLoader modelLoader;

	private Varien_Event_Collection _eventCollection = new Varien_Event_Collection();

	/**
	 * Events cache
	 *
	 * @var array
	 */
	protected Map<AreaType, Map<String, Varien_Event>> _events = new EnumMap<AreaType, Map<String, Varien_Event>>(AreaType.class);

	static class EventList {
		String name;
		Map<String, Object> events;
	}

	public static final Varien_Event NULL_EVENT = new Varien_Event(null);

	public EventDispatcher addEventArea(AreaType area) {
		if (!_events.containsKey(area)) {
			_events.put(area, new HashMap<String, Varien_Event>());
		}

		return this;
	}

	public void dispatchEvent(String name, Map<String, Object> args) {
		Varien_Profiler.start("DISPATCH EVENT:" + name);
		try {
			_dispatchEvent(name, args);
		} finally {
			Varien_Profiler.stop("DISPATCH EVENT:" + name);
		}
	}

	/**
	 * Mage_Core_Model_App#dispatchEvent
	 * 
	 * @param eventName
	 * @param args
	 */
	public void _dispatchEvent(String eventName, Map<String, Object> args) {
		for (Map.Entry<AreaType, Map<String, Varien_Event>> eachArea : _events.entrySet()) {
			AreaType area = eachArea.getKey();
			Map<String, Varien_Event> events = eachArea.getValue();
			if (!events.containsKey(eventName)) {
				Simplexml_Element eventConfig = getEventConfig(area, eventName);
				if (eventConfig.isNull()) {
					events.put(eventName, NULL_EVENT);
					continue;
				}

				Varien_Event event = events.get(eventName);
				if (event == null) {
					event = new Varien_Event(null);
					event.setName(eventName);
					events.put(eventName, event);
				}

				Varien_Event_Observer_Collection observers = event.getObservers();
				for (Simplexml_Element obsConfig : eventConfig.selectSingle("observers").children()) {
					Varien_Event_Observer observer = new Varien_Event_Observer();
					observer.type = obsConfig.getString("type");
					observer.model = obsConfig.getString("class");
					if (observer.model == null) {
						observer.model = modelInstanceLoader.getClassName(obsConfig);
					}
					observer.method = obsConfig.getString("method");
					observer.args = obsConfig.selectSingle("args").toMap();

					observers.addObserver(observer);
				}

			}

			Varien_Event event = events.get(eventName);
			if (event == NULL_EVENT) {
				continue;
			}

			event = new Varien_Event(new HashMap<String, Object>(args));
			event.setName(eventName);
			Varien_Event_Observer observer = new Varien_Event_Observer();

			for (Entry<String, Varien_Event_Observer> eachObserver : event.getObservers().getAllObservers().entrySet()) {
				String obsName = eachObserver.getKey();
				Varien_Event_Observer obs = eachObserver.getValue();

				observer.setData("event", event);
				Varien_Profiler.start("OBSERVER: " + obsName);
				if ("disabled".equals(obs.getType())) {
					// do nothing
				} else {
					String method = obs.getMethod();
					observer.addData(args);
					Object object;
					if ("object".equals(obs.getType()) || "model".equals(obs.getType())) {
						object = modelLoader.getModel(obs.getModel(), null);
					} else {
						object = modelLoader.getSingleton(obs.getModel(), null);
					}
					_callObserverMethod(object, method, observer);

				}
				Varien_Profiler.stop("OBSERVER: " + obsName);
			}
		}
	}

	private void _callObserverMethod(Object object, String method, Varien_Event_Observer observer) {
		try {
			object.getClass().getMethod(method, Varien_Event_Observer.class).invoke(object, observer);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Mage_Core_Model_Config#loadEventObservers
	 *
	 * @param   string $area
	 * @return  boolean
	 */
	public boolean loadEventObservers(AreaType area) {
		Simplexml_Element events = modelConfig.getNode(area.name().toLowerCase() + "/events");
		if (events == null) {
			return false;
		}
		for (Simplexml_Element event : events.children()) {
			String eventName = event.getString("name");
			for (Simplexml_Element observer : event.selectSingle("observers").children()) {
				String type = observer.getString("type");

				Object object;
				if ("singleton".equals(type)) {
					object = modelLoader.getSingleton(observer.getString("class"), null);
				} else if ("object".equals(type) || "model".equals(type)) {
					object = modelLoader.getModel(observer.getString("class"), null);
				} else {
					// TODO 클래스 객체화 로직 추가.
					object = modelInstanceLoader.getClassName(observer);
				}
				Callback callback = new Callback(object, observer.getString("method"));

				Map<String, Object> args = observer.selectSingle("args").toMap();
				String observerClass = observer.getString("observer_class");
				if (observerClass == null) {
					observerClass = "";
				}

				addObserver(eventName, callback, args, observer.getName(), observerClass);
			}
		}

		return true;
	}

	/**
	 * Mage#addObserver
	 */
	private void addObserver(String eventName, Callback callback, Map<String, Object> data, String name, String observerClass) {
		if (StringUtils.isEmpty(observerClass)) {
			observerClass = "Varien_Event_Observer";
		}

		// TODO [REFLECTION] observerClass 내용 확인 이후 처리 코드 추가.
		Varien_Event_Observer observer = new Varien_Event_Observer()/*$observerClass()*/;
		observer.setName(name);
		observer.addData(data);
		observer.setEventName(eventName);
		observer.setCallback(callback);
		_eventCollection.addObserver(observer);
	}

	/**
	 * Configuration for events by area
	 *
	 * @var array
	 */
	private Map<AreaType, Simplexml_Element> _eventAreas = new EnumMap<AreaType, Simplexml_Element>(AreaType.class);

	/**
	 * Get events configuration
	*
	* @param   string $area event area
	* @param   string $eventName event name
	* @return  Mage_Core_Model_Config_Element
	*/
	private Simplexml_Element getEventConfig(AreaType area, String eventName) {
		Simplexml_Element areaConfig = _eventAreas.get(area);
		if (areaConfig == null) {
			areaConfig = modelConfig.getNode(area.getCode()).selectSingle("events");
			_eventAreas.put(area, areaConfig);
		}

		return areaConfig.selectSingle(eventName);
	}
}
