package com.pulsarang.mom.store.db.h2;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Assert;
import org.junit.Test;

import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.infra.mom.event.SimpleEventType;
import com.pulsarang.mom.store.EventEnvelope;
import com.pulsarang.mom.store.EventStore;

public class H2EventStoreTest {
	@Test
	public void queueTest() throws InterruptedException {
		H2EventStore h2EventStore = new H2EventStore("dummy");
		h2EventStore.deleteAll();
		crudTest(h2EventStore);
	}

	@Test
	public void categoryQueueTest() throws InterruptedException {
		int threadCount = 10;
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch endLatch = new CountDownLatch(threadCount);
		for (int i = 0; i < threadCount; i++) {
			H2EventStore h2EventStore = new H2EventStore("dummy2", "category_" + i);
			new Runner(h2EventStore, startLatch, endLatch).start();
		}

		startLatch.countDown();
		endLatch.await();
	}

	private static class Runner extends Thread {
		H2EventStore h2EventStore;
		CountDownLatch startLatch;
		CountDownLatch endLatch;

		public Runner(H2EventStore h2EventStore, CountDownLatch startLatch, CountDownLatch endLatch) {
			super();
			this.h2EventStore = h2EventStore;
			this.startLatch = startLatch;
			this.endLatch = endLatch;
		}

		@Override
		public void run() {
			try {
				startLatch.wait();
				crudTest(h2EventStore);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} finally {
				endLatch.countDown();
			}
		}
	}

	private static void crudTest(H2EventStore h2EventStore) {
		Event event = new Event(new SimpleEventType("dummy", "dummy"));
		event.setString("message", "xxx");

		// 빈 store는 빈 리스트를 돌려준다.
		List<EventEnvelope> waitEventEnvelopes = h2EventStore.extracts(EventStore.STATUS_WAIT, 10);
		List<EventEnvelope> processEventEnvelopes = h2EventStore.extracts(EventStore.STATUS_PROCESS, 10);
		Assert.assertEquals(0, waitEventEnvelopes.size());
		Assert.assertEquals(0, processEventEnvelopes.size());

		// event를 넣으면 대기중인 공간으로 들어간다.
		h2EventStore.insert(event);
		waitEventEnvelopes = h2EventStore.extracts(EventStore.STATUS_WAIT, 10);
		processEventEnvelopes = h2EventStore.extracts(EventStore.STATUS_PROCESS, 10);
		Assert.assertEquals(1, waitEventEnvelopes.size());
		Assert.assertEquals(0, processEventEnvelopes.size());

		// event를 marking을 하기 전에는 상태의 변화가 없다.
		EventEnvelope eventEnvelope = waitEventEnvelopes.get(0);
		Assert.assertEquals(event, eventEnvelope.getEvent());
		waitEventEnvelopes = h2EventStore.extracts(EventStore.STATUS_WAIT, 10);
		processEventEnvelopes = h2EventStore.extracts(EventStore.STATUS_PROCESS, 10);
		Assert.assertEquals(1, waitEventEnvelopes.size());
		Assert.assertEquals(0, processEventEnvelopes.size());

		// marking 이후에는 처리중인 공간으로 들어간다.
		eventEnvelope.mark();
		waitEventEnvelopes = h2EventStore.extracts(EventStore.STATUS_WAIT, 10);
		processEventEnvelopes = h2EventStore.extracts(EventStore.STATUS_PROCESS, 10);
		Assert.assertEquals(0, waitEventEnvelopes.size());
		Assert.assertEquals(1, processEventEnvelopes.size());

		// ack 이후에는 두 공간 모두에서 삭제된다.
		eventEnvelope.acknowlege();
		waitEventEnvelopes = h2EventStore.extracts(EventStore.STATUS_WAIT, 10);
		processEventEnvelopes = h2EventStore.extracts(EventStore.STATUS_PROCESS, 10);
		Assert.assertEquals(0, waitEventEnvelopes.size());
		Assert.assertEquals(0, processEventEnvelopes.size());
	}
}
