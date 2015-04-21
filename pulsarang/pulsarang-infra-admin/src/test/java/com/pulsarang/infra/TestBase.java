package com.pulsarang.infra;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestBase extends Assert {
	protected Logger log = LoggerFactory.getLogger(getClass());

	static {
		if (System.getProperty("env") == null) { // CI 서버에서는 env=ci
			System.setProperty("env", "test");
		}
	}

	@Before
	public void mockup() {
		MockitoAnnotations.initMocks(this);
	}

	public static void assertDateEquals(Date expected, Date actual) {
		if (null == expected && actual == null) {
			return;
		} else if (null != expected && null != actual && Math.abs(expected.getTime() - actual.getTime()) < 1000) {
			return;
		} else {
			fail("expected[" + expected + "] != actual[" + actual + "]");
		}
	}
}