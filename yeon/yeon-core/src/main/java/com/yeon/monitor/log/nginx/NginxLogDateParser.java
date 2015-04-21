package com.yeon.monitor.log.nginx;

import com.yeon.monitor.log.CalendarHolder;
import com.yeon.monitor.log.ServerLogEntry;

import java.util.Calendar;

public class NginxLogDateParser {
	private static final char[] SEPERATORS = new char[] {'/', ' ', ':'};
	private static final int[] DATE_FIELDS = new int[] {Calendar.DAY_OF_MONTH, Calendar.MONTH, Calendar.YEAR, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND};

	// 19/Apr/2012:11:02:01 +0900
	public static long parseDate(char[] charArray, int startIndex) {
		if (charArray == null || charArray.length - startIndex < 26) {
			return -1;
		}

		Calendar calendar = CalendarHolder.get();

		int dateFieldindex = 0;
		while (dateFieldindex < DATE_FIELDS.length) {
			int endIndex = ServerLogEntry.nextMatch(charArray, startIndex, SEPERATORS);
			if (endIndex == -1) {
				return -1;
			}

			int dateField;
			try {
				dateField = DATE_FIELDS[dateFieldindex++];
			} catch (IndexOutOfBoundsException e) {
				return -1;
			}

			int value;
			if (dateField == Calendar.MONTH) {
				value = getMonthIndex(charArray, startIndex);
			} else {
				value = ServerLogEntry.parseNotNegativeInt(charArray, startIndex, endIndex);
			}

			if (value == -1) {
				return -1;
			}

			startIndex = endIndex + 1;

			calendar.set(dateField, value);
		}

		return calendar.getTimeInMillis();
	}

	private static int getMonthIndex(char[] charArray, int offset) {
		switch (charArray[offset]) {
			case 'J':
				if (charArray[offset + 1] == 'a') {
					return 0; // Jan
				} else if (charArray[offset + 2] == 'n') {
					return 5; // Jun
				} else {
					return 6; // Jul
				}
			case 'F':
				return 1; // FEB
			case 'M':
				if (charArray[offset + 2] == 'r') {
					return 2; // Mar
				} else {
					return 4; // May
				}
			case 'A':
				if (charArray[offset + 1] == 'p') {
					return 3; // Apr
				} else {
					return 7; // Aug
				}
			case 'S':
				return 8; // Sep
			case 'O':
				return 9; // Oct
			case 'N':
				return 10; // Nov
			case 'D':
				return 11; // Dec
			default:
				return -1;
		}
	}
}
