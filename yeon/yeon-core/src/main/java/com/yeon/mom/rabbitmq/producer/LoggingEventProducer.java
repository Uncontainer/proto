package com.yeon.mom.rabbitmq.producer;

import com.yeon.api.ApiServiceFactory;
import com.yeon.async.AsyncMapModelProcessor;
import com.yeon.async.FailoverQueueType;
import com.yeon.mom.EventProducer;
import com.yeon.mom.event.Event;
import com.yeon.mom.event.EventApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author pulsarang
 */
public class LoggingEventProducer extends AsyncMapModelProcessor<Event> implements EventProducer {
	public static final String NAME = "loggingEvent";
	private final Logger log = LoggerFactory.getLogger(LoggingEventProducer.class);

	EventProducer parent;

	EventApiService eventApi = ApiServiceFactory.getEventApiService();

	long currentEventId = -1;
	long maxEventId = -1;
	long lastReserverSerialFailed = 0L;
	public static final int INC_AMOUNT = 100;

	public LoggingEventProducer(EventProducer eventProducer) {
		super(NAME, Event.class, 100, FailoverQueueType.HYBRID);
		this.parent = eventProducer;
	}

	@Override
	public void publish(Event event) {
		publish(event, false);
	}

	@Override
	public void publish(Event event, boolean async) {
		event.setEventId(nextEventId());
		parent.publish(event, async);

		if (event.getEventId() != 0L) {
			put(event);
		}
	}

	@Override
	public void retry(Event event) {
		parent.retry(event);

		put(event);
	}

	private synchronized long nextEventId() {
		// TODO Event 로깅하지 못한다는 것에 대한 대한 로그 추가.
		if (System.currentTimeMillis() - lastReserverSerialFailed < 3000L) {
			return 0L;
		}

		try {
			currentEventId++;
			if (currentEventId > maxEventId) {
				currentEventId = eventApi.reserveSerial(INC_AMOUNT);
				if (currentEventId == 0) {
					currentEventId = -1;
					maxEventId = -1;
					return 0;
				}

				maxEventId = currentEventId + INC_AMOUNT - 1;
			}

			return currentEventId;
		} catch (Throwable t) {
			lastReserverSerialFailed = System.currentTimeMillis();
			log.info("[YEON] Fail to get serial", t);
			return 0;
		}
	}

	@Override
	protected void process(Event event) {
		if (event.hasEventProcessOption()) {
			eventApi.retry(event);
		} else {
			eventApi.publish(event);
		}
	}
}
