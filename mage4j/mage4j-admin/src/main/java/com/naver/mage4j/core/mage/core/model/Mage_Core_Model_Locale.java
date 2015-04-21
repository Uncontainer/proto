package com.naver.mage4j.core.mage.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.model.app.Mage_Core_Model_App_Area.AreaType;
import com.naver.mage4j.core.mage.core.model.locale.Mage_Core_Model_Locale_Config;
import com.naver.mage4j.core.mage.core.model.resource.store.Store;
import com.naver.mage4j.core.mage.core.model.resource.store.StoreContext;
import com.naver.mage4j.core.util.PhpDateUtils;
import com.naver.mage4j.core.zend.Zend_Date;
import com.naver.mage4j.external.php.Standard;
import com.naver.mage4j.external.varien.Varien_Object;
import com.naver.mage4j.external.varien.Varien_Profiler;
import com.naver.mage4j.external.zend.Zend_Currency;
import com.naver.mage4j.external.zend.Zend_Locale;
import com.naver.mage4j.external.zend.locale.Zend_Locale_Data;
import com.naver.mage4j.php.mage.MageAtomArray;

/**
 * Locale model
 */
public class Mage_Core_Model_Locale {
	/**
	 * Default locale name
	 */
	public static final String DEFAULT_LOCALE = "en_US";

	/**
	 * Default locale name
	 */
	public static final String DEFAULT_TIMEZONE = "UTC";

	/**
	 * Default locale name
	 */
	public static final String DEFAULT_CURRENCY = "USD";

	/**
	 * XML path constants
	 */
	public static final String XML_PATH_DEFAULT_LOCALE = "general/locale/code";

	/**
	 * XML path constants
	 */
	public static final String XML_PATH_DEFAULT_TIMEZONE = "general/locale/timezone";

	public static final String XML_PATH_DEFAULT_COUNTRY = "general/country/default";

	public static final String XML_PATH_ALLOW_CODES = "global/locale/allow/codes";

	public static final String XML_PATH_ALLOW_CURRENCIES = "global/locale/allow/currencies";

	public static final String XML_PATH_ALLOW_CURRENCIES_INSTALLED = "system/currency/installed";

	/**
	 * Date and time format codes
	 */
	public static final String FORMAT_TYPE_FULL = "full";

	/**
	 * Date and time format codes
	 */
	public static final String FORMAT_TYPE_LONG = "long";

	/**
	 * Date and time format codes
	 */
	public static final String FORMAT_TYPE_MEDIUM = "medium";

	/**
	 * Date and time format codes
	 */
	public static final String FORMAT_TYPE_SHORT = "short";

	/**
	 * Default locale code
	 */
	protected String _defaultLocale;

	/**
	 * Locale object
	 */
	protected Zend_Locale _locale;

	/**
	 * Locale code
	 */
	protected String _localeCode;

	/**
	 * Emulated locales stack
	 */
	protected List<Zend_Locale> _emulatedLocales = new ArrayList<Zend_Locale>();

	/**
	 * Emulated locales stack
	 */
	protected static Map<String, Map<String, Zend_Currency>> _currencyCache = new HashMap<>();

	/**
	 * Emulated locales stack
	 * 
	 */
	public Mage_Core_Model_Locale(String locale/* = null */) {
		this.setLocale(locale);
	}

	/**
	 * Set default locale code
	 * 
	 * @param locale
	 * @return Mage_Core_Model_Locale
	 */
	public Mage_Core_Model_Locale setDefaultLocale(String locale) {
		this._defaultLocale = locale;
		return this;
	}

	/**
	 * REtrieve default locale code
	 * 
	 * @return string
	 */
	public String getDefaultLocale() {
		if (this._defaultLocale == null) {
			String locale = AppContext.getCurrent().getStoreConfigAsString(XML_PATH_DEFAULT_LOCALE);
			if (locale == null) {
				locale = DEFAULT_LOCALE;
			}

			this._defaultLocale = locale;
		}

		return this._defaultLocale;
	}

