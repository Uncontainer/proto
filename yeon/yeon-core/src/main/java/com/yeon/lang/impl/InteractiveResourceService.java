package com.yeon.lang.impl;

import com.yeon.lang.Resource;
import com.yeon.lang.ResourceGetCondition;
import com.yeon.lang.ResourceService;
import com.yeon.lang.query.ResultSet;

public class InteractiveResourceService implements ResourceService {
	private final ResourceService api;

	public InteractiveResourceService(ResourceService api) {
		this.api = api;
	}

	@Override
	public void add(Resource resource) {
		api.add(resource);
	}

	@Override
	public void modify(Resource resource) {
		api.modify(resource);
	}

	@Override
	public void remove(String resourceId) {
		api.remove(resourceId);
	}

	@Override
	public Resource get(String resourceId) {
		return api.get(resourceId);
	}

	@Override
	public String getId(ResourceGetCondition condition) {
		return api.getId(condition);
	}

	@Override
	public ResultSet select(String query) {
		return api.select(query);
	}
}
