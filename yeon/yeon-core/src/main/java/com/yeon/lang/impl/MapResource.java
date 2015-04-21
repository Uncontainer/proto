package com.yeon.lang.impl;

import java.util.Map;

import com.yeon.YeonContext;
import com.yeon.lang.Class;
import com.yeon.lang.ResourceIdentifiable;
import com.yeon.lang.ResourceIdentifier;
import com.yeon.lang.ResourceService;

public class MapResource extends AbstractMapResource {
	public static final MapResource NULL = new MapResource();

	private final Class clazz;

	// Constructor for null
	private MapResource() {
		clazz = null;
	}

	public MapResource(ResourceIdentifiable typeClassId) {
		String classId = typeClassId.getResourceId();
		Class c = (classId != null) ? (Class) resourceService.get(classId) : null;

		if (c == null) {
			// TODO session을 이용한 통신으로 server에서 가져갈 수 있도록 수정.
			c = ClassByReflectionBuilder.build(typeClassId, this.getClass());
			resourceService.add(c);
		}

		this.clazz = c;
		data.put(PARAM_CLASS, c.getId());
	}

	public MapResource(String classId) {
		this(classId != null ? new ResourceIdentifier(classId) : ResourceIdentifiable.NULL);
	}

	public MapResource(Class clazz) {
		this.clazz = clazz;
		data.put(PARAM_CLASS, clazz.getId());
	}

	public MapResource(ResourceIdentifiable classId, Map<String, Object> map) {
		this(classId);
		setValues(map);
	}

	public MapResource(MapResource resource) {
		this.clazz = resource.clazz;
		setValues(resource.getValues());
	}

	@Override
	public String getTypeClassId() {
		return (String) data.get(PARAM_CLASS);
	}

	@Override
	public Class getTypeClass() {
		return clazz;
	}
}
