package com.naver.fog;

import com.naver.fog.web.FogHandledException;

public class ResourceNotFoundException extends FogHandledException {
	private static final long serialVersionUID = 4578027977795474302L;

	private final ResourceType resourceType;

	public ResourceNotFoundException(ResourceType resourceType, long resourceId) {
		this(resourceType, resourceId, HandleType.ALERT_AND_BACK);
	}

	public ResourceNotFoundException(ResourceType resourceType, long resourceId, HandleType handleType) {
		super(resourceId, null, handleType, null, "Fail to find " + resourceType.name().toLowerCase() + ".(" + resourceId + ")");
		this.resourceType = resourceType;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}
}
