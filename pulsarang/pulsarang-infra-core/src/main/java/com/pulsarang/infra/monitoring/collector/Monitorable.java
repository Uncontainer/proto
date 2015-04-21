package com.pulsarang.infra.monitoring.collector;

public interface Monitorable<T> {
	T createShapshot();
}
