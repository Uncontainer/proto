package com.pulsarang.infra.mom.sxb.producer;

import java.io.IOException;

import com.pulsarang.infra.mom.EventProducer;
import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.infra.mom.receiver.EventPushResult;
import com.pulsarang.infra.mom.receiver.EventReceiverRemoteServiceInvoker;
import com.pulsarang.infra.server.Server;

public class SxbEventProducer implements EventProducer {
	final Sender[] senders;

	public SxbEventProducer() {
		senders = new Sender[100];
	}

	@Override
	public void publish(Event event) {
		publish(event, false);
	}

	@Override
	public void publish(Event event, boolean async) {
		Sender producer = getSender(event);
		try {
			producer.send(event);
		} catch (IOException e) {
			throw new RepublishException(e);
		}
	}

	@Override
	public void retry(Event event) {
		throw new UnsupportedOperationException();
	}

	private Sender getSender(Event event) {
		int bucketNo = EventServerSelector.getBucketNo(event);

		synchronized (this) {
			if (senders[bucketNo] == null) {
				senders[bucketNo] = new Sender(bucketNo);
			}
		}

		return senders[bucketNo];
	}

	// TODO MQ 서버가 죽었을 경우 속도를 조절하는 로직 추가 필요.
	class Sender {
		final int bucketNo;
		EventReceiverRemoteServiceInvoker invoker;

		public Sender(int bucketNo) {
			this.bucketNo = bucketNo;
		}

		public void send(Event event) throws IOException {
			if (invoker == null) {
				Server server = EventServerSelector.getInstance().select(event, true);
				invoker = new EventReceiverRemoteServiceInvoker(server);
			}

			EventPushResult result = invoker.push(event);
			if (result.isRedirected()) {
				EventServerSelector.getInstance().changeServer(bucketNo, result.getServerIp());
				invoker = null;
			}
		}
	}
}
