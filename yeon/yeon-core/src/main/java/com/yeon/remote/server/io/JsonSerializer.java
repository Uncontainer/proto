package com.yeon.remote.server.io;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JsonSerializer implements Serializer {
	ObjectMapper mapper = new ObjectMapper();

	@Override
	public void serialize(Object object, OutputStream out) throws IOException {
		if (object == null) {
			MapModelJavaSerializer.writeClass(out, null);
		} else {
			MapModelJavaSerializer.writeClass(out, object.getClass());
			mapper.writeValue(out, object);
		}
	}

	@Override
	public Object deserizlize(InputStream in) throws ClassNotFoundException, IOException {
		Class<?> clazz = MapModelJavaSerializer.readClass(in);
		if (clazz == null) {
			return null;
		} else {
			return mapper.readValue(in, clazz);
		}
	}
}
