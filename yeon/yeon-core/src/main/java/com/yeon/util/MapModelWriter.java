package com.yeon.util;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

public class MapModelWriter<T extends MapModel> {
	Writer writer;

	public MapModelWriter(Writer writer) {
		this.writer = writer;
	}

	public void write(List<T> list) throws IOException {
		if (list == null || list.isEmpty()) {
			writer.append("[]");
			return;
		}

		Iterator<T> iter = list.iterator();
		writer.append("[");
		while (iter.hasNext()) {
			this.write(iter.next());
			if (iter.hasNext()) {
				writer.append(",");
			}
		}
		writer.append("]");
	}

	public void write(T model) throws IOException {
		if (model == null) {
			writer.write("{}");
			return;
		}

		writer.write(CollectionJsonMapper.getInstance().toJson(model.getValues()));
	}

	public void close() throws IOException {
		writer.close();
	}
}
