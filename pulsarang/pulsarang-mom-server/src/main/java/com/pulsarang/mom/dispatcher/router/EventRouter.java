/*
 * EventProcessorController.java $version 2010. 10. 15
 * 
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.pulsarang.mom.dispatcher.router;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.mom.dispatcher.processor.EventProcessContext;
import com.pulsarang.mom.dispatcher.processor.EventProcessorInvoker;
import com.pulsarang.mom.dispatcher.processor.ProcessorEventQueue;
import com.pulsarang.mom.store.EventEnvelope;

public class EventRouter {
	private static final Logger LOG = LoggerFactory.getLogger(EventRouter.class);

	long awaitMillies;
	List<ProcessorEventQueue> processorEventQueues;

	private CountDownLatch completeCheckLatch;

	private List<EventProcessorInvoker> executors;

	private EventRouterStatus status;

	public EventRouter(List<ProcessorEventQueue> processorEventQueues, long awaitMillies) {
		super();
		this.awaitMillies = awaitMillies;
		this.processorEventQueues = processorEventQueues;
		status = EventRouterStatus.WAITING;
	}

	public void execute(EventEnvelope eventEnvelope, boolean verify) {
		synchronized (this) {
			if (eventEnvelope == null || status != EventRouterStatus.WAITING) {
				throw new IllegalStateException();
			}

			if (status == EventRouterStatus.CANCELLED) {
				LOG.info("Event processor controller cancelled.");
				return;
			}
		}

		// TODO event processor executors를 생성하는 도중에 발생하는 예외 처리 추가.
		executors = executes(eventEnvelope);
		if (executors.isEmpty()) {
			return;
		}

		waitForExit();
	}

	public boolean cancel() {
		synchronized (this) {
			if (status != EventRouterStatus.WAITING) {
				return false;
			}

			status = EventRouterStatus.CANCELLED;
		}

		for (EventProcessorInvoker executor : executors) {
			try {
				executor.cancel();
			} catch (RuntimeException e) {
				LOG.error("Fail to cancel EventProcessor.", e);
			}
		}

		return true;
	}

	private List<EventProcessorInvoker> executes(EventEnvelope eventEnvelope) {
		synchronized (this) {
			status = EventRouterStatus.RUNNING;
		}

		List<EventProcessorInvoker> invokers = new ArrayList<EventProcessorInvoker>();
		if (processorEventQueues.isEmpty()) {
			return Collections.emptyList();
		}

		completeCheckLatch = new CountDownLatch(processorEventQueues.size());
		for (ProcessorEventQueue queue : processorEventQueues) {
			EventProcessContext context = new EventProcessContext(eventEnvelope, queue);
			EventProcessorInvoker invoker = queue.execute(context, completeCheckLatch, false);
			invokers.add(invoker);
		}

		return invokers;
	}

	private void waitForExit() {
		try {
			if (awaitMillies <= 0) {
				completeCheckLatch.await();
			} else {
				if (!completeCheckLatch.await(awaitMillies, TimeUnit.MILLISECONDS)) {
					synchronized (this) {
						status = EventRouterStatus.EXEC_TIMEOUT;
					}
					LOG.error("Wait for all processors exit time out.");
				}
			}

			synchronized (this) {
				status = EventRouterStatus.SUCCESS;
			}
		} catch (InterruptedException e) {
			synchronized (this) {
				status = EventRouterStatus.INTERRUPTED;
				LOG.error("Wait for all processors exit interrupted.", e);
			}

			Thread.currentThread().interrupt();
		}

		int failCount = 0;
		for (EventProcessorInvoker executor : executors) {
			if (executor.isSuccess()) {
				continue;
			}

			executor.detachIfRunning();

			failCount++;
		}
	}
}
