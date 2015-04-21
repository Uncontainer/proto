package com.yeon.lang.triple;

public class Triple {
	private long id;

	private String subject;
	private String propertyId;
	private String object;
	private boolean literalObject;

	public Triple() {
		super();
	}

	public Triple(String subject, String propertyId, String object) {
		super();
		this.subject = subject;
		this.propertyId = propertyId;
		this.object = object;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

}
