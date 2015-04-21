package com.yeon.remote.server.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Serializer {
	void serialize(Object object, OutputStream out) throws IOException;

	Object deserizlize(InputStream in) throws ClassNotFoundException, IOException;
}
