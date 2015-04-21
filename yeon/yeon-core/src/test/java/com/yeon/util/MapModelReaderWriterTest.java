package com.yeon.util;

import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class MapModelReaderWriterTest {
	@Test
	public void listTest() throws IOException {
		MapModel model = new MapModel();
		model.setValue("test", 1);
		List<MapModel> list = new ArrayList<MapModel>();
		list.add(model);

		StringWriter writer = new StringWriter();
		new MapModelWriter<MapModel>(writer).write(list);

		List<MapModel> readList = new MapModelReader<MapModel>(writer.getBuffer().toString(), MapModel.class).list();
		Assert.assertEquals(list, readList);
	}

	@Test
	public void itemTest() throws IOException {
		MapModel model = new MapModel();
		model.setValue("test", 1);

		StringWriter writer = new StringWriter();
		new MapModelWriter<MapModel>(writer).write(model);

		MapModel readModel = new MapModelReader<MapModel>(writer.getBuffer().toString(), MapModel.class).get();
		Assert.assertEquals(model, readModel);
	}
}
