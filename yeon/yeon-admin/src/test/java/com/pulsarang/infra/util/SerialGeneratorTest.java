package com.pulsarang.infra.util;

import com.jeojiri.lang.SpringTestBase;
import com.yeon.infra.util.SerialGenerator;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SerialGeneratorTest extends SpringTestBase {
	@Autowired
	SerialGenerator generator;

	@Test
	public void test() {
		String name = "dummy-1";
		int amount = 10;

		long first = generator.next(name, amount);
		long second = generator.next(name, amount);

		Assert.assertEquals(amount, second - first);
		Assert.assertEquals(second + amount, generator.current(name));
	}
}
