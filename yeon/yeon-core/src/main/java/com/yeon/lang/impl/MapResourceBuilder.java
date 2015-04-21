package com.yeon.lang.impl;

import com.yeon.lang.LocalValue;
import com.yeon.lang.Property;
import com.yeon.lang.ResourceIdentifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapResourceBuilder {
	protected String id;
	protected ResourceIdentifiable classId;
	protected List<LocalValue> names;
	protected List<LocalValue> descriptions;
	protected Map<String, Object> attributes;

	protected MapResourceBuilder(ResourceIdentifiable classId) {
		this.classId = classId;
	}

	public static MapResourceBuilder aResource(ResourceIdentifiable classId) {
		return new MapResourceBuilder(classId);
	}

	public MapResourceBuilder id(String id) {
		this.id = id;
		return this;
	}

	public MapResourceBuilder name(String locale, String value) {
		if (names == null) {
			names = new ArrayList<LocalValue>();
		}

		names.add(new LocalValue(locale, value));

		return this;
	}

	public MapResourceBuilder description(String locale, String value) {
		if (descriptions == null) {
			descriptions = new ArrayList<LocalValue>();
		}

		descriptions.add(new LocalValue(locale, value));

		return this;
	}

	public MapResourceBuilder attr(Property property, Object value) {
		attributes.put(property.getId(), value);

		return this;
	}

	public MapResource build() {
		MapResource resource = new MapResource(classId, attributes);
		if (id != null) {
			resource.setId(id);
		}
		if (names != null) {
			resource.setNames(names);
		}
		if (descriptions != null) {
			resource.setDescriptions(descriptions);
		}

		return resource;
	}
}
