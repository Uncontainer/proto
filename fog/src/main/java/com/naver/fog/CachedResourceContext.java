package com.naver.fog;

import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.google.common.cache.CacheBuilder;

public abstract class CachedResourceContext<T extends Resource> extends AbstractResourceContext<T> {
	private final ConcurrentMap<Long, T> cache;

	@Autowired
	private ApplicationContext applicationContext;

	protected CachedResourceContext(ResourceType resourceType, int cacheSize) {
		super(resourceType);
		cache = CacheBuilder.newBuilder()
			.concurrencyLevel(10)
			.maximumSize(cacheSize)
			.<Long, T> build().asMap();
	}

	@Override
	public T get(long id) {
		T resource = cache.get(id);
		if (resource == null) {
			resource = super.get(id);
			if (resource != null) {
				T cachedResource = cache.put(id, resource);
				if (cachedResource != null) {
					resource = cachedResource;
				}
			}
		}

		return resource;
	}

	public void updateCache(T resource) {
		T cachedResource = cache.get(resource.getId());
		if (cachedResource == null) {
			return;
		}

		cachedResource.replaceWith(resource);
	}
}
