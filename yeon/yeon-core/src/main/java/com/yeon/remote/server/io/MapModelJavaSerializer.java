package com.yeon.remote.server.io;

import com.yeon.util.MapModel;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class MapModelJavaSerializer extends JavaSerializer {
	@Override
	public void serialize(Object object, OutputStream out) throws IOException {
		if (object instanceof MapModel) {
			writeClass(out, object.getClass());
			super.serialize(((MapModel) object).getValues(), out);
		} else if (object == null) {
			writeClass(out, null);
		} else {
			throw new IllegalArgumentException("Object must be a instance of MapModel.(" + object.getClass() + ")");
		}
	}

	@Override
	public Object deserizlize(InputStream in) throws ClassNotFoundException, IOException {
		Class<?> clazz = readClass(in);
		if (clazz == null) {
			return null;
		}

		Object object = super.deserizlize(in);

		if (object instanceof Map) {
			MapModel model;
			try {
				// TODO 기본 생성자가 없을 경우에 대한 처리 추가...
				model = (MapModel) clazz.newInstance();
			} catch (InstantiationException e) {
				// TODO 예외 처리 정교화
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}

			model.setValues((Map) object);
			return model;
		} else {
			throw new IllegalArgumentException("Object must be a instance of Map.(" + object.getClass() + ")");
		}
	}

	public static void writeClass(OutputStream out, Class<?> clazz) throws IOException {
		if (clazz != null) {
			byte[] bytes = clazz.getCanonicalName().getBytes("UTF8");
			writeShort(out, bytes.length);
		} else {
			writeShort(out, -1);
		}
	}

	public static Class<?> readClass(InputStream in) throws IOException, ClassNotFoundException {
		short length = readShort(in);
		String className = null;
		if (length > 0) {
			byte bytes[] = new byte[length];
			in.read(bytes);
			className = new String(bytes, "UTF8");

			return Class.forName(className);
		} else {
			return null;
		}
	}

	public static final void writeShort(OutputStream out, int v) throws IOException {
		out.write((v >>> 8) & 0xFF);
		out.write((v >>> 0) & 0xFF);
	}

	public static final short readShort(InputStream in) throws IOException {
		int ch1 = in.read();
		int ch2 = in.read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (short) ((ch1 << 8) + (ch2 << 0));
	}
}
