package com.yeon.remote.server;

import com.yeon.remote.RemoteServiceRequest;
import com.yeon.remote.RemoteServiceResponse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ResponseFuture {
	private final long timestamp;
	private final CountDownLatch latch;
	private RemoteServiceResponse response;

	public ResponseFuture(long key, RemoteServiceRequest request) {
		timestamp = System.currentTimeMillis();
		latch = new CountDownLatch(1);
	}

	public long getTimestamp() {
		return timestamp;
	}

	public RemoteServiceResponse get() throws InterruptedException {
		latch.await();

		return response;
	}

	public RemoteServiceResponse get(long timeout, TimeUnit unit) throws InterruptedException {
		latch.await(timeout, unit);

		return response;
	}

	synchronized void setResponse(RemoteServiceResponse response) {
		if (latch.getCount() == 0L) {
			throw new IllegalStateException("Response has already set.");
		}

		this.response = response;

		latch.countDown();
	}
}
