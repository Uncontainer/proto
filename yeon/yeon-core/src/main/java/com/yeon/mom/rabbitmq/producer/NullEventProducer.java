package com.yeon.mom.rabbitmq.producer;

import com.yeon.mom.EventProducer;
import com.yeon.mom.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author pulsarang
 */
public class NullEventProducer implements EventProducer {
	private final Logger log = LoggerFactory.getLogger(NullEventProducer.class);

	@Override
	public void publish(Event event) {
		log.debug("[YEON] Discard event.({})", event);
	}

	@Override
	public void publish(Event event, boolean async) {
		log.debug("[YEON] Discard event.({})", event);
	}

	@Override
	public void retry(Event event) {
		log.debug("[YEON] Discard event.({})", event);
	}
}
