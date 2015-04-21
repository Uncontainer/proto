package com.yeon.async;

import com.yeon.util.MapModel;

import java.util.List;

public interface FailoverQueue<T extends MapModel> {
	void put(T model) throws InterruptedException;

	Entry<T> take() throws InterruptedException;

	void clear();

	int size();

	boolean isEmpty();

	List<Entry<T>> elements(int maxCount);

	void close();

	/**
	 * Queue의 용량을 넘어서는 item이 있을 경우 삭제된 item을 알려준다.
	 * 
	 * @param listener
	 */
	void setRemovedItemListener(RemovedItemListener<T> listener);

	RemovedItemListener<T> getRemovedItemListener();

	/**
	 * 처리 완료 여부를 알 수 없는 item을 인지했을 경우 호출된다. 
	 * @param listener
	 */
	void setUncompletedItemListener(UncompletedItemListener<T> listener);

	UncompletedItemListener<T> getUncompletedItemListener();

	/**
	 * 
	 * @author pulsarang
	 * @param <T>
	 */
	interface Entry<T> {
		int STATUS_WAIT = 1;
		int STATUS_RUNNING = 2;

		T getMessage();

		int getStatus();

		int ack();
	}

	/**
	 * @author EC셀러서비스개발팀 
	 * @param <T>
	 */
	interface RemovedItemListener<T> {
		void itemRemoved(T item);
	}

	/**
	 * @author EC셀러서비스개발팀 
	 * @param <T>
	 */
	interface UncompletedItemListener<T> {
		void uncompletedItemDetected(T item);
	}
}
