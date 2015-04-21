package com.naver.fog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class AbstractResourceContext<T extends Resource> implements InitializingBean {
	protected final ResourceType resourceType;

	protected AbstractResourceService<T> resourceService;

	@Autowired
	private ApplicationContext applicationContext;

	protected AbstractResourceContext(ResourceType resourceType) {
		super();
		this.resourceType = resourceType;
	}

	public void afterPropertiesSet() throws Exception {
		if (resourceService == null) {
			throw new IllegalStateException("resourceService is null.");
		}
	}

	public boolean exists(long id) {
		return get(id) != null;
	}

	public T get(long id) {
		return resourceService.getById(id);
	}

	public T getSafely(long id) {
		T resource = get(id);
		if (resource == null) {
			throw new ResourceNotFoundException(resourceType, id);
		}

		return resource;
	}

	public List<T> list(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyList();
		}

		List<T> fields = new ArrayList<T>(ids.size());
		for (long id : ids) {
			fields.add(getSafely(id));
		}

		return fields;
	}

	public List<T> listByNamePrefix(String namePrefix, int count) {
		List<Long> fieldIds = resourceService.listIdsByNamePrefix(namePrefix, count);
		return list(fieldIds);
	}
}
