package com.yeon.mom.rabbitmq.resque;

import com.yeon.YeonContext;
import com.yeon.mom.EventProducer;
import com.yeon.mom.Resque;
import com.yeon.mom.ResqueWorker;
import com.yeon.mom.event.Event;
import com.yeon.mom.event.QueueEvent;
import com.yeon.mom.event.QueueEventType;

import java.util.Map;

/**
 * 
 * @author pulsarang
 */
public class RabbitMqResque implements Resque {
	EventProducer eventProducer;
	QueueEventType eventType;
	ResqueWorker worker;

	public RabbitMqResque(ResqueWorker worker, EventProducer eventProducer) {
		this.eventType = new QueueEventType(getQueueName(worker.getName()));
		this.eventProducer = eventProducer;
		this.worker = worker;
	}

	@Override
	public void enqueue(Map<String, Object> arguments) {
		enqueue(arguments, worker.getAsync());
	}

	@Override
	public void enqueue(Map<String, Object> arguments, boolean async) {
		if (arguments == null || arguments.isEmpty()) {
			throw new IllegalArgumentException("Empty arguments.");
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
		event.setMap("_resque_arguments", arguments);

		return event;
	}

	public static Map<String, Object> unwrapArguments(Event event) {
		return event.getMap("_resque_arguments");
	}

	public static String getQueueName(String workerName) {
		return "resque_" + YeonContext.getSolutionName() + "_" + YeonContext.getProjectName() + "_" + workerName;
	}
}
