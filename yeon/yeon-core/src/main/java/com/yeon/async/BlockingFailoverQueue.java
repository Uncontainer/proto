package com.yeon.async;

import com.yeon.api.ApiServiceFactory;
import com.yeon.monitor.alarm.AlarmLog;
import com.yeon.monitor.alarm.AlarmLogType;
import com.yeon.util.MapModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingFailoverQueue<T extends MapModel> extends AbstractFailoverQueue<T> {
	private final Logger log = LoggerFactory.getLogger(BlockingFailoverQueue.class);

	private final BlockingQueue<T> queue = new LinkedBlockingQueue<T>();
	private final int maxSize;

	private final String name;

	BlockingFailoverQueue(String name, int maxSize) {
		this.name = name;
		this.maxSize = maxSize;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public boolean isFull() {
		return queue.size() >= maxSize;
	}

	@Override
	public void put(T model) throws InterruptedException {
		if (isFull()) {
			T oldest = queue.poll();
			log.warn("[YEON] Oldest queued item is discarded.({})", oldest);
			itemRemoved(oldest);

			AlarmLog alarmLog = new AlarmLog(AlarmLogType.ALT_QUEUE_OVERFLOW);
			alarmLog.setSource(name);
			alarmLog.setMessage("Queue size is greater than max.(" + maxSize + ")");
			ApiServiceFactory.getMonitorApiService().logAlarm(alarmLog);
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
	public List<Entry<T>> elements(int maxCount) {
		if (queue.isEmpty()) {
			return Collections.emptyList();
		}

		int count = queue.size() > maxCount ? maxCount : queue.size();
		Object[] array = new Object[count];
		queue.toArray(array);

		List<Entry<T>> list = new ArrayList<Entry<T>>(count);
		for (int i = 0; i < array.length && i < maxCount; i++) {
			@SuppressWarnings("unchecked")//
			T entry = (T)array[i];
			list.add(new SimpleEntry<T>(entry));
		}

		return list;
	}

	@Override
	public void close() {
		close(null);
	}

	public void close(FailoverQueue<T> backupQueue) {
		List<T> items = new ArrayList<T>(queue.size());
		queue.drainTo(items);

		for (int i = 0; i < items.size(); i++) {
			T item = items.get(i);
			if (backupQueue == null) {
				log.warn("[YEON] Discard message. ({})", item);
				itemRemoved(item);
			} else {
				try {
					backupQueue.put(item);
				} catch (InterruptedException e) {
					backupQueue = null;
					itemRemoved(item);
				}
			}
		}
	}

	private void itemRemoved(T item) {
		if (removedItemListener != null) {
			removedItemListener.itemRemoved(item);
		}
	}

	/**
	 * 
	 * @author pulsarang
	 * @param <T>
	 */
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
