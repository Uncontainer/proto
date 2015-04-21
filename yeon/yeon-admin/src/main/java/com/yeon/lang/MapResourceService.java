package com.yeon.lang;

import com.yeon.lang.impl.AbstractMapResource;

public interface MapResourceService<T extends AbstractMapResource> {
	void add(T resource);

	T get(String resourceId);

	int modify(T resource);

	int remove(String resourceId);

	String getId(ResourceGetCondition condition);
}
