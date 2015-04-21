package com.naver.mage4j.external.zend;

import java.util.Map;

import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Locale;

public class Zend_Currency {
	/**
	 * TODO locale null 유무에 따는 처리는 {@link Mage_Core_Model_Locale#currency(String)} 참고하여 처리
	 * @param currency
	 * @param locale
	 */
	public Zend_Currency(String currency, Zend_Locale locale) {
		if (locale == null) {
			throw new IllegalArgumentException();
		}

		throw new UnsupportedOperationException();
	}

	public Zend_Currency(String currency) {
		throw new UnsupportedOperationException();
	}

	public void setFormat(Map<String, Object> params) {
		throw new UnsupportedOperationException();
	}
}
