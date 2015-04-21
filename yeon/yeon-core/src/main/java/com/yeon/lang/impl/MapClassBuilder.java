package com.yeon.lang.impl;

import com.yeon.lang.LocalValue;
import com.yeon.lang.NamedClassId;
import com.yeon.lang.Property;
import com.yeon.lang.ResourceList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapClassBuilder {
	protected String id;
	protected List<LocalValue> names;
	protected List<LocalValue> descriptions;
	protected Map<String, Object> attributes;
	protected List<String> prototypeIds;

	protected MapClassBuilder() {
	}

	public static MapClassBuilder aClass() {
		return new MapClassBuilder();
	}

	public MapClassBuilder id(String id) {
		this.id = id;
		return this;
	}

	public MapClassBuilder alias(String alias) {
		return name(NamedClassId.LOCALE, alias);
	}

	public MapClassBuilder name(LocalValue localNames) {
		return name(localNames.getLocale(), localNames.getValue());
	}

	public MapClassBuilder name(String locale, String value) {
		if (names == null) {
			names = new ArrayList<LocalValue>();
		}

		names.add(new LocalValue(locale, value));

		return this;
	}

	public MapClassBuilder description(String locale, String value) {
		if (descriptions == null) {
			descriptions = new ArrayList<LocalValue>();
		}

		descriptions.add(new LocalValue(locale, value));

		return this;
	}

	public MapClassBuilder attr(Property property, Object value) {
		attributes.put(property.getId(), value);

		return this;
	}

	public MapClassBuilder prototype(String prototypeId) {
		if (prototypeIds == null) {
			prototypeIds = new ArrayList<String>(2);
		}

		prototypeIds.add(prototypeId);

		return this;
	}

	public MapClass build() {
		MapClass resource = new MapClass();
		resource.setValues(attributes);
		if (id != null) {
			resource.setId(id);
		}
		if (names != null) {
			resource.setNames(names);
		}
		if (descriptions != null) {
			resource.setDescriptions(descriptions);
		}

		if (prototypeIds != null) {
			resource.setPrototypes(new ResourceList(prototypeIds));
		}

		return resource;
	}
}
