package com.pulsarang.infra.mom.sxb.producer;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.infra.mom.EventProducer;
import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.infra.mom.util.async.AsyncMapModelProcessor;
import com.pulsarang.infra.monitoring.collector.Mergable;
import com.pulsarang.infra.monitoring.collector.Monitorable;

public class QueuedEventProducer extends AsyncMapModelProcessor<Event> implements EventProducer {
	public static final String NAME = "publishWaitEvent";

	private final Logger log = LoggerFactory.getLogger(QueuedEventProducer.class);

	EventProducer parent;

	public QueuedEventProducer(EventProducer eventProducer) {
		super(NAME, Event.class, true, 1000);
		this.parent = eventProducer;
	}

	public Monitorable<Integer> getQueueSizeMonitorable() {
		Monitorable<Integer> monitorable = new Monitorable<Integer>() {
			@Override
			public Integer createShapshot() {
				return queue.size();
			}
		};

		return monitorable;
	}

	public static Mergable<Integer> getQueueSizeMergable() {
		Mergable<Integer> mergable = new Mergable<Integer>() {
			@Override
			public Integer merge(Integer source, Integer target) {
				return source.intValue() + target.intValue();
			}
		};

		return mergable;
	}

	@Override
	public void publish(Event event) {
		publish(event, false);
	}

	@Override
	public void publish(Event event, boolean async) {
		Date date = event.getEventDate();
		if (date == null) {
			event.setEventDate(new Date());
		}

		if (event.hasEventProcessOption()) {
			throw new IllegalArgumentException("Not empty event process option.");
		}

		if (async) {
			try {
				super.put(event);
			} catch (Exception e) {
				log.error("[MOM] Fail to queueing event.({})", event);
				process(event);
			}
		} else {
			process(event);
		}
	}

	@Override
	public void retry(Event event) {
		event.setEventDate(new Date());
		if (!event.hasEventProcessOption()) {
			throw new IllegalArgumentException("Empty event process option.");
		}

		process(event);
	}

	@Override
	protected void process(Event event) {
		try {
			// TODO retry와 publish 기준을 판단하는 명확한 parameter를 넣을 것.
			if (event.hasEventProcessOption()) {
				parent.retry(event);
			} else {
				parent.publish(event, false);
			}
		} catch (RepublishException e) {
			log.error("[MOM] Faile to publish event.(" + event + ")\nTry republish.", e);

			// publish 시도 횟수 증가.
			event.setProperty("_pbs_retry_cnt", event.getProperty("_pbs_retry_cnt", 0) + 1);
			super.put(event);
		} catch (Throwable t) {
			log.error("[MOM] Fail to publish event.(" + event + ")", t);
		}
	}
}
