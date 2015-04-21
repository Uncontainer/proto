package com.yeon.lang.property;

import com.yeon.lang.impl.MapProperty;
import com.yeon.lang.resource.ResourceCreateForm;

public class PropertyCreateForm extends ResourceCreateForm {
	private String domainClassId;
	private String rangeClassId;

	public MapProperty getProperty() {
		MapProperty property = new MapProperty();
		property.setDomainClassId(domainClassId);
		property.setRangeClassId(rangeClassId);

		// TODO type을 property로 고정.

		return property;
	}

	public String getDomainClassId() {
		return domainClassId;
	}

	public void setDomainClassId(String domainClassId) {
		this.domainClassId = domainClassId;
	}

	public String getRangeClassId() {
		return rangeClassId;
	}

	public void setRangeClassId(String rangeClassId) {
		this.rangeClassId = rangeClassId;
	}
}
