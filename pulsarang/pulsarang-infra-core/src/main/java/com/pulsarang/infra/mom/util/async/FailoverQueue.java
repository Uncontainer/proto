package com.pulsarang.infra.mom.util.async;

import java.util.List;

import com.pulsarang.core.util.MapModel;

public interface FailoverQueue<T extends MapModel> {
	void put(T model) throws InterruptedException;

	Entry<T> take() throws InterruptedException;

	void clear();

	int size();

	boolean isEmpty();

	List<Entry<T>> elements();

	void close();

	interface Entry<T> {
		int STATUS_WAIT = 1;
		int STATUS_RUNNING = 2;

		T getMessage();

		int getStatus();

		int ack();
	}

}
