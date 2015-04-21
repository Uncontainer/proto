package com.naver.mage4j.php.echo;

public class EchoBuffer {
	static ThreadLocal<StringBuffer> HOLDER = new ThreadLocal<StringBuffer>();

	public static void append(String content) {
		StringBuffer buffer = HOLDER.get();
		if (buffer == null) {
			buffer = new StringBuffer();
			HOLDER.set(buffer);
		}

		buffer.append(content);
	}

	public static String getAndClean() {
		StringBuffer buffer = HOLDER.get();
		if (buffer == null) {
			return null;
		}

		String result = buffer.toString();
		HOLDER.remove();

		return result;
	}

	public static void init() {
		HOLDER.set(new StringBuffer());
	}
}
