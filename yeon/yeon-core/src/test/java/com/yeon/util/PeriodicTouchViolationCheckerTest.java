package com.yeon.util;

import junit.framework.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class PeriodicTouchViolationCheckerTest {

	@Test
	public void test_3() throws InterruptedException {
		Thread.interrupted();

		long period = TimeUnit.MILLISECONDS.toMillis(10);
		PeriodicTouchViolationChecker checker = new PeriodicTouchViolationChecker(3, period);
		Assert.assertFalse(checker.isViolated());
		Assert.assertFalse(checker.touch());
		Assert.assertFalse(checker.isViolated());
		Assert.assertFalse(checker.touch());
		Assert.assertFalse(checker.isViolated());
		Assert.assertTrue(checker.touch());
		Assert.assertTrue(checker.isViolated());

		Thread.sleep(period + 1);

		Assert.assertFalse(checker.isViolated());
		Assert.assertFalse(checker.touch());
		Assert.assertFalse(checker.isViolated());
	}

	@Test
	public void test_1() throws InterruptedException {
		Thread.interrupted();

		long period = TimeUnit.MILLISECONDS.toMillis(10);
		PeriodicTouchViolationChecker checker = new PeriodicTouchViolationChecker(1, period);
		Assert.assertFalse(checker.isViolated());
		Assert.assertTrue(checker.touch());
		Assert.assertTrue(checker.isViolated());

		Thread.sleep(period + 1);

		Assert.assertFalse(checker.isViolated());
		Assert.assertTrue(checker.touch());
		Assert.assertTrue(checker.isViolated());
	}
}
