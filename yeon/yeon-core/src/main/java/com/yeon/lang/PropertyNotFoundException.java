package com.yeon.lang;

public class PropertyNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -6703187393395767227L;

	private Class clazz;
	private String propertyId;
	private LocalValue name;

	public PropertyNotFoundException(Class clazz, String propertyId) {
		this.clazz = clazz;
		this.propertyId = propertyId;
	}

	public PropertyNotFoundException(Class clazz, LocalValue name) {
		this.clazz = clazz;
		this.name = name;
	}
}