	/**
	 * Set locale
	 * 
	 * @param locale
	 * @return Mage_Core_Model_Locale
	 */
	public Mage_Core_Model_Locale setLocale(String locale/* = null */) {
		if (locale != null) {
			this._localeCode = locale;
		} else {
			this._localeCode = this.getDefaultLocale();
		}

		AppContext.getCurrent().getEventDispatcher().dispatchEvent("core_locale_set_locale", Collections.singletonMap("locale", this));
		return this;
	}

	/**
	 * Retrieve timezone code
	 * 
	 * @return string
	 */
	public String getTimezone() {
		return DEFAULT_TIMEZONE;
	}

	/**
	 * Retrieve currency code
	 * 
	 * @return string
	 */
	public String getCurrency() {
		return DEFAULT_CURRENCY;
	}

	/**
	 * Retrieve locale object
	 * 
	 * @return Zend_Locale
	 */
	public Zend_Locale getLocale() {
		if (this._locale == null) {
			Zend_Locale_Data.setCache(AppContext.getCurrent().getCache());
			this._locale = (new Zend_Locale(this.getLocaleCode()));
		} else if (this._locale.__toString() != this._localeCode) {
			this.setLocale(this._localeCode);
		}

		return this._locale;
	}

	/**
	 * Retrieve locale code
	 * 
	 * @return string
	 */
	public String getLocaleCode() {
		if (this._localeCode == null) {
			this.setLocale(null);
		}

		return this._localeCode;
	}

	/**
	 * Specify current locale code
	 * 
	 * @param code
	 * @return Mage_Core_Model_Locale
	 */
	public Mage_Core_Model_Locale setLocaleCode(String code) {
		this._localeCode = code;
		this._locale = null;
		return this;
	}

	/**
	 * Get options array for locale dropdown in currunt locale
	 * 
	 * @return array
	 */
	public List<Entry> getOptionLocales() {
		return this._getOptionLocales(false);
	}

	/**
	 * Get translated to original locale options array for locale dropdown
	 * 
	 * @return array
	 */
	public List<Entry> getTranslatedOptionLocales() {
		return this._getOptionLocales(true);
	}

	public static class Entry {
		String label;
		String value;

		public Entry(String label, String value) {
			super();
			this.label = label;
			this.value = value;
		}

		public static final Comparator<Entry> LABEL_ASC = new Comparator<Entry>() {
			@Override
			public int compare(Entry o1, Entry o2) {
				o1.label.compareTo(o2.label);
				return 0;
			}
		};
	}

	/**
	 * Get options array for locale dropdown
	 * 
	 * @param translatedName flag 
	 * @return array
	 */
	protected List<Entry> _getOptionLocales(boolean translatedName/* = false */) {
		List<Entry> options = new ArrayList<Entry>();
		Map<String, ?> locales = this.getLocale().getLocaleList();
		Map<String, ?> languages = this.getLocale().getTranslationList("language", this.getLocale(), null);
		Map<String, ?> countries = this.getCountryTranslationList();
		List<String> allowed = this.getAllowLocales();
		for (String code : locales.keySet()) {
			if (code.contains("_")) {
				if (!allowed.contains(code)) {
					continue;
				}

				String[] data = code.split("_");
				if (data.length < 2) {
					continue;
				}

				String label;
				if (translatedName) {
					label = (Standard.ucwords(this.getLocale().getTranslation(data[0], "language", code)) + " (" + this.getLocale().getTranslation(data[1], "country", code) + ") / " + languages.get(data[0]) + " (" + countries.get(data[1]) + ")");
				} else {
					label = (languages.get(data[0]) + " (" + countries.get(data[1]) + ")");
				}

				options.add(new Entry(label, code));
			}

		}

		return this._sortOptionArray(options);
	}

	/**
	 * Retrieve timezone option list
	 * 
	 * @return array
	 */
	public List<Entry> getOptionTimezones() {
		Map<String, Object> zones = this.getTranslationList("windowstotimezone", null);
		List<Entry> options = new ArrayList<Entry>(zones.size());
		zones = new TreeMap<String, Object>(zones);
		for (Map.Entry<String, Object> each : zones.entrySet()) {
			String code = each.getKey();
			String name = (String)each.getValue();
			name = StringUtils.trim(name);
			name = StringUtils.isBlank(name) ? code : name + " (" + code + ")";
			options.add(new Entry(name, code));
		}

		return this._sortOptionArray(options);
	}

