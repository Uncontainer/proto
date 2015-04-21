package com.pulsarang.mom.receiver;

import java.util.Date;

import org.apache.commons.httpclient.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.infra.mom.receiver.EventPushResult;
import com.pulsarang.infra.mom.receiver.EventReceiver;
import com.pulsarang.infra.mom.receiver.EventReceiverRemoteServiceInvoker;
import com.pulsarang.infra.mom.sxb.producer.EventServerSelector;
import com.pulsarang.infra.server.Server;
import com.pulsarang.mom.dispatcher.router.TableEventScanner;

/**
 * 
 * @author pulsarang
 */
@Component
public class EventReceiverImpl implements EventReceiver {
	private final Logger log = LoggerFactory.getLogger(EventReceiverImpl.class);

	@Autowired
	private BucketAllocator bucketAllocator;

	@Autowired
	private TableEventScannerSelector scannerSelector;

	private EventServerSelector eventServerSelector = EventServerSelector.getInstance();

	@Override
	public EventPushResult push(Event event) {
		log.debug("[RECEIVE EVENT] {}", event);

		if (event.getEventDate() == null) {
			event.setEventDate(new Date());
		}

		Server server = eventServerSelector.select(event, false);
		if (server == null) {
			server = bucketAllocator.allocateServer(EventServerSelector.getBucketNo(event));
		}

		EventPushResult eventPushResult = new EventPushResult();
		eventPushResult.setServerIp(server.getIp());

		try {
			if (server.isLocal()) {
				TableEventScanner tableScanner = scannerSelector.select(event);
				tableScanner.push(event);
				eventPushResult.setResponseCode(HttpStatus.SC_OK);
			} else {
				// TODO redirected-count를 기록하여 동기화 이상으로 인한 무한 루프 문제 해결.
				EventReceiverRemoteServiceInvoker invoker = new EventReceiverRemoteServiceInvoker(server);
				EventPushResult pushResult = invoker.push(event);

				if (pushResult.isRedirected()) {
					bucketAllocator.checkSync(EventServerSelector.getBucketNo(event));
					// TODO BucketAllocator 자체 내에서 해결할 지 결정...
					eventServerSelector.changeServer(EventServerSelector.getBucketNo(event), pushResult.getServerIp());
					eventPushResult.setServerIp(pushResult.getServerIp());
				}

				eventPushResult.setResponseCode(HttpStatus.SC_MOVED_PERMANENTLY);
			}
		} catch (Exception e) {
			eventPushResult.setResponseCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			log.error("Fail to store event.", e);
		}

		return eventPushResult;
	}
}
