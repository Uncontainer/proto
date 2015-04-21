package com.naver.fog.web;

import com.naver.fog.Resource;

public class AutoCompleteItem {
	private String label;
	private String value;
	private String description;

	public AutoCompleteItem() {
	}

	public AutoCompleteItem(String label, String value) {
		super();
		this.label = label;
		this.value = value;
	}

	public AutoCompleteItem(Resource resource) {
		this(resource.getName(), Long.toString(resource.getId()));
		this.description = resource.getDescription();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
