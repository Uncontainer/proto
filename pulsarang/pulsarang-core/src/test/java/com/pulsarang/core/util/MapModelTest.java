package com.pulsarang.core.util;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

public class MapModelTest {

	@Test
	public void beanTest() {
		MapModel model = new MapModel();
		Assert.assertEquals(0, model.getProperties().size());

		model.setProperties(null);
		Assert.assertEquals(0, model.getProperties().size());

		model.setProperty("a", 1);
		Assert.assertEquals(1, model.getProperty("a"));

		// 날짜는 내부적으로 long으로 저장한다.
		Date date = new Date();
		model.setDate("d", date);
		Assert.assertEquals(date.getTime(), model.getProperty("d"));
		Assert.assertEquals(date, model.getDate("d"));
	}
}
