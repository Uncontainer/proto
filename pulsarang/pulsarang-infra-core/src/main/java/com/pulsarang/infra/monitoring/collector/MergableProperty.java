package com.pulsarang.infra.monitoring.collector;

public interface MergableProperty<T> {
	String getPropertyName();

	Mergable<T> getMergable();
}
