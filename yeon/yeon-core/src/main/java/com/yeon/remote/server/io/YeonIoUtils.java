package com.yeon.remote.server.io;

import com.yeon.util.MapModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class YeonIoUtils {
	public static final byte version = 1;

	public static void writeAndFlush(OutputStream out, Object object) throws IOException {
		write(out, object);
		out.flush();
	}

	public static void write(OutputStream out, Object object) throws IOException {
		SerializationType serializationType;
		if (object instanceof MapModel) {
			serializationType = SerializationType.MAP_MODEL_JAVA_SERIALIZATION;
		} else {
			serializationType = SerializationType.JAVA_SERIALIZATION;
		}

		out.write(version);
		out.write(serializationType.getCode());
		serializationType.getSerializer().serialize(object, out);
	}

	public static Object read(InputStream in) throws IOException, ClassNotFoundException {
		/* int version = */in.read();
		SerializationType serializationType = SerializationType.fromCode(in.read());
		return serializationType.getSerializer().deserizlize(in);
	}
}