	/**
	 * Retrieve days of week option list
	 * 
	 * @param preserveCodes
	 * @param ucFirstCode
	 * @return array
	 */
	public List<Entry> getOptionWeekdays(boolean preserveCodes/* = false */, boolean ucFirstCode/* = false */) {
		Map<String, Object> days = this.getTranslationList("days", null);
		List<Entry> options = new ArrayList<Entry>(days.size());
		Map<String, Object> wide = (Map<String, Object>)((Map<String, Object>)days.get("format")).get("wide");
		if (preserveCodes) {
			days = wide;
		} else {
			Map<String, Object> temp = new HashMap<String, Object>();
			int index = 0;
			for (Object value : wide.values()) {
				temp.put(Integer.toString(index++), value);
			}
			days = temp;
		}
		for (Map.Entry<String, Object> each : days.entrySet()) {
			String code = each.getKey();
			String name = (String)each.getValue();
			options.add(new Entry(name, ucFirstCode ? StringUtils.capitalize(code) : code));
		}

		return options;
	}

	/**
	 * Retrieve country option list
	 * 
	 * @return array
	 */
	public List<Entry> getOptionCountries() {
		Map<String, String> countries = this.getCountryTranslationList();
		List<Entry> options = new ArrayList<Entry>(countries.size());
		for (Map.Entry<String, String> each : countries.entrySet()) {
			String code = each.getKey();
			String name = each.getValue();
			options.add(new Entry(name, code));
		}

		return this._sortOptionArray(options);
	}

	/**
	 * Retrieve currency option list
	 * 
	 * @return unknown
	 */
	public List<Entry> getOptionCurrencies() {
		Map<String, Object> currencies = this.getTranslationList("currencytoname", null);
		List<Entry> options = new ArrayList<Entry>(currencies.size());
		List<String> allowed = this.getAllowCurrencies();
		for (Map.Entry<String, Object> each : currencies.entrySet()) {
			String name = each.getKey();
			String code = (String)each.getValue();
			if (!allowed.contains(code)) {
				continue;
			}

			options.add(new Entry(name, code));
		}

		return this._sortOptionArray(options);
	}

	/**
	 * Retrieve all currency option list
	 * 
	 * @return unknown
	 */
	public List<Entry> getOptionAllCurrencies() {
		Map<String, Object> currencies = this.getTranslationList("currencytoname", null);
		List<Entry> options = new ArrayList<Entry>(currencies.size());
		for (Map.Entry<String, Object> each : currencies.entrySet()) {
			String name = each.getKey();
			String code = (String)each.getValue();
			options.add(new Entry(name, code));
		}

		return this._sortOptionArray(options);
	}

	/**
	 * Retrieve all currency option list
	 * 
	 * @return unknown
	 */
	protected List<Entry> _sortOptionArray(List<Entry> option) {
		Collections.sort(option, Entry.LABEL_ASC);
		return option;
	}

	/**
	 * Retrieve array of allowed locales
	 * 
	 * @return array
	 */
	public List<String> getAllowLocales() {
		return Mage_Core_Model_Locale_Config.getAllowedLocales();
	}

	/**
	 * Retrieve array of allowed currencies
	 * 
	 * @return unknown
	 */
	public List<String> getAllowCurrencies() {
		if (AppContext.getCurrent().isInstalled()) {
			String data = AppContext.getCurrent().getStoreConfigAsString(XML_PATH_ALLOW_CURRENCIES_INSTALLED);
			return Arrays.asList(StringUtils.split(data, ","));
		} else {
			return Mage_Core_Model_Locale_Config.getAllowedCurrencies();
		}
	}

	/**
	 * Retrieve ISO date formatand filter for 2 digit year format, it must be 4 digits
	 * 
	 * @param type
	 * @return string
	 */
	public String getDateFormat(String type/* = null */) {
		return this.getTranslation(type, "date").replaceAll("(?<!y)yy(?!y)", "yyyy");
	}

	/**
	 * Retrieve short date format with 4-digit year
	 * 
	 * @return string
	 */
	public String getDateFormatWithLongYear() {
		return this.getTranslation(Mage_Core_Model_Locale.FORMAT_TYPE_SHORT, "date").replaceAll("(?<!y)yy(?!y)", "yyyy");
	}

