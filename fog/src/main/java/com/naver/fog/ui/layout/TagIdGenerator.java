package com.naver.fog.ui.layout;

import java.util.concurrent.atomic.AtomicLong;

public class TagIdGenerator {
	private static AtomicLong seed = new AtomicLong();

	public static String nextId(String prefix) {
		return prefix + "_" + seed.incrementAndGet();
	}
}
