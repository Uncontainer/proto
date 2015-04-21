package com.yeon.lang.listener;

import com.yeon.lang.ResourceIdentifiable;
import com.yeon.lang.ResourceIdentifier;

/**
 * 
 * @author pulsarang
 */
public class ListenTarget {
	private final ListenScope scope;
	private final ResourceIdentifiable value;

	public static ListenTarget type(String classId) {
		return new ListenTarget(ListenScope.TYPE, classId);
	}

	public static ListenTarget type(ResourceIdentifiable resourceIdentifiable) {
		return new ListenTarget(ListenScope.TYPE, resourceIdentifiable);
	}

	public static ListenTarget instance(String resourceId) {
		return new ListenTarget(ListenScope.INSTANCE, resourceId);
	}

	public static ListenTarget instance(ResourceIdentifiable resourceIdentifiable) {
		return new ListenTarget(ListenScope.INSTANCE, resourceIdentifiable);
	}

	public ListenTarget(ListenScope scope, String value) {
		this(scope, new ResourceIdentifier(value));
	}

	public ListenTarget(ListenScope scope, ResourceIdentifiable value) {
		super();
		if (scope == null) {
			throw new IllegalArgumentException("ListenScope is null.");
		}

		if (value == null) {
			throw new IllegalArgumentException("Null listen target value");
		}

		this.scope = scope;
		this.value = value;
	}

	public ListenScope getScope() {
		return scope;
	}

	public String getValue() {
		return value.getResourceId();
	}

	public ResourceIdentifiable getResourceIdentifiable() {
		return value;
	}

	@Override
	public String toString() {
		return scope + ":" + value;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
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

		return toString().equals(obj.toString());
	}
}
