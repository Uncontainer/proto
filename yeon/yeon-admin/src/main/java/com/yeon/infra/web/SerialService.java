package com.yeon.infra.web;

import java.util.concurrent.atomic.AtomicLong;

public class SerialService {
	public static final SerialService INSTANCE = new SerialService();

	public static final SerialService getInstance() {
		return INSTANCE;
	}

	private final AtomicLong seed = new AtomicLong();

	private SerialService() {
	}

	public long next() {
		return seed.getAndIncrement();
	}

	public long current() {
		return seed.get();
	}
}
