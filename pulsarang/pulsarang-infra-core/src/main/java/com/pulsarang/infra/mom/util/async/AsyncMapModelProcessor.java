package com.pulsarang.infra.mom.util.async;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.Disposable;
import com.pulsarang.infra.InfraContext;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.remote.reload.AbstractReloadService;

public abstract class AsyncMapModelProcessor<T extends MapModel> extends AbstractReloadService implements Runnable, Disposable {
	private final Logger log = LoggerFactory.getLogger(AsyncMapModelProcessor.class);

	static final String NS = "apque";

	protected FailoverQueue<T> queue;

	final Thread thread;

	final Class<T> clazz;

	public AsyncMapModelProcessor(String name, Class<T> clazz, boolean persist, int maxSize) {
		this.queue = FailoverQueueFactory.getQueue(name, clazz, persist, maxSize);
		this.clazz = clazz;

		thread = new Thread(this);
		thread.start();

		InfraContext infraContext = InfraContextFactory.getInfraContext();
		infraContext.addDisposable(this);
		String canonicalName = getCanonicalName(name);
		// TODO remoteService에 이름을 주어 셋팅할 수 있는 방법 추가.
		// infraContext.getRemoteContext().setRemoteService(canonicalName, this);
	}

	public static final String getCanonicalName(String simpleName) {
		return NS + "$" + simpleName;
	}

	public void put(T item) {
		try {
			queue.put(item);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void dispose() {
		synchronized (this) {
			queue = null;
		}

		thread.interrupt();
	}

	@Override
	public void run() {
		while (true) {
			FailoverQueue.Entry<T> item;
			FailoverQueue<T> queueCopy = getQueue();
			if (queueCopy == null) {
				return;
			}

			try {
				item = queueCopy.take();
				if (item == null) {
					continue;
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}

			try {
				process(item.getMessage());
			} catch (Exception e) {
				// TODO 선별적으로 queue에 다시 넣도록 해야 함.
				log.error("[MOM] Fail to process item.", e);
				try {
					queueCopy.put(item.getMessage());
				} catch (Exception e2) {
					log.error("[MOM] Fail to requeue event.", e2);
				}
			} finally {
				try {
					item.ack();
				} catch (Exception e) {
					log.error("[MOM] Fail to remove sent event.", e);
				}
			}
		}
	}

	private synchronized FailoverQueue<T> getQueue() {
		return queue;
	}

	@Override
	public List<MapModel> list(MapModel optionMap) {
		if (queue.isEmpty()) {
			return null;
		}

		List<FailoverQueue.Entry<T>> elements = queue.elements();
		List<MapModel> list = new ArrayList<MapModel>(elements.size());
		for (FailoverQueue.Entry<T> element : elements) {
			MapModel map = new MapModel(element.getMessage());
			map.setInteger("staus", element.getStatus());
			list.add(map);
		}

		return list;
	}

	@Override
	public int listCount(MapModel optionMap) {
		return queue.size();
	}

	protected abstract void process(T item);
}
