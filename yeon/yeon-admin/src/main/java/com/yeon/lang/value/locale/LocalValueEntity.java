package com.yeon.lang.value.locale;

import com.yeon.lang.ResourceType;

import java.util.Date;

public class LocalValueEntity {
	private ResourceType resourceType;
	private String resourceId;
	private Long id;
	private String locale;
	private String value;
	private Date createDate;

	public LocalValueEntity() {
		super();
	}

	public LocalValueEntity(ResourceType resourceType, String resourceId, Long id, String locale, String value) {
		super();
		this.resourceType = resourceType;
		this.resourceId = resourceId;
		this.id = id;
		this.locale = locale;
		this.value = value;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
