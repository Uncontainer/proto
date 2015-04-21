package com.yeon.monitor.log.tomcat;

import com.yeon.monitor.log.CalendarHolder;
import com.yeon.monitor.log.ServerLogEntry;

import java.util.Calendar;

public class TomcatLogDateParser {
	private static final char[] SEPERATORS = new char[] {'-', ' ', ':'};
	private static final int[] DATE_FIELDS = {Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND};

	public static long parseDate(char[] charArray, int offset) {
		if (charArray == null || charArray.length - offset < 19) {
			return -1;
		}

		Calendar calendar = CalendarHolder.get();

		int dateFieldindex = 0;
		while (dateFieldindex < DATE_FIELDS.length) {
			int endIndex = ServerLogEntry.nextMatch(charArray, offset, SEPERATORS);
			if (endIndex == -1/* || endIndex > 19*/) {
				return -1;
			}

			int value = ServerLogEntry.parseNotNegativeInt(charArray, offset, endIndex);
			if (value == -1) {
				return -1;
			}
			offset = endIndex + 1;

			int dateField;
			try {
				dateField = DATE_FIELDS[dateFieldindex++];
			} catch (IndexOutOfBoundsException e) {
				return -1;
			}

			if (dateField == Calendar.MONTH) {
				value = value - 1;
			}

			calendar.set(dateField, value);
		}

		return calendar.getTimeInMillis();
	}
}
