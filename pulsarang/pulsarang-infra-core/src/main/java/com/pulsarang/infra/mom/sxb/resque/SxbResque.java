package com.pulsarang.infra.mom.sxb.resque;

import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.pulsarang.infra.InfraContext;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.mom.EventProducer;
import com.pulsarang.infra.mom.Resque;
import com.pulsarang.infra.mom.ResqueWorker;
import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.infra.mom.event.QueueEvent;
import com.pulsarang.infra.mom.event.QueueEventType;

public class SxbResque implements Resque {
	EventProducer eventProducer;
	QueueEventType eventType;
	ResqueWorker worker;

	public SxbResque(ResqueWorker worker) {
		this.eventType = new QueueEventType(getQueueName(worker.getName()));
		this.eventProducer = InfraContextFactory.getInfraContext().getMomContext().getEventProducer();
		this.worker = worker;
	}

	@Override
	public void enqueue(Map<String, Object> arguments) {
		enqueue(arguments, worker.getAsync());
	}

	@Override
	public void enqueue(Map<String, Object> arguments, boolean async) {
		if (MapUtils.isEmpty(arguments)) {
			throw new IllegalArgumentException("Emtpry arguments.");
		}

		if (async) {
			eventProducer.publish(wrapArguments(eventType, arguments));
		} else {
			worker.process(arguments);
		}
	}

	public QueueEventType getEventType() {
		return eventType;
	}

	public ResqueWorker getWorker() {
		return worker;
	}

	public boolean isAsync() {
		return worker.getAsync();
	}

	public static Event wrapArguments(QueueEventType queueType, Map<String, Object> arguments) {
		Event event = new QueueEvent(queueType);
		event.setProperty("_resque_arguments", arguments);

		return event;
	}

	public static Map<String, Object> unwrapArguments(Event event) {
		return event.getProperty("_resque_arguments");
	}

	public static String getQueueName(String workerName) {
		InfraContext infraContext = InfraContextFactory.getInfraContext();
		return "resque_" + infraContext.getSolutionName() + "_" + infraContext.getProjectName() + "_" + workerName;
	}
}
