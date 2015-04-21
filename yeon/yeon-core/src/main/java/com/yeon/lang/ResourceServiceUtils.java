package com.yeon.lang;

import com.yeon.YeonContext;
import com.yeon.lang.impl.MapResource;

public class ResourceServiceUtils {
	public static Resource getResource(String resourceId) {
		ResourceService resourceService = YeonContext.getResourceContext().getResourceService();

		return resourceService.get(resourceId);
	}

	public static Resource getResourceSafely(String resourceId) {
		Resource resource = getResource(resourceId);
		if (resource != null) {
			return resource;
		}

		throw new ResourceNotFoundException(resourceId);
	}

	public static <T extends Resource> T getResourceSafely(String resourceId, java.lang.Class<T> clazz) {
		Resource resource = getResourceSafely(resourceId);

		return convert(resource, clazz);
	}

	public static <T extends Resource> T getResourceSafely(ResourceIdentifiable resourceIdentifiable, java.lang.Class<T> clazz) {
		return getResourceSafely(resourceIdentifiable.getResourceId(), clazz);
	}

	public static String getId(ResourceGetCondition condition) {
		return YeonContext.getResourceContext().getResourceService().getId(condition);
	}

	public static Class getClass(String classId) {
		Resource resource = getResource(classId);
		if (resource instanceof Class) {
			return (Class) resource;
		} else {
			return null;
		}
	}

	public static Class getClassSafely(String classId) {
		Class clazz = getClass(classId);
		if (clazz != null) {
			return clazz;
		}

		throw new ResourceNotFoundException(classId);
	}

	public static <T extends Resource> T convert(Resource resource, java.lang.Class<T> clazz) {
		if (resource instanceof MapResource) {
			return (T) MapResource.convert((MapResource) resource, (java.lang.Class<MapResource>) clazz);
		}

		throw new UnsupportedOperationException("Unsupported resource class.(" + clazz + ")");
	}
}
