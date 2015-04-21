package com.yeon;

public class YeonContextFactory {
	public synchronized static void init(YeonConfiguration yeonConfiguration) {
		if (YeonContext.yeonContext != null) {
			throw new IllegalStateException("YeonContext has already initialized.");
		}

		YeonContext.yeonContext = new YeonContext(yeonConfiguration);
	}
}
