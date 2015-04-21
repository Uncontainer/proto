package com.yeon.util;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Date;

public class InstanceTest {

	@Test
	public void beanTest() {
		MapModel model = new MapModel();
		Assert.assertEquals(0, model.getValues().size());

		model.setValues(null);
		Assert.assertEquals(0, model.getValues().size());

		model.setValue("a", 1);
		Assert.assertEquals(1, model.getValue("a"));

		// 날짜는 내부적으로 long으로 저장한다.
		Date date = new Date();
		model.setDate("d", date);
		Assert.assertEquals(date.getTime(), model.getValue("d"));
		Assert.assertEquals(date, model.getDate("d"));
	}
}
