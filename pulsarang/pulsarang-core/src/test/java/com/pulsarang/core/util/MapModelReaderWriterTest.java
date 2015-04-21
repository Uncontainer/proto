package com.pulsarang.core.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class MapModelReaderWriterTest {
	@Test
	public void listTest() throws IOException {
		MapModel model = new MapModel();
		model.setProperty("test", 1);
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
		model.setProperty("test", 1);

		StringWriter writer = new StringWriter();
		new MapModelWriter<MapModel>(writer).write(model);

		MapModel readModel = new MapModelReader<MapModel>(writer.getBuffer().toString(), MapModel.class).get();
		Assert.assertEquals(model, readModel);
	}
}
