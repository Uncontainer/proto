package com.yeon.monitor.log;

import ch.qos.logback.classic.Level;
import com.yeon.monitor.common.MomString;

public class ServerLogEntry {
	public static final int SEVERITY_UNDEFINED = 0;
	public static final int SEVERITY_DEBUG = Level.DEBUG_INT;
	public static final int SEVERITY_INFO = Level.INFO_INT;
	public static final int SEVERITY_WARN = Level.WARN_INT;
	public static final int SEVERITY_ERROR = Level.ERROR_INT;

	protected long createTime = -1;
	protected MomString content = new MomString();

	public long getCreateTime() {
		return createTime;
	}

	public MomString getContent() {
		return content;
	}

	public int getSeverity() {
		return 0;
	}

	public static int parseInt(char[] chars, int startIndex, int endIndex, int defaultValue) {
		int value = 0;
		boolean negative = false;
		if (chars[startIndex] == '-') {
			negative = true;
			startIndex++;
		}

		for (; startIndex < endIndex; startIndex++) {
			int digit = chars[startIndex] - '0';
			if (digit < 0 || digit > 9) {
				return defaultValue;
			}

			value = value * 10 + digit;
		}

		return negative ? -value : value;
	}

	public static int parseNotNegativeInt(char[] chars, int startIndex, int endIndex) {
		int value = 0;
		for (; startIndex < endIndex; startIndex++) {
			int digit = chars[startIndex] - '0';
			if (digit < 0 || digit > 9) {
				return -1;
			}

			value = value * 10 + digit;
		}

		return value;
	}

	public static int nextMatch(char[] chars, int offset, char sepChar) {
		for (int i = offset; i < chars.length; i++) {
			if (chars[i] == sepChar) {
				return i;
			}
		}

		return -1;
	}

	public static int nextMatch(char[] chars, int offset, char[] sepChars) {
		for (int i = offset + 1; i < chars.length; i++) {
			for (int j = 0; j < sepChars.length; j++) {
				if (chars[i] == sepChars[j]) {
					return i;
				}
			}
		}

		return -1;
	}
}
