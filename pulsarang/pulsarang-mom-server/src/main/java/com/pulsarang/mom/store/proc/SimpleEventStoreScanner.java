package com.pulsarang.mom.store.proc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.mom.store.EventEnvelope;
import com.pulsarang.mom.store.EventStore;

public abstract class SimpleEventStoreScanner implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(SimpleEventStoreScanner.class);

	String name;
	EventStore store;

	Thread thread;
	boolean stop;

	public SimpleEventStoreScanner(String name, EventStore store) {
		this.name = name;
		this.store = store;

		stop = false;
		thread = new Thread(this);
		thread.start();
	}

	public void dispose() {
		if (stop) {
			return;
		}

		thread.interrupt();
		stop = true;
	}

	public void push(Event event) {
		store.insert(event);
	}

	@Override
	public void run() {
		// 반드시 RUNNING상태의 event를 모두 소진한 이후 WAIT 상태의 event를 처리하는 프로세스로 넘어가야 한다.
		// 그렇지 않을 경우, 현재 처리중인 event가 중복 처리된다.
		processRunningEvent();

		while (true) {
			if (stop) {
				return;
			}

			try {
				List<EventEnvelope> eventEnvelopes = store.extracts(EventStore.STATUS_WAIT, 10);
				if (eventEnvelopes.isEmpty()) {
					// TODO congestion 제거.
					continue;
				}

				for (EventEnvelope eventEnvelope : eventEnvelopes) {
					process(eventEnvelope, false);
				}
			} catch (Exception e) {
				log.error("[MOM] Fail to process item.", e);
			} finally {
				// TODO process()에서 ack를 주지 않았을 경우에 대한 처리 추가.
			}
		}
	}

	private void processRunningEvent() {
		List<EventEnvelope> eventEnvelopes = store.extracts(EventStore.STATUS_PROCESS, 10);
		while (!eventEnvelopes.isEmpty()) {
			// TODO EventEnvelope에 verify 상태 코드를 저장하고 EventExecutor에서 판단하도록 수정.
			for (EventEnvelope eventEnvelope : eventEnvelopes) {
				process(eventEnvelope, true);
			}

			eventEnvelopes = store.extracts(EventStore.STATUS_PROCESS, 10);
		}
	}

	protected abstract void process(EventEnvelope eventEnvelope, boolean verify);
}
