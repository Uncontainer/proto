package com.yeon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DisposableManager {
	private static final Logger log = LoggerFactory.getLogger(DisposableManager.class);
	
	private final Set<Disposable> disposables = new HashSet<Disposable>();

	public synchronized boolean add(Disposable disposable) {
		if (disposable == null) {
			return false;
		}

		return disposables.add(disposable);
	}

	public void disposeAll() {
		log.info("[YEON] Disposing infra-core...");
		if (disposables.isEmpty()) {
			return;
		}

		final CountDownLatch latch = new CountDownLatch(disposables.size());
		for (final Disposable disposable : disposables) {
			new Thread() {
				@Override
				public void run() {
					try {
						log.info("[YEON] Disposing '{}'...", disposable.getClass().getCanonicalName());
						disposable.dispose();
						log.info("[YEON] Disposed Success '{}'", disposable.getClass().getCanonicalName());
					} catch (Exception e) {
						log.warn("[YEON] Fail to dispose.", e);
					} finally {
						latch.countDown();
					}
				}
			}.start();
		}

		boolean interrupted = Thread.currentThread().isInterrupted();
		if (interrupted) {
			Thread.interrupted();
		}

		try {
			if (latch.await(3, TimeUnit.SECONDS)) {
				log.info("[YEON] Dispose infra-core completed.");
			} else {
				log.warn("[YEON] Dispose infra-core incomplete. Some threads are running.");
			}
		} catch (InterruptedException e) {
			log.info("[YEON] Wait for dispose all fail.", e);
			interrupted = true;
		}

		if (interrupted) {
			Thread.currentThread().interrupt();
		}
	}
}
