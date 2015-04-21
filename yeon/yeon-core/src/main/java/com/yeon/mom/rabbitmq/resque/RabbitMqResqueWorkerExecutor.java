package com.yeon.mom.rabbitmq.resque;

import com.yeon.mom.EventProcessContext;
import com.yeon.mom.EventProcessor;
import com.yeon.mom.ResqueWorker;
import com.yeon.mom.event.EventType;
import com.yeon.mom.event.QueueEventType;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author pulsarang
 */
public class RabbitMqResqueWorkerExecutor implements EventProcessor {
	ResqueWorker worker;

	public RabbitMqResqueWorkerExecutor(ResqueWorker worker) {
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
	public List<? extends EventType> getSubscribeEvents() {
		String queueName = RabbitMqResque.getQueueName(getName());
		return Arrays.asList(new QueueEventType(queueName));
	}

	@Override
	public void process(EventProcessContext context) {
		worker.process(RabbitMqResque.unwrapArguments(context.getEvent()));
	}

	@Override
	public void reprocess(EventProcessContext context) {
		worker.reprocess(RabbitMqResque.unwrapArguments(context.getEvent()), context.getReprocessArguments());
	}

	@Override
	public void verify(EventProcessContext context) {
		worker.verify(RabbitMqResque.unwrapArguments(context.getEvent()), context.getReprocessArguments());
	}

	@Override
	public void fail(EventProcessContext context) {
		reprocess(context);
	}
}
