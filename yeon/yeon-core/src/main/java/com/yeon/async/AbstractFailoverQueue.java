package com.yeon.async;

import com.yeon.util.MapModel;

public abstract class AbstractFailoverQueue<T extends MapModel> implements FailoverQueue<T> {
	protected RemovedItemListener<T> removedItemListener;
	protected UncompletedItemListener<T> uncompletedItemListener;

	@Override
	public RemovedItemListener<T> getRemovedItemListener() {
		return removedItemListener;
	}

	@Override
	public void setRemovedItemListener(RemovedItemListener<T> itemRemovedListener) {
		this.removedItemListener = itemRemovedListener;
	}

	@Override
	public UncompletedItemListener<T> getUncompletedItemListener() {
		return uncompletedItemListener;
	}

	@Override
	public void setUncompletedItemListener(UncompletedItemListener<T> uncompletedItemListener) {
		this.uncompletedItemListener = uncompletedItemListener;
	}

}
