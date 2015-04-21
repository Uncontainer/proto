package com.pulsarang.mom.dispatcher.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.pulsarang.infra.mom.processor.EventProcessorInfo;
import com.pulsarang.infra.server.Server;
import com.pulsarang.mom.ProcessorEventQueueManager;
import com.pulsarang.mom.queue.proc.EventQueueFetcher;
import com.pulsarang.mom.store.EventEnvelope;

public class ProcessorEventQueue extends EventQueueFetcher {
	private EventProcessorInfo processorInfo;
	private List<Server> servers;
	private EventProcessorInvokerPool pool;

	/**
	 * {@link ProcessorEventQueueManager#getProcessorEventQueue(String)}를 이용해서 instance를 가져올 수 있다.
	 * 
	 * @param eventProcessorInfo
	 * @param pool
	 */
	public ProcessorEventQueue(boolean persist, EventProcessorInfo processorInfo, EventProcessorInvokerPool pool) {
		// TODO 이름 규칙 수청
		super(processorInfo.getName(), true);
		this.processorInfo = processorInfo;
		this.pool = pool;
	}

	@Override
	protected void process(EventEnvelope eventEnvelope, boolean verify) throws Exception {
		// TODO Auto-generated method stub
		EventProcessContext context = new EventProcessContext(eventEnvelope, this);

		CountDownLatch latch = new CountDownLatch(1);
		execute(context, latch, true);

		latch.await();
	}

	public EventProcessorInvoker execute(EventProcessContext context, CountDownLatch latch, boolean persisted) {
		// TODO persisted 되었는지에 따른 처리 추가.
		EventProcessorInvoker executor = createEventProcessorExecutor(context, latch);
		if (executor == null) {
			latch.countDown();
		} else {
			pool.execute(executor);
		}

		return executor;
	}

	private EventProcessorInvoker createEventProcessorExecutor(EventProcessContext context, CountDownLatch endLatch) {
		if (!processorInfo.useProcessor()) {
			// TODO event가 더 이상 전달되지 않도록 조치를 취해야 함.
			context.getEventEnvelope().discard(processorInfo);
			return null;
		}

		if (processorInfo.isExpiredEvent(context.getEvent())) {
			context.getEventEnvelope().expire(processorInfo);

			return null;
		}

		if (processorInfo.isPostponeStatus(context.isReprocess())) {
			long interval = processorInfo.getRetryInterval();

			context.getEventEnvelope().postpone(interval, processorInfo);

			return null;
		}

		EventProcessorInvoker processorExecutor = new EventProcessorInvoker(context, endLatch);

		return processorExecutor;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return processorInfo.toString();
	}

	public EventProcessorInfo getProcessorInfo() {
		return processorInfo;
	}

	public void setProcessorInfo(EventProcessorInfo processorInfo) {
		this.processorInfo = processorInfo;
	}

	public synchronized List<Server> getServers() {
		return servers;
	}

	public synchronized void setServers(List<Server> servers) {
		this.servers = servers;
	}

	public synchronized void addServer(Server server) {
		if (servers == null) {
			servers = new ArrayList<Server>();
		}

		servers.add(server);
	}

	public void publish(EventProcessContext context) {
		// TODO Auto-generated method stub
		// TODO 등록된 서버 중 하나로 event를 송신하는 코드 추가.

	}
}
