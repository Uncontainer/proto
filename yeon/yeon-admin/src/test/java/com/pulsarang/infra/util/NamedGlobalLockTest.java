package com.pulsarang.infra.util;

import com.jeojiri.lang.SpringTestBase;
import com.yeon.infra.util.NamedGlobalLock;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedGlobalLockTest extends SpringTestBase {

	@Autowired
	private NamedGlobalLock lock;

	final String lockName = "test-dummy-lock";

	@Test
	public void test() {
		lock.unlock(lockName);

		boolean locked = lock.tryLock(lockName, 10000);
		Assert.assertTrue(locked);
		locked = lock.tryLock(lockName, 10000);
		Assert.assertFalse(locked);
		lock.unlock(lockName);

		locked = lock.tryLock(lockName, 10000);
		Assert.assertTrue(locked);
	}

	@Test
	public void test_Concurrency() throws InterruptedException {
		lock.unlock(lockName);

		int threadCount = 50;
		CountDownLatch startupLatch = new CountDownLatch(1);
		CountDownLatch finishLatch = new CountDownLatch(threadCount);
		AtomicInteger acquiredCount = new AtomicInteger(0);

		for (int i = 0; i < threadCount; i++) {
			new LockRequester(startupLatch, finishLatch, acquiredCount).start();
		}
		startupLatch.countDown();
		finishLatch.await(5000, TimeUnit.SECONDS);

		Assert.assertEquals(1, acquiredCount.get());
	}

	private class LockRequester extends Thread {
		private final CountDownLatch startupLatch;
		private final CountDownLatch finishLatch;
		private final AtomicInteger acquiredCount;

		public LockRequester(CountDownLatch startupLatch, CountDownLatch finishLatch, AtomicInteger acquiredCount) {
			super();
			this.startupLatch = startupLatch;
			this.finishLatch = finishLatch;
			this.acquiredCount = acquiredCount;
		}

		@Override
		public void run() {
			try {
				startupLatch.await(1000, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

			try {
				if (lock.tryLock(lockName, 100000)) {
					acquiredCount.incrementAndGet();
				}
			} finally {
				finishLatch.countDown();
			}
		}
	}
}
