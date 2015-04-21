package com.naver.mage4j.core.zend;

import java.util.TimeZone;

import com.naver.mage4j.external.zend.Zend_Locale;

// TODO Mutable 또는 Immutable로 설계할 지 결정 필요
public class Zend_Date {
	private String timezone;

	public Zend_Date(String date, String part, String locale, String timezone) {
		throw new UnsupportedOperationException();
	}

	public Zend_Date(String date, String part, Zend_Locale locale, TimeZone timezone) {
		throw new UnsupportedOperationException();
	}

	public Zend_Date(long timestamp, String part, Zend_Locale locale, TimeZone timezone) {
		throw new UnsupportedOperationException();
	}

	public String getTimezone() {
		return timezone;
	}

	public Zend_Date setTimezone(String timezone) {
		//		this.timezone = timezone;
		throw new UnsupportedOperationException();
	}

	/**
	 * 시/분/초를 0으로 셋팅
	 */
	public Zend_Date clearTime() {
		throw new UnsupportedOperationException();
	}
}
