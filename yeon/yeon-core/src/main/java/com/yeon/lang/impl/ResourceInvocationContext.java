package com.yeon.lang.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ResourceInvocationContext {
	private static final AtomicLong seed = new AtomicLong();

	private static final ThreadLocal<ResourceInvocationContext> HOLDER = new ThreadLocal<ResourceInvocationContext>();
	private static final Map<Long, ResourceInvocationContext> contextMap = new ConcurrentHashMap<Long, ResourceInvocationContext>();

	public static ResourceInvocationContext get() {
		return HOLDER.get();
	}

	public static ResourceInvocationContext get(long id) {
		return contextMap.get(id);
	}

	public static ResourceInvocationContext init() {
		ResourceInvocationContext context = new ResourceInvocationContext();
		HOLDER.set(context);
		return context;
	}

	public static void clear() {
		ResourceInvocationContext context = HOLDER.get();
		if (context == null) {
			return;
		}

		HOLDER.remove();
		contextMap.remove(context.id);
	}

	private long id;
	private Thread caller;
	private long parterId;

	private ResourceInvocationContext() {
		this.id = seed.incrementAndGet();
		caller = Thread.currentThread();
		contextMap.put(id, this);
	}
}
