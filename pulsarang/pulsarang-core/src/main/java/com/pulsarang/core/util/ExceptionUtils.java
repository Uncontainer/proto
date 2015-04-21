package com.pulsarang.core.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {
	public static String toString(Throwable t) {
		if (t == null) {
			return null;
		}

		StringWriter writer = new StringWriter();
		t.printStackTrace(new PrintWriter(writer));
		return writer.getBuffer().toString();
	}
}
