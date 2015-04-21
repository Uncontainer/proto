package com.yeon.util;

import junit.framework.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CollectionJsonMapperTest_MapModel {

	@Test
	public void test_맵에있는내용만직렬화() {
		CollectionJsonMapper mapper = CollectionJsonMapper.getInstance();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dummy", "dummyValue");

		MyMapModel model = new MyMapModel();
		model.setValues(map);
		model.setFiled1("test");

		Assert.assertEquals(map, mapper.toMap(mapper.toJson(model)));
	}

	static class MyMapModel extends MapModel {
		String filed1;

		public String getFiled1() {
			return filed1;
		}

		public void setFiled1(String filed1) {
			this.filed1 = filed1;
		}

	}
}
