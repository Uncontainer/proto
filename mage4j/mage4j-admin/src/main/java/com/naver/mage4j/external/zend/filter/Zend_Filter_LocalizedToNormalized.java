package com.naver.mage4j.external.zend.filter;

import java.util.Map;

import com.naver.mage4j.php.mage.MageAtomArray;

/**
 * Normalizes given localized input
 */
public class Zend_Filter_LocalizedToNormalized implements Zend_Filter_Interface {
	/**
	 * Set options
	 */
	protected Map<String, Object> _options = MageAtomArray.createMap(
		new Object[] {"locale", null},
		new Object[] {"date_format", null},
		new Object[] {"precision", null});

	/**
	 * Class constructor
	 * 
	 * @param locale set 
	 */
	public Zend_Filter_LocalizedToNormalized(Map<String, Object> options/* = null */) {
		throw new UnsupportedOperationException();
//		if (options instanceof Zend_Config) {
//			options = options.toArray();
//		}
//
//		if (null != options) {
//			this.setOptions(options);
//		}
	}

	/**
	 * Returns the set options
	 * 
	 * @return array
	 */
	public Map<String, Object> getOptions() {
		return this._options;
	}

	/**
	 * Sets options to use
	 * 
	 * @param options use 
	 * @return Zend_Filter_LocalizedToNormalized
	 */
	public Zend_Filter_LocalizedToNormalized setOptions(Map<String, Object> options/* = null */) {
		if (options != null) {
			this._options.putAll(options);
		}

		return this;
	}

	/**
	 * Defined by Zend_Filter_InterfaceNormalizes the given input
	 * 
	 * @param value normalized 
	 * @return string|array  The normalized value
	 */
	@Override
	public String filter(String value) {
		throw new UnsupportedOperationException();
//		if (Zend_Locale_Format.isNumber(value, this._options)) {
//			return Zend_Locale_Format.getNumber(value, this._options);
//		} else {
//			if ((this._options.get("date_format") == null) && (value.indexOf(":") != false)) {
//				return Zend_Locale_Format.getTime(value, this._options);
//			} else {
//				if (Zend_Locale_Format.checkDateFormat(value, this._options)) {
//					return Zend_Locale_Format.getDate(value, this._options);
//				}
//			}
//		}
//
//		return value;
	}
}