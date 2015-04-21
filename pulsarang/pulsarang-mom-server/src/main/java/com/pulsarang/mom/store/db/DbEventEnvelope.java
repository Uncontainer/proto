package com.pulsarang.mom.store.db;

import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.infra.mom.processor.EventProcessorInfo;
import com.pulsarang.mom.store.EventEnvelope;
import com.pulsarang.mom.store.EventStore;

public class DbEventEnvelope implements EventEnvelope {
	EventStore eventStore;
	Event event;
	long id;
	int status;

	public DbEventEnvelope(EventStore eventStore, Event event, long id, int status) {
		this.eventStore = eventStore;
		this.event = event;
		this.id = id;
		this.status = status;
	}

	public long getId() {
		return id;
	}

	@Override
	public Event getEvent() {
		return event;
	}

	@Override
	public boolean mark() {
		this.status = EventStore.STATUS_PROCESS;
		return eventStore.updateStatus(id, status) != 0;
	}

	@Override
	public void acknowlege() {
		eventStore.delete(id);
		// TODO Auto-generated method stub
	}

	@Override
	public void acknowlege(Exception e) {
		eventStore.delete(id);
		// TODO Auto-generated method stub
	}

	@Override
	public void postpone(long time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postpone(long time, EventProcessorInfo processorInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void expire(EventProcessorInfo processorInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void discard(EventProcessorInfo processorInfo) {
		eventStore.delete(id);
		// TODO Auto-generated method stub

	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub

	}

}
