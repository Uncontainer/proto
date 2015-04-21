package com.yeon.lang.impl;

import com.yeon.lang.Class;
import com.yeon.lang.*;

import java.util.ArrayList;
import java.util.List;

public class ClassByReflectionBuilder {
	public static Class build(ResourceIdentifiable typeClassId, java.lang.Class<? extends MapResource> javaClass) {
		MapClass clazz = new MapClass();
		List<LocalValue> localNames = new ArrayList<LocalValue>();
		localNames.add(new LocalValue("en", NameManager.normalizeClassName(javaClass.getSimpleName())));
		if (typeClassId instanceof NamedResourceIdentifiable) {
			localNames.add(((NamedResourceIdentifiable) typeClassId).getLocalName());
		}

		List<? extends Property> properties = new ArrayList<Property>();
		// TODO 명시적으로 annotation이 선언된 method, field들 추가.

		// TODO javaType property 추가.

		clazz.setNames(localNames);
		clazz.setProperties(properties);

		return clazz;
	}
}
