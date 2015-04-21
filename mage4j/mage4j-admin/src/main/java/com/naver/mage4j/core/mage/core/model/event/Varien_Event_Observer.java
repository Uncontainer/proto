package com.naver.mage4j.core.mage.core.model.event;

import java.util.Map;

import com.naver.mage4j.external.varien.Varien_Object;

public class Varien_Event_Observer extends Varien_Object {
	String type;
	String model;
	String method;
	Map<String, Object> args;

	public Varien_Event_Observer() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, Object> getArgs() {
		return args;
	}

	public void setArgs(Map<String, Object> args) {
		this.args = args;
	}

	public String getName() {
		return (String)getData("name");
	}

	public void setName(String name) {
		setData("name", name);
	}

	public String getEventName() {
		return (String)getData("event_name");
	}

	public void setEventName(String eventName) {
		setData("event_name", eventName);
	}

	public Callback getCallback() {
		return (Callback)getData("callback");
	}

	public void setCallback(Callback callback) {
		setData("callback", callback);
	}
}
