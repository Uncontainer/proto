package com.naver.mage4j.core.mage.core.model.resource.url;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.model.event.EventDispatcher;

public class AbstractContext {
	/**
	 * Prefix of model events names
	 */
	protected String _eventPrefix = "core_abstract";

	/**
	 * Parameter name in event
	 *
	 * In observe method you can use $observer->getEvent()->getObject() in this case
	 */
	protected String _eventObject = "object";

	/**
	 * Processing object before load data
	 */
	protected void _beforeLoad(Object id, String field) {
		if (id == null) {
			throw new IllegalArgumentException();
		}

		EventDispatcher eventDispatcher = AppContext.getCurrent().getEventDispatcher();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("object", this); // TODO object는 this는 안됨.
		params.put("field", field);
		params.put("value", id);
		eventDispatcher.dispatchEvent("model_load_before", params);

		params = new HashMap<String, Object>();
		params.putAll(_getEventData(null));
		eventDispatcher.dispatchEvent(_eventPrefix + "_load_before", params);
	}

	/**
	 * Processing object after load data
	 */
	protected void _afterLoad(Object target) {
		if (target == null) {
			throw new IllegalArgumentException();
		}

		EventDispatcher eventDispatcher = AppContext.getCurrent().getEventDispatcher();
		eventDispatcher.dispatchEvent("model_load_after", Collections.singletonMap("object", target));
		eventDispatcher.dispatchEvent(_eventPrefix + "_load_after", _getEventData(target));
	}

	/**
	* Get array of objects transfered to default events processing
	*/
	protected Map<String, Object> _getEventData(Object target) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data_object", target);
		result.put(_eventObject, this);

		return result;
	}
}
