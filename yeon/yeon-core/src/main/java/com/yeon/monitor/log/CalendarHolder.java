package com.yeon.monitor.log;

import java.util.Calendar;

public class CalendarHolder {
	private static ThreadLocal<Calendar> holder = new ThreadLocal<Calendar>() {
		@Override
		protected Calendar initialValue() {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MILLISECOND, 0);

			return calendar;
		}
	};

	public static Calendar get() {
		return holder.get();
	}

	public static void remove() {
		holder.remove();
	}
}
