package com.pulsarang.mom.queue.db.h2;

import com.pulsarang.mom.queue.db.DbEventQueue;
import com.pulsarang.mom.store.db.h2.H2EventStore;

public class H2EventQueue extends DbEventQueue {
	public H2EventQueue(String name) {
		super(name, new H2EventStore(name));
	}
}