	/**
	 * Retrieve ISO time format
	 * 
	 * @param type
	 * @return string
	 */
	public String getTimeFormat(String type/* = null */) {
		return this.getTranslation(type, "time");
	}

	/**
	 * Retrieve ISO datetime format
	 * 
	 * @param type
	 * @return string
	 */
	public String getDateTimeFormat(String type) {
		return this.getDateFormat(type) + " " + this.getTimeFormat(type);
	}

	/**
	 * Retrieve date format by strftime function
	 * 
	 * @param type
	 * @return string
	 */
//	public String getDateStrFormat(String type) {
//		return Varien_Date.convertZendToStrftime(this.getDateFormat(type), true, false);
//	}

	/**
	 * Retrieve time format by strftime function
	 * 
	 * @param type
	 * @return string
	 */
//	public String getTimeStrFormat(String type) {
//		return Varien_Date.convertZendToStrftime(this.getTimeFormat(type), false, true);
//	}

	/**
	 * Create Zend_Date object for current locale
	 * 
	 * @param  $date 
	 * @param  $part 
	 * @param locale
	 * @param  $useTimezone 
	 * @return Zend_Date
	 */
	public Zend_Date date(String date/* = null */, String part/* = null */, Zend_Locale locale/* = null */, boolean useTimezone/* = true */) {
		if (locale == null) {
			locale = this.getLocale();
		}

		TimeZone timezone = null;
		if (useTimezone) {
			String timezoneString = defaultStore(null).getHelper().getConfigAsString(XML_PATH_DEFAULT_TIMEZONE, null);
			if (StringUtils.isNotEmpty(timezoneString)) {
				timezone = TimeZone.getTimeZone(timezoneString);
			}
		}

		return new Zend_Date(date, part, locale, timezone);
	}

	public Zend_Date date(long timestamp) {
		TimeZone timezone = null;
		String timezoneString = defaultStore(null).getHelper().getConfigAsString(XML_PATH_DEFAULT_TIMEZONE, null);
		if (StringUtils.isNotEmpty(timezoneString)) {
			timezone = TimeZone.getTimeZone(timezoneString);
		}

		return new Zend_Date(timestamp, null, this.getLocale(), timezone);
	}

	/**
	 * Create Zend_Date object with date converted to store timezone and store Locale
	 * 
	 * @param store store 
	 * @param date UTC 
	 * @param includeTime date 
	 * @param format
	 * @return Zend_Date
	 */
	public Zend_Date storeDate(Store store/* = null */, String date/* = null */, boolean includeTime/* = false */, String format/* = null */) {
		Zend_Date result = new Zend_Date(date, format, this.getLocale(), getStoreTimezone(store));
		if (!includeTime) {
			result = result.clearTime();
		}

		return result;
	}

	private TimeZone getStoreTimezone(Store store) {
		String storeTimezoneString = defaultStore(store).getHelper().getConfigAsString(XML_PATH_DEFAULT_TIMEZONE, null);
		if (StringUtils.isEmpty(storeTimezoneString)) {
			storeTimezoneString = getTimezone();
		}

		return TimeZone.getTimeZone(storeTimezoneString);
	}

	private Store defaultStore(Store store) {
		return store != null ? store : AppContext.getCurrent().getStore();
	}

	/**
	 * Create Zend_Date object with date converted from store's timezone
	 * to UTC time zone. Date can be passed in format of store's locale
	 * or in format which was passed as parameter.
	 * 
	 * @param store store 
	 * @param date timezone 
	 * @param includeTime date 
	 * @param format
	 * @return Zend_Date
	 */
	public Zend_Date utcDate(Store store/* = null */, String date, boolean includeTime/* = false */, String format/* = null */) {
		Zend_Date dateObj = this.storeDate(store, date, includeTime, format);
		return dateObj.setTimezone(Mage_Core_Model_Locale.DEFAULT_TIMEZONE);
	}

