package com.yeon.remote.server.io;

import java.io.*;

public class JavaSerializer implements Serializer {
	@Override
	public void serialize(Object object, OutputStream out) throws IOException {
		if (!(out instanceof ObjectOutputStream)) {
			throw new IllegalArgumentException("OutputStream must be a ObjectOutputStream.");
		}

		((ObjectOutputStream) out).writeObject(object);
	}

	@Override
	public Object deserizlize(InputStream in) throws ClassNotFoundException, IOException {
		if (!(in instanceof ObjectInputStream)) {
			throw new IllegalArgumentException("InputStream must be a ObjectInputStream.");
		}

		return ((ObjectInputStream) in).readObject();
	}
}
