package com.naver.mage4j.core.mage.core.model.event;


public class Callback {
	Object object;
	String method;

	public Callback(Object object, String method) {
		super();
		this.object = object;
		this.method = method;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Object call(Object... args) {
		throw new UnsupportedOperationException();
	}
}
