package com.naver.mage4j.external.zend.filter;

import java.util.Map;

import com.naver.mage4j.php.mage.MageAtomArray;

/**
 * Localizes given normalized input
 */
public class Zend_Filter_NormalizedToLocalized implements Zend_Filter_Interface {
	/**
	 * Set options
	 */
	protected Map<String, Object> _options = MageAtomArray.createMap(
		new Object[] {"locale", null}
		, new Object[] {"date_format", null}
		, new Object[] {"precision", null});

	/**
	 * Class constructor
	 * 
	 * @param locale set 
	 */
	public Zend_Filter_NormalizedToLocalized(Object options/* = null */) {
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
	public Zend_Filter_NormalizedToLocalized setOptions(Map<String, Object> options/* = null */) {
		throw new UnsupportedOperationException();
//		this._options = (options + this._options);
//		return this;
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
//		Zend_Date date;
//		if (is_array(value)) {
//			date = (new Zend_Date(value, this._options.get("locale")));
//			return date.toString(this._options.get("date_format"));
//		} else {
//			if (this._options.get("precision") == 0) {
//				return Zend_Locale_Format.toInteger(value, this._options);
//			} else {
//				if (this._options.get("precision") == null) {
//					return Zend_Locale_Format.toFloat(value, this._options);
//				}
//			}
//		}
//
//		return Zend_Locale_Format.toNumber(value, this._options);
	}

}