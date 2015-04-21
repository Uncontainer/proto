package com.yeon.lang.resource;

import com.yeon.lang.ResourceType;

public class ResourceSearchCriteria {
	private ResourceSearchType searchType = ResourceSearchType.NAME;
	private ResourceType resourceType;
	private String keyword;

	public ResourceSearchType getSearchType() {
		return searchType;
	}

	public void setSearchType(ResourceSearchType type) {
		this.searchType = type;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
