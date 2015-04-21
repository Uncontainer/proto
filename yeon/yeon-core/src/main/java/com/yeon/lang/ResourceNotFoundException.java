package com.yeon.lang;

/**
 * Resource를 찾지 못할 때 발생하는 예외
 * 
 * @author ghost
 * 
 */
public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -577472203894857072L;

	private final String resourceId;

	public ResourceNotFoundException(String resourceId) {
		super("Fail to find resource '" + resourceId + "'");

		this.resourceId = resourceId;
	}

	public String getResourceId() {
		return resourceId;
	}
}
