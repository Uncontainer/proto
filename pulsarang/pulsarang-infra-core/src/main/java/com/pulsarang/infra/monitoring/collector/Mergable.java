package com.pulsarang.infra.monitoring.collector;

public interface Mergable<T> {
	T merge(T source, T target);
}
