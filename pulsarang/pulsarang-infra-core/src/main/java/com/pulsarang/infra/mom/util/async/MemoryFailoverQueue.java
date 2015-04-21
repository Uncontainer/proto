package com.pulsarang.infra.mom.util.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.core.util.MapModel;

public class MemoryFailoverQueue<T extends MapModel> implements FailoverQueue<T> {
	private final Logger log = LoggerFactory.getLogger(MemoryFailoverQueue.class);

	private final BlockingQueue<T> queue = new LinkedBlockingQueue<T>();
	private final int maxSize;

	private final boolean persist;

	MemoryFailoverQueue(String canonicalName, boolean persist, int maxSize) {
		this.persist = persist;
		this.maxSize = maxSize;
	}

	@Override
	public void put(T model) throws InterruptedException {
		if (persist) {
			log.warn("[MOM] Message is stored at memoty storage.");
		}

		if (queue.size() >= maxSize) {
			MapModel oldest = queue.poll();
			log.info("[MOM] Oldest queued item is discarded.({})", oldest);
		}

		queue.put(model);
	}

	@Override
	public Entry<T> take() throws InterruptedException {
		T model = queue.take();
		if (model == null) {
			return null;
		}

		return new SimpleEntry<T>(model);
	}

	@Override
	public void clear() {
		queue.clear();
	}

	@Override
	public int size() {
		return queue.size();
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public List<Entry<T>> elements() {
		Object[] array = queue.toArray();
		List<Entry<T>> list = new ArrayList<Entry<T>>(array.length);
		for (int i = 0; i < array.length; i++) {
			@SuppressWarnings("unchecked")//
			T entry = (T)array[i];
			list.add(new SimpleEntry<T>(entry));
		}

		return list;
	}

	@Override
	public void close() {
		Object[] array = queue.toArray();
		queue.clear();
		for (int i = 0; i < array.length; i++) {
			log.warn("[MOM] Discard message. ({})", array[i]);
		}
	}

	private static class SimpleEntry<T> implements Entry<T> {
		T model;

		SimpleEntry(T model) {
			super();
			this.model = model;
		}

		@Override
		public T getMessage() {
			return model;
		}

		@Override
		public int getStatus() {
			return STATUS_WAIT;
		}

		@Override
		public int ack() {
			return 1;
		}
	}
}
