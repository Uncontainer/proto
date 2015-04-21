package com.pulsarang.infra.config.propinfo.type;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class DatePropertyValueParser {
	public static Date parseDate(String dateString) {
		if (StringUtils.isEmpty(dateString)) {
			throw new IllegalArgumentException();
		}

		if (NumberUtils.isNumber(dateString)) {
			return new Date(Long.parseLong(dateString));
		}

		return PARSER_HOLDER.get().parse(dateString);
	}

	private static final ThreadLocal<DateParser> PARSER_HOLDER = new ThreadLocal<DateParser>() {
		@Override
		protected DateParser initialValue() {
			return new DateParser();
		}
	};

	private static class DateParser {
		Calendar calendar = Calendar.getInstance();

		public DateParser() {
			calendar = Calendar.getInstance();
		}

		public Date parse(String dateString) {
			String[] dateStrings = dateString.split(" |T");
			String[] dates = dateStrings[0].split("-");
			if (dates.length != 3) {
				throw new IllegalArgumentException("Invalid date format.(" + dateString + ")");
			}

			int year = Integer.parseInt(dates[0]);
			int month = Integer.parseInt(dates[1]);
			int day = Integer.parseInt(dates[2]);

			int hourOfDay = 0;
			int minute = 0;
			int second = 0;
			int millies = 0;

			if (dateStrings.length > 1) {
				String[] times = dateStrings[1].split(":");
				if (times.length < 1 || times.length > 3) {
					throw new IllegalArgumentException("Invalid date format.(" + dateString + ")");
				}

				if (times.length > 0) {
					hourOfDay = Integer.parseInt(times[0]);
				}
				if (times.length > 1) {
					minute = Integer.parseInt(times[1]);
				}
				if (times.length > 2) {
					if (times[2].contains(".")) {
						String[] tokens = times[2].split("\\.");
						second = Integer.parseInt(tokens[0]);
						millies = Integer.parseInt(tokens[1]) % 1000;
					} else {
						second = Integer.parseInt(times[2]);
					}
				}
			}

			calendar.setTimeInMillis(millies);
			calendar.set(year, month - 1, day, hourOfDay, minute, second);
			return calendar.getTime();
		}
	}
}
