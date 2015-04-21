package com.pulsarang.infra.remote;

public interface ResponseIterator<T> {
	void next(T item);
}
