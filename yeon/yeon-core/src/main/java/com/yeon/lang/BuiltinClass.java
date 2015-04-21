package com.yeon.lang;

import com.yeon.lang.impl.MapClass;
import com.yeon.lang.impl.MapClassBuilder;


public enum BuiltinClass {
    CLASS(MapClass.CLASS_RESOURCE_ID),
    TYPE(ResourceType.CLASS.buildId("p2")),
    PROPERTY(ResourceType.CLASS.buildId("p3"));
    final String id;
	final MapClass clazz;

	BuiltinClass(String id) {
		this.id = id;
		clazz = MapClassBuilder.aClass().id(id).name("en", this.name().toLowerCase()).build();
	}

	public String getId() {
		return id;
	}

	public MapClass getTypeClass() {
		return clazz;
	}
}
