package com.pulsarang.infra.util;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.infra.Disposable;

public abstract class SimpleScheduler implements Runnable, Disposable {
	private final Logger log = LoggerFactory.getLogger(SimpleScheduler.class);

	private final ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(2);
	private boolean running = false;

	protected Job job;
	protected int delay = 10;
	protected int interval = (int) TimeUnit.SECONDS.toMillis(30);

	public SimpleScheduler(Job job, int interval, int delay) {
		this.job = job;
		this.interval = interval;
		this.delay = delay;
	}

	public void start() {
		scheduleNext();
	}

	@Override
	public void dispose() throws Exception {
		scheduler.shutdownNow();
	}

	@Override
	public void run() {
		synchronized (this) {
			// 이전 요청이 아직 수행되고 있다면 작업을 진행하지 않는다.
			if (running) {
				log.debug("[MOM] Skip collecting monitoring infomation.");
				return;
			}

			running = true;
		}

		try {
			long now = System.currentTimeMillis();
			long startTime = now - (now % interval);

			job.execute(startTime);
		} catch (Throwable t) {
			log.info("[MOM] Fail to collect system resource monitoring data.", t);
		} finally {
			synchronized (this) {
				running = false;
			}

			scheduleNext();
		}
	}

	private void scheduleNext() {
		long now = System.currentTimeMillis();
		long elapsedTime = now % interval;
		long sleepTime = interval - elapsedTime + delay;
		scheduler.schedule(this, sleepTime, TimeUnit.MILLISECONDS);
	}

	public interface Job {
		void execute(long startTime);
	}
}
