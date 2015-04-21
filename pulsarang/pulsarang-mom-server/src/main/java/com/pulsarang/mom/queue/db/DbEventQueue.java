package com.pulsarang.mom.queue.db;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.mom.queue.EventQueue;
import com.pulsarang.mom.store.EventEnvelope;
import com.pulsarang.mom.store.EventStore;

public class DbEventQueue implements EventQueue {
	final String name;

	/** Main lock guarding all access */
	private final ReentrantLock takeLock = new ReentrantLock();
	/** Lock held by put, offer, etc */
	private final ReentrantLock putLock = new ReentrantLock();
	/** Condition for waiting takes */
	private final Condition notEmpty = takeLock.newCondition();

	private final AtomicInteger count;

	private final EventStore eventStore;

	protected DbEventQueue(String name, EventStore eventStore) {
		this.name = name;
		this.eventStore = eventStore;
		// TODO size 계산 로직 수정.
		this.count = new AtomicInteger(eventStore.count(EventStore.STATUS_WAIT));
	}

	@Override
	public void push(Event event) throws InterruptedException {
		if (event == null) {
			throw new IllegalArgumentException("Null event.");
		}

		int c;
		final ReentrantLock putLock = this.putLock;
		final AtomicInteger count = this.count;
		putLock.lockInterruptibly();
		try {
			eventStore.insert(event);
			c = count.getAndIncrement();
		} finally {
			putLock.unlock();
		}

		if (c == 0) {
			final ReentrantLock takeLock = this.takeLock;
			takeLock.lock();
			try {
				notEmpty.signal();
			} finally {
				takeLock.unlock();
			}
		}
	}

	@Override
	public EventEnvelope pop() throws InterruptedException {
		EventEnvelope eventEnvelope = null;
		final AtomicInteger count = this.count;
		final ReentrantLock takeLock = this.takeLock;
		takeLock.lockInterruptibly();
		try {
			try {
				while (count.get() == 0) {
					notEmpty.await();
				}
			} catch (InterruptedException ie) {
				notEmpty.signal(); // propagate to a non-interrupted thread
				throw ie;
			}

			while (true) {
				List<EventEnvelope> eventEnvelopes = eventStore.extracts(EventStore.STATUS_WAIT, 1);
				if (eventEnvelopes.isEmpty()) {
					break;
				}

				eventEnvelope = eventEnvelopes.get(0);
				if (eventEnvelope.mark()) {
					break;
				} else {
					eventEnvelope = null;
				}
			}

			int c = -1;
			if (eventEnvelope != null) {
				c = count.getAndDecrement();
			} else {
				// TODO Count 동기화 로직 필요할 수 있음.
			}

			if (c > 1) {
				notEmpty.signal();
			}
		} finally {
			takeLock.unlock();
		}

		return eventEnvelope;
	}

	@Override
	public synchronized EventEnvelope popRunningEvent() {
		List<EventEnvelope> eventEnvelopes = eventStore.extracts(EventStore.STATUS_PROCESS, 1);
		if (eventEnvelopes.isEmpty()) {
			return null;
		}

		return eventEnvelopes.get(0);
	}

	@Override
	public void close() {
		eventStore.close();
	}

	public void clear() {
		putLock.lock();
		takeLock.lock();
		try {
			eventStore.deleteAll();
		} finally {
			takeLock.unlock();
			putLock.unlock();
		}
	}

	public int size() {
		return count.get();
	}

	public boolean isEmpty() {
		return size() == 0;
	}
}
