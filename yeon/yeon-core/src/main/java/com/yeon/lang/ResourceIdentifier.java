package com.yeon.lang;

import java.io.Serializable;

public class ResourceIdentifier implements ResourceIdentifiable, Serializable {
	private static final long serialVersionUID = -8524468254610134372L;

	private String resourceId;

	public ResourceIdentifier(String resourceId) {
		super();
		if (resourceId == null) {
			throw new IllegalArgumentException("Null resourceId");
		}
		this.resourceId = resourceId;
	}

	@Override
	public String getResourceId() {
		return resourceId;
	}

	@Override
	public int hashCode() {
		return resourceId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		return this.resourceId.equals(((ResourceIdentifier) obj).resourceId);
	}
}
