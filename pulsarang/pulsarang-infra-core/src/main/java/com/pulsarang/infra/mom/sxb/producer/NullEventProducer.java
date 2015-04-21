package com.pulsarang.infra.mom.sxb.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.infra.mom.EventProducer;
import com.pulsarang.infra.mom.event.Event;

public class NullEventProducer implements EventProducer {
	private final Logger log = LoggerFactory.getLogger(NullEventProducer.class);

	@Override
	public void publish(Event event) {
		log.debug("[MOM] Discard event.({})", event);
	}

	@Override
	public void publish(Event event, boolean async) {
		log.debug("[MOM] Discard event.({})", event);
	}

	@Override
	public void retry(Event event) {
		log.debug("[MOM] Discard event.({})", event);
	}
}
