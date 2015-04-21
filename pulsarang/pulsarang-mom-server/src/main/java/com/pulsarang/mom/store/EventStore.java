package com.pulsarang.mom.store;

import java.util.List;

import com.pulsarang.infra.mom.event.Event;

public interface EventStore {
	int STATUS_WAIT = 1;
	int STATUS_PROCESS = 2;

	void insert(Event event);

	List<EventEnvelope> extracts(int status, int size);

	int updateStatus(long id, int status);

	int delete(long id);

	void deleteAll();

	int count(int status);

	void close();
}