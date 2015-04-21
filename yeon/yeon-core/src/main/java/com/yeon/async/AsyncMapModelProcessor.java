package com.yeon.async;

import com.yeon.Disposable;
import com.yeon.YeonContext;
import com.yeon.async.FailoverQueue.RemovedItemListener;
import com.yeon.async.FailoverQueue.UncompletedItemListener;
import com.yeon.mom.MomConstants;
import com.yeon.monitor.resource.MomMBeanFactory;
import com.yeon.monitor.resource.annotation.MonitoringBean;
import com.yeon.monitor.resource.annotation.MonitoringProperty;
import com.yeon.remote.reload.AbstractReloadService;
import com.yeon.util.ExceptionUtils;
import com.yeon.util.MapModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AsyncMapModelProcessor<T extends MapModel> extends AbstractReloadService implements Disposable, RemovedItemListener<T>, UncompletedItemListener<T> {
	private final Logger log = LoggerFactory.getLogger(AsyncMapModelProcessor.class);

	static final String NS = "apque";

	protected FailoverQueue<T> queue;

	private final int threadCount;
	private ThreadGroup threadGroup;
	private int threadNumber = 1;

	private int maxProcessCount = 0;

	private volatile boolean dequeuing;

	final Class<T> clazz;
	final String canonicalName;

	public AsyncMapModelProcessor(String name, Class<T> clazz, int maxSize, FailoverQueueType type) {
		this(name, clazz, maxSize, 0, type, 1);
	}

	public AsyncMapModelProcessor(String name, Class<T> clazz, int maxSize, int maxProcessCount, FailoverQueueType type) {
		this(name, clazz, maxSize, maxProcessCount, type, 1);
	}

	public AsyncMapModelProcessor(String name, Class<T> clazz, int maxSize, int maxProcessCount, FailoverQueueType type, int threadCount) {
		if (threadCount < 0 || threadCount > 16) {
			throw new IllegalArgumentException("ThreadCount must be 1 <= x <= 16");
		}

		this.canonicalName = getCanonicalName(name);
		this.maxProcessCount = maxProcessCount;
		this.threadCount = threadCount;
		this.queue = FailoverQueueFactory.getQueue(name, clazz, type, maxSize);
		this.queue.setRemovedItemListener(this);
		this.queue.setUncompletedItemListener(this);
		this.clazz = clazz;

		resumeDequeuing();

		YeonContext yeonContext = YeonContext.getYeonContext();

		yeonContext.addDisposable(this);
		YeonContext.getRemoteContext().setRemoteService(canonicalName, this);

		MonitorMBean mBean = getMBean();
		if (mBean != null) {
			MomMBeanFactory.getInstance().addMBean(canonicalName, mBean);
		}
	}

	protected MonitorMBean getMBean() {
		return new MonitorMBean(this);
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

	protected synchronized void pauseDequeuing() {
		if (threadGroup == null) {
			return;
		}

		dequeuing = false;
		threadGroup.interrupt();
		threadGroup = null;
	}

	protected synchronized void resumeDequeuing() {
		if (threadGroup != null) {
			return;
		}

		dequeuing = true;
		threadGroup = new ThreadGroup("mom-" + canonicalName + "-dequer");
		for (int i = 0; i < threadCount; i++) {
			new Thread(threadGroup, new TakeAndProcessInvoker(), threadGroup.getName() + "-" + threadNumber++).start();
		}
	}

	public boolean isDeduing() {
		return dequeuing;
	}

	public boolean isRunning() {
		return threadGroup != null;
	}

	@Override
	public synchronized void dispose() {
		queue = null;

		pauseDequeuing();
	}

	private synchronized FailoverQueue<T> getQueue() {
		return queue;
	}

	@Override
	public List<MapModel> list(MapModel optionMap) {
		if (queue.isEmpty()) {
			return null;
		}

		List<FailoverQueue.Entry<T>> elements = queue.elements(100);
		List<MapModel> list = new ArrayList<MapModel>(elements.size());
		for (FailoverQueue.Entry<T> element : elements) {
			MapModel item = new MapModel(element.getMessage().getValues());
			item.put("staus", element.getStatus());
			list.add(item);
		}

		return list;
	}

	@Override
	public int listCount(MapModel optionMap) {
		return queue.size();
	}

	protected abstract void process(T item);

	@Override
	public void itemRemoved(T item) {
		log.info("[YEON] Removed item discarded.({})", item);
	}

	@Override
	public void uncompletedItemDetected(T item) {
		log.info("[YEON] Uncompleted item discarded.({})", item);
	}

	/**
	 * 
	 * @author pulsarang
	 */
	private class TakeAndProcessInvoker implements Runnable {
		static final String PARAM_PROCESS_TRY_COUNT = "_prcs_try_cnt";

		@Override
		public void run() {
			while (dequeuing) {
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
					try {
						process(item.getMessage());
					} catch (Exception e) {
						handleException(queueCopy, item, e);
					} finally {
						item.ack();
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return;
				}
			}
		}

		private void handleException(FailoverQueue<T> queue, FailoverQueue.Entry<T> item, Exception exception) throws InterruptedException {
			log.error("[YEON] Fail to process item.", exception);

			if (maxProcessCount > 0 && increaseProcessTryCount(item.getMessage()) > maxProcessCount) {
				itemRemoved(item.getMessage());
			} else {
				try {
					queue.put(item.getMessage());
				} catch (Exception e2) {
					log.error("[YEON] Fail to requeue event.", e2);
					itemRemoved(item.getMessage());
					if (e2 instanceof InterruptedException) {
						throw (InterruptedException) e2;
					}

					ExceptionUtils.launderThrowable(e2);
				}
			}

			Thread.sleep(MomConstants.MIN_PROCESS_TRY_INTERVAL);
		}

		private int increaseProcessTryCount(MapModel message) {
			int newProcessTryCount = getProcessTryCount(message) + 1;
			message.setInteger(PARAM_PROCESS_TRY_COUNT, newProcessTryCount);

			return newProcessTryCount;
		}

		private int getProcessTryCount(MapModel message) {
			return message.getInteger(PARAM_PROCESS_TRY_COUNT, 0);
		}
	}

	/**
	 * @author EC셀러서비스개발팀
	 */
	@MonitoringBean(inclusive = false)
	public static class MonitorMBean {
		protected AsyncMapModelProcessor<?> proc;

		public MonitorMBean(AsyncMapModelProcessor<?> proc) {
			if (proc == null) {
				throw new IllegalArgumentException();
			}

			this.proc = proc;
		}

		@MonitoringProperty(name = "queueSize")
		public int getQueueSize() {
			return proc.queue.size();
		}

		@MonitoringProperty(name = "dequing")
		public boolean isDequeing() {
			return proc.dequeuing;
		}

		@MonitoringProperty(name = "running")
		public boolean isRunning() {
			return proc.isRunning();
		}
	}
}
