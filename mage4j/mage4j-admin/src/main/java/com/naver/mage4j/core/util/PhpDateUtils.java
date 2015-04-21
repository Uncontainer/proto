/*
 * @(#)DateUtil.java 2012. 3. 15
 *
 * Copyright 2011 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.naver.mage4j.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.joda.time.DateMidnight;

/**
 * @author EC셀러서비스개발팀
 */
public class PhpDateUtils {
	private static final FastDateFormat DEFAULT_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

	public static Date getFirstDayOfMinusMonth(Date date) {
		return new DateMidnight(date).minusMonths(1).withDayOfMonth(1).toDate();
	}

	public static Date getLastDayOfMinusMonth(Date date) {
		return new DateMidnight(date).withDayOfMonth(1).minusDays(1).toDate();
	}

	public static Date getFirstDayOfMinusMonth(String yyyyMM) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		Date date = dateFormat.parse(yyyyMM);

		return new DateMidnight(date).minusMonths(1).withDayOfMonth(1).toDate();
	}

	public static Date getLastDayOfMinusMonth(String yyyyMM) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		Date date = dateFormat.parse(yyyyMM);

		return new DateMidnight(date).withDayOfMonth(1).minusDays(1).toDate();
	}

	public static Calendar CalendarFromString(String date, String format)
	{
		Calendar cal = Calendar.getInstance();

		try
		{
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			cal.setTime(formatter.parse(date));
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return cal;
	}

	public static String formatDate(Date date, String pattern) {
		if (date == null) {
			return null;
		}

		return FastDateFormat.getInstance(pattern).format(date);
	}

	public static String formatDate(Date date) {
		if (date == null) {
			return null;
		}

		return DEFAULT_DATE_FORMAT.format(date);
	}

	public static Date parseDate(String dateString, boolean reverseInterpretDate) {
		if (StringUtils.isBlank(dateString)) {
			return null;
		}

		if (dateString.equals("0")) {
			return new DateMidnight().toDate();
		} else if (dateString.startsWith("-")) {
			return new DateMidnight().minusDays(Integer.parseInt(dateString.substring(1))).toDate();
		} else if (dateString.startsWith("+")) {
			return new DateMidnight().plusDays(Integer.parseInt(dateString.substring(1))).toDate();
		}

		Calendar calendar = Calendar.getInstance();
		String[] dateStrings = dateString.trim().split(" |T");
		if (dateStrings.length == 1) {
			if (dateString.contains(":")) {
				setTime(calendar, dateStrings[0]);
			} else {
				setDate(calendar, dateStrings[0], reverseInterpretDate);
				clearTime(calendar);
			}
		} else if (dateStrings.length == 2) {
			setDate(calendar, dateStrings[0], reverseInterpretDate);
			setTime(calendar, dateStrings[1]);
		} else {
			throw new IllegalArgumentException();
		}

		return calendar.getTime();
	}

	public static long parseAndGetTime(String dateString) {
		return parseDate(dateString, false).getTime();
	}

	private static void setDate(Calendar calendar, String dateString, boolean reverseInterpret) {
		if (reverseInterpret) {
			setDateByReverseInterpret(calendar, dateString);
		} else {
			setDateByForwardInterpret(calendar, dateString);
		}
	}

	private static void setDateByReverseInterpret(Calendar calendar, String dateString) {
		String[] strDateFieldValues = dateString.split("-|/|\\.");

		switch (strDateFieldValues.length) {
			case 3:
				calendar.set(Calendar.YEAR, Integer.parseInt(strDateFieldValues[0]));
				calendar.set(Calendar.MONTH, Integer.parseInt(strDateFieldValues[1]) - 1);
				calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strDateFieldValues[2]));
				return;
			case 2:
				calendar.set(Calendar.MONTH, Integer.parseInt(strDateFieldValues[0]) - 1);
				calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strDateFieldValues[1]));
				return;
			case 1:
				// 구분자 없이 이어쓰기한 날짜 형식을 처리한다.
				switch (dateString.length()) {
					case 8:
						calendar.set(Calendar.YEAR, Integer.parseInt(dateString.substring(0, 4)));
						calendar.set(Calendar.MONTH, Integer.parseInt(dateString.substring(4, 6)) - 1);
						calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateString.substring(6)));
						return;
					case 6:
						calendar.set(Calendar.YEAR, 2000 + Integer.parseInt(dateString.substring(0, 2)));
						calendar.set(Calendar.MONTH, Integer.parseInt(dateString.substring(2, 4)) - 1);
						calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateString.substring(4)));
						return;
					case 4:
						calendar.set(Calendar.MONTH, Integer.parseInt(dateString.substring(0, 2)) - 1);
						calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateString.substring(2)));
						return;
					case 3:
						calendar.set(Calendar.MONTH, Integer.parseInt(dateString.substring(0, 1)) - 1);
						calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateString.substring(1)));
						return;
					case 2:
					case 1:
						calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateString));
						return;
				}
				break;
		}
		throw new IllegalArgumentException("Invalid date format.(" + dateString + ")");
	}

	private static void setDateByForwardInterpret(Calendar calendar, String dateString) {
		String[] strDateFieldValues = dateString.split("-|/|\\.");

		int year;
		int month = 1;
		int day = 1;

		switch (strDateFieldValues.length) {
			case 3:
				year = Integer.parseInt(strDateFieldValues[0]);
				month = Integer.parseInt(strDateFieldValues[1]);
				day = Integer.parseInt(strDateFieldValues[2]);
				break;
			case 2:
				year = Integer.parseInt(strDateFieldValues[0]);
				month = Integer.parseInt(strDateFieldValues[1]);
				break;
			case 1:
				// 구분자 없이 이어쓰기한 날짜 형식을 처리한다.
				switch (dateString.length()) {
					case 8:
						year = Integer.parseInt(dateString.substring(0, 4));
						month = Integer.parseInt(dateString.substring(4, 6));
						day = Integer.parseInt(dateString.substring(6));
						break;
					case 6:
						year = Integer.parseInt(dateString.substring(0, 4));
						month = Integer.parseInt(dateString.substring(4));
						break;
					case 4:
						year = Integer.parseInt(dateString);
						break;
					case 2:
						year = 2000 + Integer.parseInt(dateString);
						break;
					default:
						throw new IllegalArgumentException("Invalid date format.(" + dateString + ")");
				}
				break;
			default:
				throw new IllegalArgumentException("Invalid date format.(" + dateString + ")");
		}

		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
	}

	private static void setTime(Calendar calendar, String timeString) {
		int hourOfDay = 0;
		int minute = 0;
		int second = 0;
		int millies = 0;

		String[] strTimeFieldValues = timeString.split(":");
		switch (strTimeFieldValues.length) {
			case 3:
				if (strTimeFieldValues[2].contains(".")) {
					String[] tokens = strTimeFieldValues[2].split("\\.");
					second = Integer.parseInt(tokens[0]);
					millies = Integer.parseInt(tokens[1]) % 1000;
				} else {
					second = Integer.parseInt(strTimeFieldValues[2]);
				}
			case 2:
				minute = Integer.parseInt(strTimeFieldValues[1]);
			case 1:
				// 구분자 없이 이어쓰기한 시간 형식을 처리한다.
				String hourString = strTimeFieldValues[0];
				switch (hourString.length()) {
					case 6:
						hourOfDay = Integer.parseInt(hourString.substring(0, 2));
						minute = Integer.parseInt(hourString.substring(2, 4));
						second = Integer.parseInt(hourString.substring(4, 6));
						break;
					case 4:
						hourOfDay = Integer.parseInt(hourString.substring(0, 2));
						minute = Integer.parseInt(hourString.substring(2, 4));
						break;
					default:
						hourOfDay = Integer.parseInt(hourString);
				}
				break;
			default:
				throw new IllegalArgumentException("Invalid time format.(" + timeString + ")");
		}

		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, millies);
	}

	private static Calendar clearTime(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar;
	}

	public static Integer getElapsedTime(Date startDate, Date endDate) {
		long diffTime = endDate.getTime() - startDate.getTime();
		return (int)(diffTime / (1000 * 60 * 60));
	}

	public static Integer getElapsedMinutes(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return 0;
		}
		long diffTime = endDate.getTime() - startDate.getTime();
		return (int)(diffTime / (1000 * 60));
	}

	/**
	 * 인스턴스 타입이 다른 시간을 비교하기 위해 추가.
	 * 예) date1은 Timestamp 인스턴스이고 date2는 Date 인스턴스인 경우
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean equals(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return ObjectUtils.equals(date1, date2);
		}
		return ObjectUtils.equals(date1.getTime(), date2.getTime());
	}

	public static boolean isEmptyDate(String date) {
		return date.replaceAll("[ 0:-]", "").isEmpty();
	}

	public static boolean isNotEmptyDate(String date) {
		return !isEmptyDate(date);
	}
}
