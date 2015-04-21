package com.pulsarang.mom.queue.proc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.mom.queue.EventQueue;
import com.pulsarang.mom.queue.EventQueueFactory;
import com.pulsarang.mom.store.EventEnvelope;

public abstract class EventQueueFetcher implements Runnable {
	private final Logger log = LoggerFactory.getLogger(EventQueueFetcher.class);

	String name;
	EventQueue queue;

	Thread thread;
	boolean stop = true;

	public EventQueueFetcher(String name, boolean persist) {
		this.name = name;
		this.queue = EventQueueFactory.getQueue(name, persist);
	}

	public String getName() {
		return name;
	}

	public void start() {
		if (thread != null) {
			throw new IllegalStateException("Fetcher has alreday started.");
		}

		stop = false;
		thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		if (stop) {
			return;
		}

		thread.interrupt();
		stop = true;
	}

	public void push(Event event) throws InterruptedException {
		queue.push(event);
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
				EventEnvelope item = queue.pop();
				if (item == null) {
					continue;
				}

				process(item, false);
			} catch (InterruptedException e) {
				log.info("EventQueueFetcher interruped.({})", name);
				break;
			} catch (Exception e) {
				log.error("[MOM] Fail to process item.", e);
			} finally {
				// TODO process()에서 ack를 주지 않았을 경우에 대한 처리 추가.
			}
		}
	}

	private void processRunningEvent() {
		EventEnvelope eventEnvelope = queue.popRunningEvent();
		while ((eventEnvelope = queue.popRunningEvent()) != null) {
			try {
				// TODO EventEnvelope에 verify 상태 코드를 저장하고 EventExecutor에서 판단하도록 수정.
				process(eventEnvelope, true);
			} catch (InterruptedException e) {
				log.info("EventQueueFetcher interruped.({})", name);
				break;
			} catch (Exception e) {
				log.error("[MOM] Fail to process item.", e);
			}
		}
	}

	public EventQueue getEventQueue() {
		return queue;
	}

	protected abstract void process(EventEnvelope item, boolean verify) throws Exception;
}
