package com.pulsarang.infra.config.propinfo.type;

import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

public class TimePropertyValueParser {
	public static long parseTime(String timeString) {
		StringTokenizer st = new StringTokenizer(timeString, ", \t\n");
		long time = 0L;
		while (st.hasMoreTokens()) {
			time += getMillies(st.nextToken());
		}

		return time;
	}

	private static long getMillies(String expr) {
		if (expr == null | expr.isEmpty()) {
			return 0;
		}

		long duration = 0L;
		int index = 0;
		for (; index < expr.length(); index++) {
			if (!Character.isDigit(expr.charAt(index))) {
				break;
			}
			duration = duration * 10 + (expr.charAt(index) - '0');
		}

		if (index == expr.length()) {
			return duration;
		}

		if (index + 1 != expr.length()) {
			throw new IllegalArgumentException("Illegal time expression.(" + expr + ")");
		}

		return toMillis(duration, expr.charAt(index));
	}

	private static long toMillis(long duration, char postfix) {
		switch (postfix) {
		case 'S':
			return duration;
		case 's':
			return TimeUnit.SECONDS.toMillis(duration);
		case 'm':
			return TimeUnit.MINUTES.toMillis(duration);
		case 'h':
			return TimeUnit.HOURS.toMillis(duration);
		case 'd':
			return TimeUnit.DAYS.toMillis(duration);
		case 'w':
			return TimeUnit.DAYS.toMillis(duration * 7);
		case 'M':
			return TimeUnit.DAYS.toMillis(duration * 30);
		case 'y':
			return TimeUnit.DAYS.toMillis(duration * 365);
		}

		throw new IllegalArgumentException("Invalid time postfix: " + postfix);
	}

}