	/**
	 * Get store timestampTimstamp will be builded with store timezone settings
	 * 
	 * @param store
	 * @return int
	 */
	public long storeTimeStamp(Store store/* = null */) {
		store = defaultStore(store);
		String storeTimezoneString = store.getHelper().getConfigAsString(XML_PATH_DEFAULT_TIMEZONE, null);
		TimeZone timezone = TimeZone.getTimeZone(storeTimezoneString);
		return Calendar.getInstance(timezone).getTimeInMillis();
	}

	/**
	 * Create Zend_Currency object for current locale
	 * 
	 * @param currency
	 * @return Zend_Currency
	 */
	public Zend_Currency currency(String currency) {
		Varien_Profiler.start("locale/currency");
		Map<String, Zend_Currency> map = _currencyCache.get(this.getLocaleCode());
		if (map == null) {
			map = new HashMap<String, Zend_Currency>();
			_currencyCache.put(this.getLocaleCode(), map);
		}

		Zend_Currency result = map.get(currency);
		if (result == null) {
			Varien_Object options = new Varien_Object();
			try {
				result = new Zend_Currency(currency, this.getLocale());
			} catch (Exception e) {
				result = new Zend_Currency(currency);
				options.setData("name", currency);
				options.setData("currency", currency);
				options.setData("symbol", currency);
			}

			options = new Varien_Object(options);
			AppContext.getCurrent().getEventDispatcher().dispatchEvent("currency_display_options_forming", MageAtomArray.createMap(new Object[] {"currency_options", options}, new Object[] {"base_code", currency}));
			result.setFormat(options.toArray(null));
			map.put(currency, result);
		}

		Varien_Profiler.stop("locale/currency");
		return result;
	}

	/**
	 * Returns the first found number from an string
	 * Parsing depends on given locale (grouping and decimal)
	 * Examples for input:
	 * '  2345.4356,1234' = 23455456.1234
	 * '+23,3452.123' = 233452.123
	 * ' 12343 ' = 12343
	 * '-9456km' = -9456
	 * '0' = 0
	 * '2 054,10' = 2054.1
	 * '2'054.52' = 2054.52
	 * '2,46 GB' = 2.46
	 * 
	 * @param value
	 * @return float|null
	 */
	public Number getNumber(String value) {
		if (value == null) {
			return null;
		}

		value = value.replaceAll("'| ", "");
		int separatorComa = value.indexOf(",");
		int separatorDot = value.indexOf(".");
		if (separatorComa >= 0 && separatorDot >= 0) {
			if (separatorComa > separatorDot) {
				value = value.replace(".", "");
				value = value.replace(",", ".");
			} else {
				value = value.replace(",", "");
			}

		} else if (separatorComa >= 0) {
			value = value.replace(",", ".");
		}

		value = value.replaceAll("[^0-9]+$", "");

		return Double.parseDouble(value);
	}

	/**
	 * Functions returns array with price formatting info for js functionformatCurrency in js/varien/js.js
	 * 
	 * @return array
	 */
//	public Map<String, Object> getJsPriceFormat() {
//		String format = Zend_Locale_Data.getContent(this.getLocaleCode(), "currencynumber", false);
//		int pos = format.indexOf(";");
//		if (pos >= 0) {
//			format = format.substring(0, pos);
//		}
//
//		format = format.replaceAll("[^0\\#\\.,]", "");
//		int totalPrecision = 0;
//		int decimalPoint = format.indexOf(".");
//		if (decimalPoint >= 0) {
//			totalPrecision = (format.length() - (format.lastIndexOf(".") + 1));
//		} else {
//			decimalPoint = format.length();
//		}
//
//		int requiredPrecision = totalPrecision;
//		String t = format.substring(decimalPoint);
//		pos = t.indexOf("#");
//		if (pos >= 0) {
//			requiredPrecision = (t.length() - pos - totalPrecision);
//		}
//
//		int group = 0;
//		if (format.lastIndexOf(",") >= 0) {
//			group = (decimalPoint - format.lastIndexOf(",") - 1);
//		} else {
//			group = format.lastIndexOf(".");
//		}
//
//		Map<String, Object> symbols = Zend_Locale_Data.getList(this.getLocaleCode(), "symbols", false);
//		int integerRequired = format.indexOf(".") - format.indexOf("0");
//		Map<String, Object> result = new HashMap<>();
//		result.put("pattern", AppContext.getCurrent().getStore().getHelper().getCurrentCurrency().getOutputFormat());
//		result.put("precision", totalPrecision);
//		result.put("requiredPrecision", requiredPrecision);
//		result.put("decimalSymbol", symbols.get("decimal"));
//		result.put("groupSymbol", symbols.get("group"));
//		result.put("groupLength", group);
//		result.put("integerRequired", integerRequired);
//		return result;
//	}

