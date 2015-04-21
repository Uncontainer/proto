package com.pulsarang.infra.mom.util;

import java.util.List;

public interface Subscribable<T> {
	String getName();

	List<? extends T> getSubscribeTargets();
}
