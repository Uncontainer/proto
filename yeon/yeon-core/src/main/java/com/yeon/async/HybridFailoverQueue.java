package com.yeon.async;

import com.yeon.api.ApiServiceFactory;
import com.yeon.monitor.alarm.AlarmLog;
import com.yeon.monitor.alarm.AlarmLogType;
import com.yeon.util.MapModel;

import java.util.ArrayList;
import java.util.List;

public class HybridFailoverQueue<T extends MapModel> implements FailoverQueue<T> {
	private final String name;
	private final H2FailoverQueue<T> fileQueue;
	private final BlockingFailoverQueue<T> memoryQueue;
	private int moveCount;

	HybridFailoverQueue(String name, Class<T> clazz, int maxMemoryQueueSize) {
		this.name = name;
		fileQueue = new H2FailoverQueue<T>(name, clazz);
		memoryQueue = new BlockingFailoverQueue<T>(name, maxMemoryQueueSize);
		moveCount = 0;
	}

	@Override
	public void put(T model) throws InterruptedException {
		synchronized (this) {
			if (memoryQueue.isFull()) {
				if (moveCount % 100 == 0) {
					AlarmLog alarmLog = new AlarmLog(AlarmLogType.ALT_HYBIRD_QUEUE_MODE_CHANGED, "HybridQueue mode changed.(MEMORY -> FILE)", null, name);
					ApiServiceFactory.getMonitorApiService().logAlarm(alarmLog);
				}

				if (!moveItem()) {
					throw new RuntimeException("Move messages from memory to file failed.");
				}
			}
		}

		memoryQueue.put(model);
	}

	@Override
	public Entry<T> take() throws InterruptedException {
		if (fileQueue.isEmpty()) {
			return memoryQueue.take();
		} else {
			return fileQueue.take();
		}
	}

	@Override
	public void clear() {
		fileQueue.clear();
		memoryQueue.clear();
	}

	@Override
	public int size() {
		return fileQueue.size() + memoryQueue.size();
	}

	@Override
	public boolean isEmpty() {
		return memoryQueue.isEmpty();
	}

	@Override
	public List<Entry<T>> elements(int maxCount) {
		List<Entry<T>> result = new ArrayList<Entry<T>>(memoryQueue.elements(maxCount));
		if (result.size() != maxCount) {
			result.addAll(fileQueue.elements(maxCount - result.size()));
		}

		return result;
	}

	@Override
	public void close() {
		memoryQueue.close(fileQueue);
		fileQueue.close();
	}

	@Override
	public void setRemovedItemListener(RemovedItemListener<T> listener) {
		memoryQueue.setRemovedItemListener(listener);
		fileQueue.setRemovedItemListener(listener);
	}

	@Override
	public RemovedItemListener<T> getRemovedItemListener() {
		return memoryQueue.getRemovedItemListener();
	}

	@Override
	public void setUncompletedItemListener(UncompletedItemListener<T> listener) {
		memoryQueue.setUncompletedItemListener(listener);
		fileQueue.setUncompletedItemListener(listener);
	}

	@Override
	public UncompletedItemListener<T> getUncompletedItemListener() {
		return memoryQueue.getUncompletedItemListener();
	}

	private boolean moveItem() throws InterruptedException {
		if (memoryQueue.isEmpty()) {
			return false;
		}

		Entry<T> entry = memoryQueue.take();
		if (entry == null) {
			return false;
		}

		moveCount++;

		try {
			fileQueue.put(entry.getMessage());
			return true;
		} finally {
			entry.ack();
		}
	}

}