	/**
	 * Push current locale to stack and replace with locale from specified storeEvent is not dispatched.
	 * 
	 * @param storeId
	 */
	public void emulate(Short storeId) {
		if (storeId != null) {
			this._emulatedLocales.add(this.getLocale());
			String localeId = StoreContext.getContext().getById(storeId).getHelper().getConfigAsString(XML_PATH_DEFAULT_LOCALE, null);
			this._locale = new Zend_Locale(localeId);
			this._localeCode = this._locale.toString();
			AppContext.getCurrent().getTranslator().setLocale(this._locale).init(AreaType.FRONTEND, true);
		} else {
			this._emulatedLocales.add(null);
		}

	}

	/**
	 * Get last locale, used before last emulation
	 * 
	 */
	public void revert() {
		if (_emulatedLocales.isEmpty()) {
			return;
		}

		Zend_Locale locale = this._emulatedLocales.remove(this._emulatedLocales.size() - 1);
		if (locale == null) {
			return;
		}

		this._locale = locale;
		this._localeCode = this._locale.toString();
		AppContext.getCurrent().getTranslator().setLocale(this._locale).init(AreaType.ADMINHTML, true);

	}

	/**
	 * Returns localized informations as array, supported are severaltypes of informations.For detailed information about the types look into the documentation
	 * 
	 * @param  return 
	 * @param  list 
	 * @return array  Array with the wished information in the given language
	 */
	public Map<String, Object> getTranslationList(String path/* = null */, String value/* = null */) {
		return this.getLocale().getTranslationList(path, this.getLocale(), value);
	}

	/**
	 * Returns a localized information string, supported are several types of informations.For detailed information about the types look into the documentation
	 * 
	 * @param  about 
	 * @param  return 
	 * @return string|false  The wished information in the given language
	 */
	public String getTranslation(String value/* = null */, String path/* = null */) {
		return this.getLocale().getTranslation(value, path, this.getLocale());
	}

	/**
	 * Replace all yy date format to yyyy
	 * 
	 * @param null
	 * @return mixed
	 */
	protected String _convertYearTwoDigitTo4(String currentFormat) {
		return currentFormat.replaceAll("(?)(\\byy\\b)", "yyyy");
	}

	/**
	 * Returns the localized country name
	 * 
	 * @param  about 
	 * @return array
	 */
	public String getCountryTranslation(String value) {
		return this.getLocale().getTranslation(value, "country", this.getLocale());
	}

	/**
	 * Returns an array with the name of all countries translated to the given language
	 * 
	 * @return array
	 */
	public Map<String, String> getCountryTranslationList() {
		return this.getLocale().getTranslationList("territory", this.getLocale(), 2);
	}

	/**
	 * Checks if current date of the given store (in the store timezone) is within the range
	 * 
	 * @param store
	 * @param dateFrom
	 * @param dateTo
	 * @return bool
	 */
	public boolean isStoreDateInInterval(Store store, String dateFrom/* = null */, String dateTo/* = null */) {
		long storeTimeStamp = this.storeTimeStamp(store);
		long fromTimeStamp = PhpDateUtils.parseAndGetTime(dateFrom);
		long toTimeStamp = PhpDateUtils.parseAndGetTime(dateTo);
		if (dateTo != null) {
			toTimeStamp += TimeUnit.DAYS.toMillis(1);
		}

		boolean result = false;
		if (PhpDateUtils.isNotEmptyDate(dateFrom) && (storeTimeStamp < fromTimeStamp)) {

		} else if (PhpDateUtils.isNotEmptyDate(dateTo) && (storeTimeStamp > toTimeStamp)) {

		} else {
			result = true;
		}

		return result;
	}
}