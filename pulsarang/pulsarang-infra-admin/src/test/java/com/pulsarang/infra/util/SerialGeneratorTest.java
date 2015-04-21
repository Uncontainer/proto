package com.pulsarang.infra.util;

import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pulsarang.test.spring.SpringTestBase;

public class SerialGeneratorTest extends SpringTestBase {
	@Autowired
	SerialGenerator generator;

	@Test
	public void test() throws SQLException {
		String name = "dummy-1";
		int amount = 10;

		long first = generator.next(name, amount);
		long second = generator.next(name, amount);

		Assert.assertEquals(amount, second - first);
		Assert.assertEquals(second + amount, generator.current(name));
	}
}
