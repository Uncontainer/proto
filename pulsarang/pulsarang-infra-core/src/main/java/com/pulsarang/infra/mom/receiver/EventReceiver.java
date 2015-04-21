package com.pulsarang.infra.mom.receiver;

import com.pulsarang.infra.mom.event.Event;

public interface EventReceiver {
	EventPushResult push(Event event);
}
