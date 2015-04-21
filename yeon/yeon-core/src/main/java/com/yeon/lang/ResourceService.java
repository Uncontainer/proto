package com.yeon.lang;

import com.yeon.lang.query.ResultSet;

public interface ResourceService {
	String NAME = "_RESOURCE_API";

	void add(Resource resource);

	void modify(Resource resource);

	void remove(String resourceId);

	Resource get(String resourceId);
	
	String getId(ResourceGetCondition condition);

	ResultSet select(String query);
}
