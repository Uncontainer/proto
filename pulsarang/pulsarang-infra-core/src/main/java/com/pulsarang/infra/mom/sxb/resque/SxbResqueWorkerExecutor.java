package com.pulsarang.infra.mom.sxb.resque;

import java.util.Arrays;
import java.util.List;

import com.pulsarang.infra.mom.EventProcessContext;
import com.pulsarang.infra.mom.EventProcessor;
import com.pulsarang.infra.mom.ResqueWorker;
import com.pulsarang.infra.mom.event.EventType;
import com.pulsarang.infra.mom.event.QueueEventType;

public class SxbResqueWorkerExecutor implements EventProcessor {
	ResqueWorker worker;

	public SxbResqueWorkerExecutor(ResqueWorker worker) {
		if (worker == null) {
			throw new IllegalArgumentException("Resqueworker is null.");
		}

		this.worker = worker;
	}

	public ResqueWorker getWorker() {
		return worker;
	}

	@Override
	public String getName() {
		return worker.getName();
	}

	@Override
	public List<? extends EventType> getSubscribeEventTypes() {
		String queueName = SxbResque.getQueueName(getName());
		return Arrays.asList(new QueueEventType(queueName));
	}

	@Override
	public void process(EventProcessContext context) {
		worker.process(SxbResque.unwrapArguments(context.getEvent()));
	}

	@Override
	public void reprocess(EventProcessContext context) {
		worker.reprocess(SxbResque.unwrapArguments(context.getEvent()), context.getReprocessArguments());
	}

	@Override
	public void verify(EventProcessContext context) {
		worker.verify(SxbResque.unwrapArguments(context.getEvent()), context.getReprocessArguments());
	}

	@Override
	public void fail(EventProcessContext context) {
		reprocess(context);
	}
}
