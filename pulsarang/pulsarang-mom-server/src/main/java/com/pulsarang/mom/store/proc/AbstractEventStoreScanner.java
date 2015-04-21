package com.pulsarang.mom.store.proc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.mom.dispatcher.router.EventRouter;
import com.pulsarang.mom.store.EventEnvelope;
import com.pulsarang.mom.store.EventStore;

public abstract class AbstractEventStoreScanner implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(AbstractEventStoreScanner.class);

	private String name;
	private EventStore store;

	private volatile Semaphore semaphore;
	private volatile int size;

	private ThreadPoolExecutor executor;
	private AtomicLong executionCount;

	private Thread thread;
	private boolean stop = true;

	private final Lock popLock = new ReentrantLock();
	private final Condition notEmpty = popLock.newCondition();

	private static Map<String, AbstractEventStoreScanner> runningScanners = Collections
			.synchronizedMap(new HashMap<String, AbstractEventStoreScanner>());

	public AbstractEventStoreScanner(String name, EventStore store, int size) {
		this.name = name;
		this.store = store;

		this.size = size;
		semaphore = new Semaphore(size);
	}

	public void start() {
		if (thread != null) {
			throw new IllegalStateException("Scanner has benn stared.");
		}

		executor = new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
				((EventConsumerWrapper) runnable).cancel();
			}
		});
		executionCount = new AtomicLong(0);

		stop = false;
		thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		if (stop) {
			return;
		}

		thread.interrupt();
		executor.shutdownNow();
		stop = true;
	}

	public void push(Event event) {
		store.insert(event);

		touch();
	}

	public void touch() {
		popLock.lock();
		try {
			notEmpty.signal();
		} finally {
			popLock.unlock();
		}
	}

	@Override
	public void run() {
		if (runningScanners.containsKey(name)) {
			throw new IllegalStateException("Scanner is running.(" + name + ")");
		} else {
			runningScanners.put(name, this);
		}

		// 반드시 RUNNING상태의 event를 모두 소진한 이후 WAIT 상태의 event를 처리하는 프로세스로 넘어가야 한다.
		// 그렇지 않을 경우, 현재 처리중인 event가 중복 처리된다.
		processRunningEvent();

		while (true) {
			if (stop) {
				return;
			}

			try {
				List<EventEnvelope> eventEnvelopes;
				popLock.lock();
				try {
					eventEnvelopes = store.extracts(EventStore.STATUS_WAIT, 10);
					if (eventEnvelopes.isEmpty()) {
						notEmpty.await();

						continue;
					}
				} finally {
					popLock.unlock();
				}

				for (EventEnvelope eventEnvelope : eventEnvelopes) {
					processWithCongestionControl(eventEnvelope, false);
				}
			} catch (InterruptedException e) {
				log.info("[MOM] Waiting for event interruped.", e);
				return;
			} catch (Exception e) {
				log.error("[MOM] Fail to process item.", e);
			} finally {
				// TODO process()에서 ack를 주지 않았을 경우에 대한 처리 추가.
			}
		}
	}

	private void processWithCongestionControl(EventEnvelope eventEnvelope, boolean verify) {
		final Semaphore snapshot = semaphore;
		EventConsumer consumer = null;

		try {
			snapshot.acquire();
			if (!eventEnvelope.mark()) {
				return;
			}

			consumer = createEventConsumer(eventEnvelope);
			executor.execute(new EventConsumerWrapper(consumer, snapshot, eventEnvelope, verify));
			executionCount.incrementAndGet();

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.info("[MOM.Dispatcher] Wait for executor available interrupted.", e);
			if (consumer != null) {
				consumer.cancel();
			}
		} finally {
			snapshot.release();
		}
	}

	private void processRunningEvent() {
		List<EventEnvelope> eventEnvelopes = store.extracts(EventStore.STATUS_PROCESS, 10);
		while (!eventEnvelopes.isEmpty()) {
			// TODO EventEnvelope에 verify 상태 코드를 저장하고 EventExecutor에서 판단하도록 수정.
			for (EventEnvelope eventEnvelope : eventEnvelopes) {
				EventConsumer consumer = createEventConsumer(eventEnvelope);
				processSafely(consumer, false, eventEnvelope);
			}

			eventEnvelopes = store.extracts(EventStore.STATUS_PROCESS, 10);
		}
	}

	private static void processSafely(EventConsumer consumer, boolean verify, EventEnvelope eventEnvelope) {
		try {
			consumer.process(eventEnvelope, verify);
		} catch (Exception e) {
			log.error("[MOM] Fail to process item.", e);
		} finally {
			// TODO 재처리가 필요한 경우에 대한 판단 후 처리 추가.
			eventEnvelope.acknowlege();
		}
	}

	public int getSize() {
		return size;
	}

	public void setSize(int newSize) {
		if (this.size == newSize) {
			return;
		}

		Semaphore temp = new Semaphore(newSize);
		if (newSize > size) {
			executor.setMaximumPoolSize(newSize);
			executor.setCorePoolSize(newSize);
			semaphore = temp;
			size = newSize;
		} else {
			executor.setCorePoolSize(newSize);
			executor.setMaximumPoolSize(newSize);
			size = newSize;
			semaphore = temp;
		}
	}

	public int getAvailableCount() {
		return semaphore.availablePermits();
	}

	public int getActiveCount() {
		return size - getAvailableCount();
	}

	public long getExecutionCount() {
		return executionCount.get();
	}

	/**
	 * 중지와 관련된 모든 작업은 {@link EventRouter} 에게 위임한다.
	 */
	public void shutdown() {
		Thread.currentThread().interrupt();

		List<Runnable> wrappers = executor.shutdownNow();
		for (Runnable wrapper : wrappers) {
			((EventConsumerWrapper) wrapper).cancel();
		}
	}

	protected abstract EventConsumer createEventConsumer(EventEnvelope eventEnvelope);

	/**
	 * 
	 * @author pulsarang
	 */
	private static class EventConsumerWrapper implements Runnable {
		private EventConsumer consumer;
		private Semaphore semaphore;
		private EventEnvelope eventEnvelope;
		private boolean verify;

		private EventConsumerWrapper(EventConsumer consumer, Semaphore semaphore, EventEnvelope eventEnvelope, boolean verify) {
			this.consumer = consumer;
			this.semaphore = semaphore;
			this.eventEnvelope = eventEnvelope;
			this.verify = verify;
		}

		@Override
		public void run() {
			try {
				processSafely(consumer, verify, eventEnvelope);
			} finally {
				semaphore.release();
			}
		}

		private void cancel() {
			consumer.cancel();
		}
	}
}
