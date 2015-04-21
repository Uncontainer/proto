package com.naver.mage4j.external.zend.locale;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.external.varien.simplexml.SimplexmlUtils;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;
import com.naver.mage4j.external.zend.Zend_Cache;
import com.naver.mage4j.external.zend.Zend_Locale;
import com.naver.mage4j.external.zend.cache.Zend_Cache_Core;

/**
 * Locale data reader, handles the CLDR
 */
public class Zend_Locale_Data {
	/**
	 * Locale files
	 */
	private static Map<String, Simplexml_Element> _ldml = new HashMap<String, Simplexml_Element>();

	/**
	 * List of values which are collected
	 */
	private static Map _list = Collections.emptyMap();

	/**
	 * Internal cache for ldml values
	 */
	private static Zend_Cache_Core _cache = null;

	/**
	 * Internal value to remember if cache supports tags
	 */
	private static boolean _cacheTags = false;

	/**
	 * Internal option, cache disabled
	 */
	private static boolean _cacheDisabled = false;

	/**
	 * Read the content from localeCan be called like:<ldml><delimiter>test</delimiter><second type='myone'>content</second><second type='mysecond'>content2</second><third type='mythird' /></ldml>Case 1: _readFile('ar','/ldml/delimiter')             -> returns [] = testCase 1: _readFile('ar','/ldml/second[@type=myone]')   -> returns [] = contentCase 2: _readFile('ar','/ldml/second','type')         -> returns [myone] = content; [mysecond] = content2Case 3: _readFile('ar','/ldml/delimiter',,'right')    -> returns [right] = testCase 4: _readFile('ar','/ldml/third','type','myone')  -> returns [myone] = mythird
	 * 
	 * @param locale
	 * @param path
	 * @param attribute
	 * @param value
	 * @return array
	 */
	private static Map<String, String> _readFile(String locale, String path, String attribute, String value, Map<String, String> temp) {
		if (temp == null) {
			temp = new LinkedHashMap<String, String>();
		}

		Simplexml_Element config = _ldml.get(locale);
		if (SimplexmlUtils.isNotNull(config)) {
			List<Simplexml_Element> result = config.selectDescendants(path);
			for (Simplexml_Element found : result) {
				if (StringUtils.isEmpty(value)) {
					if (StringUtils.isEmpty(attribute)) {
						temp.put(Integer.toString(temp.size()), found.getText());
					} else {
						String key = found.getString(attribute);
						if (StringUtils.isEmpty(temp.get(key))) {
							temp.put(key, found.getText());
						}
					}

				} else if (StringUtils.isNotEmpty(temp.get(value))) {
					if (StringUtils.isEmpty(attribute)) {
						temp.put(value, found.getText());
					} else {
						temp.put(value, found.getString(attribute));
					}

				}
			}
		}

		return temp;
	}

	/**
	 * Find possible routing to other path or locale
	 * 
	 * @param locale
	 * @param path
	 * @param attribute
	 * @param value
	 * @param  $temp 
	 */
	private static boolean _findRoute(String locale, String path, String attribute, String value, Map<String, String> temp) {
		Simplexml_Element config = _ldml.get(locale);
		if (SimplexmlUtils.isNull(config)) {
			throw new UnsupportedOperationException();
			//			String filename = dirname(__FILE__) + "/Data/" + locale + ".xml";
			//			File file = new File(filename);
			//			if (!file.exists()) {
			//				throw new Zend_Locale_Exception("Missing locale file '$filename' for '$locale' locale.");
			//			}
			//
			//			config = SimplexmlUtils.build(file, (String)null);
			//			_ldml.put(locale, config);
		}

		String search = "";
		Iterator<String> iter = Arrays.asList(StringUtils.split(path, "/")).iterator();
		if (SimplexmlUtils.isNotNull(config)) {
			while (iter.hasNext()) {
				String tok = iter.next();
				search += ("/" + tok);
				if (search.contains("[@")) {
					while (search.lastIndexOf("[@") > search.lastIndexOf("]")) {
						if (iter.hasNext()) {
							tok = iter.next();
						} else {
							search += "/";
							tok = "";
						}

						search = (search + "/" + tok);
					}

				}

				List<Simplexml_Element> result = config.selectDescendants(search + "/alias");
				if (!result.isEmpty()) {
					String source = result.get(0).getString("source");
					String newpath = result.get(0).getString("path");
					if (!"//ldml".equals(newpath)) {
						while (newpath.startsWith("../")) {
							newpath = newpath.substring(3);
							search = search.substring(0, search.lastIndexOf("/"));
						}

						path = search + "/" + newpath;
						while (iter.hasNext()) {
							path = (path + "/" + iter.next());
						}

					}

					if (source != "locale") {
						locale = source;
					}

					temp = _getFile(locale, path, attribute, value, temp);
					return false;
				}
			}
		}

		return true;
	}

	private static Map<String, String> _getFile(String locale, String path, String attribute/* = false */, String value/* = false */) {
		return _getFile(locale, path, attribute, value, null);
	}

	/**
	 * Read the right LDML file
	 * 
	 * @param locale
	 * @param path
	 * @param attribute
	 * @param value
	 */
	private static Map<String, String> _getFile(String locale, String path, String attribute/* = false */, String value/* = false */, Map<String, String> temp/* = Collections.emptyMap() */) {
		boolean result = _findRoute(locale, path, attribute, value, temp);
		if (result) {
			temp = _readFile(locale, path, attribute, value, temp);
		}

		if (!"root".equals(locale) && result) {
			throw new UnsupportedOperationException();
			//			locale = locale.substring(0, -strrchr(locale, "_").length());
			//			if (!(locale == null)) {
			//				temp = _getFile(locale, path, attribute, value, temp);
			//			} else {
			//				temp = _getFile("root", path, attribute, value, temp);
			//			}
		}

		return temp;
	}

	/**
	 * Find the details for supplemental calendar datas
	 * 
	 * @param locale Detaildata 
	 * @param  search 
	 * @return string          Key for Detaildata
	 */
	private static String _calendarDetail(String locale, Map<String, String> list) {
		String ret = "001";
		for (Map.Entry<String, String> each : list.entrySet()) {
			if (locale.contains("_")) {
				locale = locale.substring(locale.indexOf("_") + 1);
			}

			if (each.getKey().contains(locale)) {
				ret = each.getKey();
				break;
			}
		}

		return ret;
	}

	/**
	 * Internal function for checking the locale
	 * 
	 * @param locale check 
	 * @return string
	 */
	private static String _checkLocale(String locale) {
		if (StringUtils.isEmpty(locale)) {
			locale = new Zend_Locale(null).toString();
		}

		if (!Zend_Locale.isLocale(locale, false, false)) {
			throw new Zend_Locale_Exception("Locale (" + (locale) + ") is a unknown locale");
		}

		return locale;
	}

	public static Map getList(Zend_Locale locale, String path, String value/* = false */) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Read the LDML file, get a array of multipath defined value
	 * 
	 * @param locale
	 * @param path
	 * @param value
	 * @return array
	 */
	public static <T> Map<String, T> getList(String locale, String path, Object value/* = false */) {
		throw new UnsupportedOperationException();
		//		Map _temp;
		//		Map datelong;
		//		NoType temp2;
		//		Map shor;
		//		Map _long;
		//		String[] _temp3;
		//		Map dateshor;
		//		Object result;
		//		Map timemedi;
		//		String found;
		//		String weekStart;
		//		String weekEnd;
		//		String id;
		//		NoType key;
		//		Map datefull;
		//		Map val;
		//		NoType keyvalue;
		//		Map timefull;
		//		String minDays;
		//		Map datemedi;
		//		Map medi;
		//		Map timelong;
		//		NoType _temp2;
		//		String firstDay;
		//		Map timeshor;
		//		Map full;
		//		locale = _checkLocale(locale);
		//		if (!(_cache != null && !_cacheDisabled)) {
		//			_cache = Zend_Cache.factory("Core", "File", Collections.singletonMap("automatic_serialization", true));
		//		}
		//
		//		val = value;
		//		if (is_array(value)) {
		//			val = StringUtils.join(value, "_");
		//		}
		//
		//		val = urlencode(val);
		//		id = strtr("Zend_LocaleL_" + locale + "_" + path + "_" + val, MageAtomArray.createMap(new Object[]{"-", "_"}, new Object[]{"%", "_"}, new Object[]{"+", "_"}));
		//		if (!(_cacheDisabled && result = _cache.load(id))) {
		//			return unserialize(result);
		//		}
		//
		//		Map<String, String> temp = new HashMap<String, String>();
		//		switch(path.toLowerCase()) {
		//			case "language": {
		//				temp = _getFile(locale, "/ldml/localeDisplayNames/languages/language", "type", false);
		//				break;
		//			}
		//			case "script": {
		//				temp = _getFile(locale, "/ldml/localeDisplayNames/scripts/script", "type", false);
		//				break;
		//			}
		//			case "territory": {
		//				temp = _getFile(locale, "/ldml/localeDisplayNames/territories/territory", "type", false);
		//				if (value == 1) {
		//					for( Map.Entry<NoType,String> each : temp.entrySet() ) {
		//						NoType key = each.getKey();
		//						String value = each.getValue();
		//						if ((is_numeric(key) == false) && (key != "QO") && (key != "QU")) {
		//							unset(temp.get(key));
		//						}
		//
		//					}
		//
		//				} else {
		//					if (value == 2) {
		//						for( Map.Entry<String,String> each : temp.entrySet() ) {
		//							NoType key = each.getKey();
		//							String value = each.getValue();
		//							if (is_numeric(key) || (key == "QO") || (key == "QU")) {
		//								unset(temp.get(key));
		//							}
		//
		//						}
		//
		//					}
		//				}
		//
		//				break;
		//			}
		//			case "variant": {
		//				temp = _getFile(locale, "/ldml/localeDisplayNames/variants/variant", "type", false);
		//				break;
		//			}
		//			case "key": {
		//				temp = _getFile(locale, "/ldml/localeDisplayNames/keys/key", "type", false);
		//				break;
		//			}
		//			case "type": {
		//				if (value == null) {
		//					temp = _getFile(locale, "/ldml/localeDisplayNames/types/type", "type", false);
		//				} else {
		//					if ((value == "calendar") || (value == "collation") || (value == "currency")) {
		//						temp = _getFile(locale, "/ldml/localeDisplayNames/types/type[@key=\\'" + value + "\\']", "type", false);
		//					} else {
		//						temp = _getFile(locale, "/ldml/localeDisplayNames/types/type[@type=\\'" + value + "\\']", "type", false);
		//					}
		//
		//				}
		//
		//				break;
		//			}
		//			case "layout": {
		//				temp = _getFile(locale, "/ldml/layout/orientation", "lines", "lines");
		//				temp.putAll(_getFile(locale, "/ldml/layout/orientation", "characters", "characters"));
		//				temp.putAll(_getFile(locale, "/ldml/layout/inList", "", "inList"));
		//				temp.putAll(_getFile(locale, "/ldml/layout/inText[@type=\\'currency\\']", "", "currency"));
		//				temp.putAll(_getFile(locale, "/ldml/layout/inText[@type=\\'dayWidth\\']", "", "dayWidth"));
		//				temp.putAll(_getFile(locale, "/ldml/layout/inText[@type=\\'fields\\']", "", "fields"));
		//				temp.putAll(_getFile(locale, "/ldml/layout/inText[@type=\\'keys\\']", "", "keys"));
		//				temp.putAll(_getFile(locale, "/ldml/layout/inText[@type=\\'languages\\']", "", "languages"));
		//				temp.putAll(_getFile(locale, "/ldml/layout/inText[@type=\\'long\\']", "", "long"));
		//				temp.putAll(_getFile(locale, "/ldml/layout/inText[@type=\\'measurementSystemNames\\']", "", "measurementSystemNames"));
		//				temp.putAll(_getFile(locale, "/ldml/layout/inText[@type=\\'monthWidth\\']", "", "monthWidth"));
		//				temp.putAll(_getFile(locale, "/ldml/layout/inText[@type=\\'quarterWidth\\']", "", "quarterWidth"));
		//				temp.putAll(_getFile(locale, "/ldml/layout/inText[@type=\\'scripts\\']", "", "scripts"));
		//				temp.putAll(_getFile(locale, "/ldml/layout/inText[@type=\\'territories\\']", "", "territories"));
		//				temp.putAll(_getFile(locale, "/ldml/layout/inText[@type=\\'types\\']", "", "types"));
		//				temp.putAll(_getFile(locale, "/ldml/layout/inText[@type=\\'variants\\']", "", "variants"));
		//				break;
		//			}
		//			case "characters": {
		//				temp = _getFile(locale, "/ldml/characters/exemplarCharacters", "", "characters");
		//				temp.putAll(_getFile(locale, "/ldml/characters/exemplarCharacters[@type=\\'auxiliary\\']", "", "auxiliary"));
		//				temp.putAll(_getFile(locale, "/ldml/characters/exemplarCharacters[@type=\\'currencySymbol\\']", "", "currencySymbol"));
		//				break;
		//			}
		//			case "delimiters": {
		//				temp = _getFile(locale, "/ldml/delimiters/quotationStart", "", "quoteStart");
		//				temp.putAll(_getFile(locale, "/ldml/delimiters/quotationEnd", "", "quoteEnd"));
		//				temp.putAll(_getFile(locale, "/ldml/delimiters/alternateQuotationStart", "", "quoteStartAlt"));
		//				temp.putAll(_getFile(locale, "/ldml/delimiters/alternateQuotationEnd", "", "quoteEndAlt"));
		//				break;
		//			}
		//			case "measurement": {
		//				temp = _getFile("supplementalData", "/supplementalData/measurementData/measurementSystem[@type=\\'metric\\']", "territories", "metric"));
		//				temp.putAll(_getFile("supplementalData", "/supplementalData/measurementData/measurementSystem[@type=\\'US\\']", "territories", "US"));
		//				temp.putAll(_getFile("supplementalData", "/supplementalData/measurementData/paperSize[@type=\\'A4\\']", "territories", "A4"));
		//				temp.putAll(_getFile("supplementalData", "/supplementalData/measurementData/paperSize[@type=\\'US-Letter\\']", "territories", "US-Letter"));
		//				break;
		//			}
		//			case "months": {
		//				if (value == null) {
		//					value = "gregorian";
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/months/default", "choice", "context");
		//				temp.putAll(_getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/months/monthContext[@type=\\'format\\']/default", "choice", "default"));
		//				temp.get("format").put("abbreviated", _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/months/monthContext[@type=\\'format\\']/monthWidth[@type=\\'abbreviated\\']/month", "type", false));
		//				temp.get("format").put("narrow", _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/months/monthContext[@type=\\'format\\']/monthWidth[@type=\\'narrow\\']/month", "type", false));
		//				temp.get("format").put("wide", _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/months/monthContext[@type=\\'format\\']/monthWidth[@type=\\'wide\\']/month", "type", false));
		//				temp.get("stand-alone").put("abbreviated", _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/months/monthContext[@type=\\'stand-alone\\']/monthWidth[@type=\\'abbreviated\\']/month", "type", false));
		//				temp.get("stand-alone").put("narrow", _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/months/monthContext[@type=\\'stand-alone\\']/monthWidth[@type=\\'narrow\\']/month", "type", false));
		//				temp.get("stand-alone").put("wide", _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/months/monthContext[@type=\\'stand-alone\\']/monthWidth[@type=\\'wide\\']/month", "type", false));
		//				break;
		//			}
		//			case "month": {
		//				if (StringUtils.isEmpty(value)) {
		//					value = Arrays.asList("gregorian","format","wide");
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value.get(0) + "\\']/months/monthContext[@type=\\'" + value.get(1) + "\\']/monthWidth[@type=\\'" + value.get(2) + "\\']/month", "type", false);
		//				break;
		//			}
		//			case "days": {
		//				if (value == null) {
		//					value = "gregorian";
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/days/default", "choice", "context");
		//				temp.putAll(_getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/days/dayContext[@type=\\'format\\']/default", "choice", "default"));
		//				temp.get("format").put("abbreviated", _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/days/dayContext[@type=\\'format\\']/dayWidth[@type=\\'abbreviated\\']/day", "type", false));
		//				temp.get("format").put("narrow", _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/days/dayContext[@type=\\'format\\']/dayWidth[@type=\\'narrow\\']/day", "type", false));
		//				temp.get("format").put("wide", _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/days/dayContext[@type=\\'format\\']/dayWidth[@type=\\'wide\\']/day", "type", false));
		//				temp.get("stand-alone").put("abbreviated", _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/days/dayContext[@type=\\'stand-alone\\']/dayWidth[@type=\\'abbreviated\\']/day", "type", false));
		//				temp.get("stand-alone").put("narrow", _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/days/dayContext[@type=\\'stand-alone\\']/dayWidth[@type=\\'narrow\\']/day", "type", false));
		//				temp.get("stand-alone").put("wide", _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/days/dayContext[@type=\\'stand-alone\\']/dayWidth[@type=\\'wide\\']/day", "type", false));
		//				break;
		//			}
		//			case "day": {
		//				if (StringUtils.isEmpty(value)) {
		//					value = Arrays.asList("gregorian","format","wide");
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value.get(0) + "\\']/days/dayContext[@type=\\'" + value.get(1) + "\\']/dayWidth[@type=\\'" + value.get(2) + "\\']/day", "type", false);
		//				break;
		//			}
		//			case "week": {
		//				minDays = _calendarDetail(locale, _getFile("supplementalData", "/supplementalData/weekData/minDays", "territories", false));
		//				firstDay = _calendarDetail(locale, _getFile("supplementalData", "/supplementalData/weekData/firstDay", "territories", false));
		//				weekStart = _calendarDetail(locale, _getFile("supplementalData", "/supplementalData/weekData/weekendStart", "territories", false));
		//				weekEnd = _calendarDetail(locale, _getFile("supplementalData", "/supplementalData/weekData/weekendEnd", "territories", false));
		//				temp = _getFile("supplementalData", "/supplementalData/weekData/minDays[@territories='" + minDays + "']", "count", "minDays");
		//				temp.putAll(_getFile("supplementalData", "/supplementalData/weekData/firstDay[@territories='" + firstDay + "']", "day", "firstDay"));
		//				temp.putAll(_getFile("supplementalData", "/supplementalData/weekData/weekendStart[@territories='" + weekStart + "']", "day", "weekendStart"));
		//				temp.putAll(_getFile("supplementalData", "/supplementalData/weekData/weekendEnd[@territories='" + weekEnd + "']", "day", "weekendEnd"));
		//				break;
		//			}
		//			case "quarters": {
		//				if (value == null) {
		//					value = "gregorian";
		//				}
		//
		//				temp.get("format").put("abbreviated", _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/quarters/quarterContext[@type=\\'format\\']/quarterWidth[@type=\\'abbreviated\\']/quarter", "type", false));
		//				temp.get("format").put("narrow", _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/quarters/quarterContext[@type=\\'format\\']/quarterWidth[@type=\\'narrow\\']/quarter", "type", false));
		//				temp.get("format").put("wide", _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/quarters/quarterContext[@type=\\'format\\']/quarterWidth[@type=\\'wide\\']/quarter", "type", false));
		//				temp.get("stand-alone").put("abbreviated", _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/quarters/quarterContext[@type=\\'stand-alone\\']/quarterWidth[@type=\\'abbreviated\\']/quarter", "type", false));
		//				temp.get("stand-alone").put("narrow", _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/quarters/quarterContext[@type=\\'stand-alone\\']/quarterWidth[@type=\\'narrow\\']/quarter", "type", false));
		//				temp.get("stand-alone").put("wide", _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/quarters/quarterContext[@type=\\'stand-alone\\']/quarterWidth[@type=\\'wide\\']/quarter", "type", false));
		//				break;
		//			}
		//			case "quarter": {
		//				if (StringUtils.isEmpty(value)) {
		//					value = Arrays.asList("gregorian","format","wide");
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value.get(0) + "\\']/quarters/quarterContext[@type=\\'" + value.get(1) + "\\']/quarterWidth[@type=\\'" + value.get(2) + "\\']/quarter", "type", false);
		//				break;
		//			}
		//			case "eras": {
		//				if (value == null) {
		//					value = "gregorian";
		//				}
		//
		//				temp.put("names", _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/eras/eraNames/era", "type", false));
		//				temp."abbreviated" = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/eras/eraAbbr/era", "type", false);
		//				temp."narrow" = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/eras/eraNarrow/era", "type", false);
		//				break;
		//			}
		//			case "era": {
		//				if (StringUtils.isEmpty(value)) {
		//					value = Arrays.asList("gregorian","Abbr");
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value.get(0) + "\\']/eras/era" + value.get(1) + "/era", "type", false);
		//				break;
		//			}
		//			case "date": {
		//				if (value == null) {
		//					value = "gregorian";
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/dateFormats/dateFormatLength[@type=\\'full\\']/dateFormat/pattern", "", "full");
		//				temp.putAll(_getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/dateFormats/dateFormatLength[@type=\\'long\\']/dateFormat/pattern", "", "long");
		//				temp.putAll(_getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/dateFormats/dateFormatLength[@type=\\'medium\\']/dateFormat/pattern", "", "medium");
		//				temp.putAll(_getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/dateFormats/dateFormatLength[@type=\\'short\\']/dateFormat/pattern", "", "short");
		//				break;
		//			}
		//			case "time": {
		//				if (StringUtils.isEmpty(value)) {
		//					value = "gregorian";
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/timeFormats/timeFormatLength[@type=\\'full\\']/timeFormat/pattern", "", "full");
		//				temp.putAll(_getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/timeFormats/timeFormatLength[@type=\\'long\\']/timeFormat/pattern", "", "long");
		//				temp.putAll(_getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/timeFormats/timeFormatLength[@type=\\'medium\\']/timeFormat/pattern", "", "medium");
		//				temp.putAll(_getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/timeFormats/timeFormatLength[@type=\\'short\\']/timeFormat/pattern", "", "short");
		//				break;
		//			}
		//			case "datetime": {
		//				if (StringUtils.isEmpty(value)) {
		//					value = "gregorian";
		//				}
		//
		//				timefull = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/timeFormats/timeFormatLength[@type=\\'full\\']/timeFormat/pattern", "", "full");
		//				timelong = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/timeFormats/timeFormatLength[@type=\\'long\\']/timeFormat/pattern", "", "long");
		//				timemedi = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/timeFormats/timeFormatLength[@type=\\'medium\\']/timeFormat/pattern", "", "medi");
		//				timeshor = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/timeFormats/timeFormatLength[@type=\\'short\\']/timeFormat/pattern", "", "shor");
		//				datefull = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/dateFormats/dateFormatLength[@type=\\'full\\']/dateFormat/pattern", "", "full");
		//				datelong = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/dateFormats/dateFormatLength[@type=\\'long\\']/dateFormat/pattern", "", "long");
		//				datemedi = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/dateFormats/dateFormatLength[@type=\\'medium\\']/dateFormat/pattern", "", "medi");
		//				dateshor = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/dateFormats/dateFormatLength[@type=\\'short\\']/dateFormat/pattern", "", "shor");
		//				full = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/dateTimeFormats/dateTimeFormatLength[@type=\\'full\\']/dateTimeFormat/pattern", "", "full");
		//				_long = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/dateTimeFormats/dateTimeFormatLength[@type=\\'long\\']/dateTimeFormat/pattern", "", "long");
		//				medi = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/dateTimeFormats/dateTimeFormatLength[@type=\\'medium\\']/dateTimeFormat/pattern", "", "medi");
		//				shor = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/dateTimeFormats/dateTimeFormatLength[@type=\\'short\\']/dateTimeFormat/pattern", "", "shor");
		//				temp.put("full", full.get("full").replace(Arrays.asList("{0}","{1}"), Arrays.asList(timefull.get("full"),datefull.get("full"))));
		//				temp."long" = _long.get("long").replace(Arrays.asList("{0}","{1}"), Arrays.asList(timelong.get("long"),datelong.get("long")));
		//				temp."medium" = medi.get("medi").replace(Arrays.asList("{0}","{1}"), Arrays.asList(timemedi.get("medi"),datemedi.get("medi")));
		//				temp."short" = shor.get("shor").replace(Arrays.asList("{0}","{1}"), Arrays.asList(timeshor.get("shor"),dateshor.get("shor")));
		//				break;
		//			}
		//			case "dateitem": {
		//				if (StringUtils.isEmpty(value)) {
		//					value = "gregorian";
		//				}
		//
		//				_temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/dateTimeFormats/availableFormats/dateFormatItem", "id", false);
		//				for( Map.Entry<NoType,String> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					temp.putAll(_getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/dateTimeFormats/availableFormats/dateFormatItem[@id=\\'" + key + "\\']", "", key);
		//				}
		//
		//				break;
		//			}
		//			case "dateinterval": {
		//				if (StringUtils.isEmpty(value)) {
		//					value = "gregorian";
		//				}
		//
		//				_temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/dateTimeFormats/intervalFormats/intervalFormatItem", "id", false);
		//				for( Map.Entry<NoType,String> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					temp.key = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/dateTimeFormats/intervalFormats/intervalFormatItem[@id=\\'" + key + "\\']/greatestDifference", "id", false);
		//				}
		//
		//				break;
		//			}
		//			case "field": {
		//				if (StringUtils.isEmpty(value)) {
		//					value = "gregorian";
		//				}
		//
		//				temp2 = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/fields/field", "type", false);
		//				for( Map.Entry<NoType,ERROR> each : temp2.entrySet() ) {
		//					NoType key = each.getKey();
		//					ERROR keyvalue = each.getValue();
		//					temp.putAll(_getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/fields/field[@type=\\'" + key + "\\']/displayName", "", key);
		//				}
		//
		//				break;
		//			}
		//			case "relative": {
		//				if (StringUtils.isEmpty(value)) {
		//					value = "gregorian";
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/fields/field/relative", "type", false);
		//				break;
		//			}
		//			case "symbols": {
		//				temp = _getFile(locale, "/ldml/numbers/symbols/decimal", "", "decimal");
		//				temp.putAll(_getFile(locale, "/ldml/numbers/symbols/group", "", "group");
		//				temp.putAll(_getFile(locale, "/ldml/numbers/symbols/list", "", "list");
		//				temp.putAll(_getFile(locale, "/ldml/numbers/symbols/percentSign", "", "percent");
		//				temp.putAll(_getFile(locale, "/ldml/numbers/symbols/nativeZeroDigit", "", "zero");
		//				temp.putAll(_getFile(locale, "/ldml/numbers/symbols/patternDigit", "", "pattern");
		//				temp.putAll(_getFile(locale, "/ldml/numbers/symbols/plusSign", "", "plus");
		//				temp.putAll(_getFile(locale, "/ldml/numbers/symbols/minusSign", "", "minus");
		//				temp.putAll(_getFile(locale, "/ldml/numbers/symbols/exponential", "", "exponent");
		//				temp += _getFile(locale, "/ldml/numbers/symbols/perMille", "", "mille");
		//				temp.putAll(_getFile(locale, "/ldml/numbers/symbols/infinity", "", "infinity");
		//				temp.putAll(_getFile(locale, "/ldml/numbers/symbols/nan", "", "nan");
		//				break;
		//			}
		//			case "nametocurrency": {
		//				_temp = _getFile(locale, "/ldml/numbers/currencies/currency", "type", false);
		//				for( Map.Entry<NoType,String> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					temp += _getFile(locale, "/ldml/numbers/currencies/currency[@type=\\'" + key + "\\']/displayName", "", key);
		//				}
		//
		//				break;
		//			}
		//			case "currencytoname": {
		//				_temp = _getFile(locale, "/ldml/numbers/currencies/currency", "type", false);
		//				for( Map.Entry<NoType,ERROR> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					ERROR keyvalue = each.getValue();
		//					val = _getFile(locale, "/ldml/numbers/currencies/currency[@type=\\'" + key + "\\']/displayName", "", key);
		//					if (!(val.get(key) != null)) {
		//						continue;
		//					}
		//
		//					if (!(temp.get(val.get(key)) != null)) {
		//						temp.put(val.get(key), key);
		//					} else {
		//						temp.val.get(key) += (" " + key);
		//					}
		//
		//				}
		//
		//				break;
		//			}
		//			case "currencysymbol": {
		//				_temp = _getFile(locale, "/ldml/numbers/currencies/currency", "type", false);
		//				for( Map.Entry<NoType,String> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					temp += _getFile(locale, "/ldml/numbers/currencies/currency[@type=\\'" + key + "\\']/symbol", "", key);
		//				}
		//
		//				break;
		//			}
		//			case "question": {
		//				temp = _getFile(locale, "/ldml/posix/messages/yesstr", "", "yes");
		//				temp += _getFile(locale, "/ldml/posix/messages/nostr", "", "no");
		//				break;
		//			}
		//			case "currencyfraction": {
		//				_temp = _getFile("supplementalData", "/supplementalData/currencyData/fractions/info", "iso4217", false);
		//				for( Map.Entry<NoType,String> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					temp += _getFile("supplementalData", "/supplementalData/currencyData/fractions/info[@iso4217=\\'" + key + "\\']", "digits", key);
		//				}
		//
		//				break;
		//			}
		//			case "currencyrounding": {
		//				_temp = _getFile("supplementalData", "/supplementalData/currencyData/fractions/info", "iso4217", false);
		//				for( Map.Entry<NoType,String> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					temp += _getFile("supplementalData", "/supplementalData/currencyData/fractions/info[@iso4217=\\'" + key + "\\']", "rounding", key);
		//				}
		//
		//				break;
		//			}
		//			case "currencytoregion": {
		//				_temp = _getFile("supplementalData", "/supplementalData/currencyData/region", "iso3166", false);
		//				for( Map.Entry<NoType,ERROR> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					ERROR keyvalue = each.getValue();
		//					temp += _getFile("supplementalData", "/supplementalData/currencyData/region[@iso3166=\\'" + key + "\\']/currency", "iso4217", key);
		//				}
		//
		//				break;
		//			}
		//			case "regiontocurrency": {
		//				_temp = _getFile("supplementalData", "/supplementalData/currencyData/region", "iso3166", false);
		//				for( Map.Entry<NoType,ERROR> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					ERROR keyvalue = each.getValue();
		//					val = _getFile("supplementalData", "/supplementalData/currencyData/region[@iso3166=\\'" + key + "\\']/currency", "iso4217", key);
		//					if (!(val.get(key) != null)) {
		//						continue;
		//					}
		//
		//					if (!(temp.get(val.get(key)) != null)) {
		//						temp.put(val.get(key), key);
		//					} else {
		//						temp.val.get(key) += (" " + key);
		//					}
		//
		//				}
		//
		//				break;
		//			}
		//			case "regiontoterritory": {
		//				_temp = _getFile("supplementalData", "/supplementalData/territoryContainment/group", "type", false);
		//				for( Map.Entry<NoType,String> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					temp += _getFile("supplementalData", "/supplementalData/territoryContainment/group[@type=\\'" + key + "\\']", "contains", key);
		//				}
		//
		//				break;
		//			}
		//			case "territorytoregion": {
		//				_temp2 = _getFile("supplementalData", "/supplementalData/territoryContainment/group", "type", false);
		//				_temp = Collections.emptyMap();
		//				for( Map.Entry<NoType,String> each : _temp2.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					_temp += _getFile("supplementalData", "/supplementalData/territoryContainment/group[@type=\\'" + key + "\\']", "contains", key);
		//				}
		//
		//				for( Map.Entry<NoType,String> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					_temp3 = found.split(" ");
		//					for( String found3 : _temp3 ) {
		//						if (!(temp.found3 != null)) {
		//							temp.found3 = ((String)key);
		//						} else {
		//							temp.found3 += (" " + key);
		//						}
		//
		//					}
		//
		//				}
		//
		//				break;
		//			}
		//			case "scripttolanguage": {
		//				_temp = _getFile("supplementalData", "/supplementalData/languageData/language", "type", false);
		//				for( Map.Entry<NoType,String> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					temp += _getFile("supplementalData", "/supplementalData/languageData/language[@type=\\'" + key + "\\']", "scripts", key);
		//					if (temp.key == null) {
		//						unset(temp.key);
		//					}
		//
		//				}
		//
		//				break;
		//			}
		//			case "languagetoscript": {
		//				_temp2 = _getFile("supplementalData", "/supplementalData/languageData/language", "type", false);
		//				_temp = Collections.emptyMap();
		//				for( Map.Entry<NoType,String> each : _temp2.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					_temp += _getFile("supplementalData", "/supplementalData/languageData/language[@type=\\'" + key + "\\']", "scripts", key);
		//				}
		//
		//				for( Map.Entry<NoType,String> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					_temp3 = found.split(" ");
		//					for( String found3 : _temp3 ) {
		//						if (found3 == null) {
		//							continue;
		//						}
		//
		//						if (!(temp.found3 != null)) {
		//							temp.found3 = ((String)key);
		//						} else {
		//							temp.found3 += (" " + key);
		//						}
		//
		//					}
		//
		//				}
		//
		//				break;
		//			}
		//			case "territorytolanguage": {
		//				_temp = _getFile("supplementalData", "/supplementalData/languageData/language", "type", false);
		//				for( Map.Entry<NoType,String> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					temp += _getFile("supplementalData", "/supplementalData/languageData/lan.putAll(e[@type=\\'" + key + "\\']", "territories", key);
		//					if (temp.key == null) {
		//						unset(temp.key);
		//					}
		//
		//				}
		//
		//				break;
		//			}
		//			case "languagetoterritory": {
		//				_temp2 = _getFile("supplementalData", "/supplementalData/languageData/language", "type", false);
		//				_temp = Collections.emptyMap();
		//				for( Map.Entry<NoType,String> each : _temp2.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					_temp += _getFile("supplementalData", "/supplementalData/languageData/language[@type=\\'" + key + "\\']", "territories", key);
		//				}
		//
		//				for( Map.Entry<NoType,String> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					_temp3 = found.split(" ");
		//					for( String found3 : _temp3 ) {
		//						if (found3 == null) {
		//							continue;
		//						}
		//
		//						if (!(temp.found3 != null)) {
		//							temp.found3 = ((String)key);
		//						} else {
		//							temp.found3 += (" " + key);
		//						}
		//
		//					}
		//
		//				}
		//
		//				break;
		//			}
		//			case "timezonetowindows": {
		//				_temp = _getFile("supplementalData", "/supplementalData/timezoneData/mapTimezones[@type=\\'windows\\']/mapZone", "other", false);
		//				for( Map.Entry<NoType,String> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					temp += _getFile("supplementalData", "/supplementalData/timezoneData/mapTimezones[@type=\\'windows\\']/mapZone[@other=\\'" + key + "\\']", "type", key);
		//				}
		//
		//				break;
		//			}
		//			case "windowstotimezone": {
		//				_temp = _getFile("supplementalData", "/supplementalData/timezoneData/mapTimezones[@type=\\'windows\\']/mapZone", "type", false);
		//				for( Map.Entry<NoType,String> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					temp += _getFile("supplementalData", "/supplementalData/timezoneData/mapTimezones[@type=\\'windows\\']/mapZone[@type=\\'" + key + "\\']", "other", key);
		//				}
		//
		//				break;
		//			}
		//			case "territorytotimezone": {
		//				_temp = _getFile("supplementalData", "/supplementalData/timezoneData/zoneFormatting/zoneItem", "type", false);
		//				for( Map.Entry<NoType,String> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					temp += _getFile("supplementalData", "/supplementalData/timezoneData/zoneFormatting/zoneItem[@type=\\'" + key + "\\']", "territory", key);
		//				}
		//
		//				break;
		//			}
		//			case "timezonetoterritory": {
		//				_temp = _getFile("supplementalData", "/supplementalData/timezoneData/zoneFormatting/zoneItem", "territory", false);
		//				for( Map.Entry<NoType,String> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					temp += _getFile("supplementalData", "/supplementalData/timezoneData/zoneFormatting/zoneItem[@territory=\\'" + key + "\\']", "type", key);
		//				}
		//
		//				break;
		//			}
		//			case "citytotimezone": {
		//				_temp = _getFile(locale, "/ldml/dates/timeZoneNames/zone", "type", false);
		//				for( Map.Entry<NoType,String> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					temp += _getFile(locale, "/ldml/dates/timeZoneNames/zone[@type=\\'" + key + "\\']/exemplarCity", "", key);
		//				}
		//
		//				break;
		//			}
		//			case "timezonetocity": {
		//				_temp = _getFile(locale, "/ldml/dates/timeZoneNames/zone", "type", false);
		//				temp = Collections.emptyMap();
		//				for( Map.Entry<NoType,String> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					String found = each.getValue();
		//					temp += _getFile(locale, "/ldml/dates/timeZoneNames/zone[@type=\\'" + key + "\\']/exemplarCity", "", key);
		//					if (!(temp.get(key) == null)) {
		//						temp.put(temp.get(key), key);
		//					}
		//
		//					unset(temp.key);
		//				}
		//
		//				break;
		//			}
		//			case "phonetoterritory": {
		//				_temp = _getFile("telephoneCodeData", "/supplementalData/telephoneCodeData/codesByTerritory", "territory", false);
		//				for( Map.Entry<NoType,ERROR> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					ERROR keyvalue = each.getValue();
		//					temp += _getFile("telephoneCodeData", "/supplementalData/telephoneCodeData/codesByTerritory[@territory=\\'" + key + "\\']/telephoneCountryCode", "code", key);
		//				}
		//
		//				break;
		//			}
		//			case "territorytophone": {
		//				_temp = _getFile("telephoneCodeData", "/supplementalData/telephoneCodeData/codesByTerritory", "territory", false);
		//				for( Map.Entry<NoType,ERROR> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					ERROR keyvalue = each.getValue();
		//					val = _getFile("telephoneCodeData", "/supplementalData/telephoneCodeData/codesByTerritory[@territory=\\'" + key + "\\']/telephoneCountryCode", "code", key);
		//					if (!(val.get(key) != null)) {
		//						continue;
		//					}
		//
		//					if (!(temp.val.get(key) != null)) {
		//						temp.val.get(key) = key;
		//					} else {
		//						temp.val.get(key) += (" " + key);
		//					}
		//
		//				}
		//
		//				break;
		//			}
		//			case "numerictoterritory": {
		//				_temp = _getFile("supplementalData", "/supplementalData/codeMappings/territoryCodes", "type", false);
		//				for( Map.Entry<NoType,ERROR> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					ERROR keyvalue = each.getValue();
		//					temp += _getFile("supplementalData", "/supplementalData/codeMappings/territoryCodes[@type=\\'" + key + "\\']", "numeric", key);
		//				}
		//
		//				break;
		//			}
		//			case "territorytonumeric": {
		//				_temp = _getFile("supplementalData", "/supplementalData/codeMappings/territoryCodes", "numeric", false);
		//				for( Map.Entry<NoType,ERROR> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					ERROR keyvalue = each.getValue();
		//					temp += _getFile("supplementalData", "/supplementalData/codeMappings/territoryCodes[@numeric=\\'" + key + "\\']", "type", key);
		//				}
		//
		//				break;
		//			}
		//			case "alpha3toterritory": {
		//				_temp = _getFile("supplementalData", "/supplementalData/codeMappings/territoryCodes", "type", false);
		//				for( Map.Entry<NoType,ERROR> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					ERROR keyvalue = each.getValue();
		//					temp += _getFile("supplementalData", "/supplementalData/codeMappings/territoryCodes[@type=\\'" + key + "\\']", "alpha3", key);
		//				}
		//
		//				break;
		//			}
		//			case "territorytoalpha3": {
		//				_temp = _getFile("supplementalData", "/supplementalData/codeMappings/territoryCodes", "alpha3", false);
		//				for( Map.Entry<NoType,ERROR> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					ERROR keyvalue = each.getValue();
		//					temp += _getFile("supplementalData", "/supplementalData/codeMappings/territoryCodes[@alpha3=\\'" + key + "\\']", "type", key);
		//				}
		//
		//				break;
		//			}
		//			case "postaltoterritory": {
		//				_temp = _getFile("postalCodeData", "/supplementalData/postalCodeData/postCodeRegex", "territoryId", false);
		//				for( Map.Entry<NoType,ERROR> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					ERROR keyvalue = each.getValue();
		//					temp += _getFile("postalCodeData", "/supplementalData/postalCodeData/postCodeRegex[@territoryId=\\'" + key + "\\']", "territoryId", false);
		//				}
		//
		//				break;
		//			}
		//			case "numberingsystem": {
		//				_temp = _getFile("numberingSystems", "/supplementalData/numberingSystems/numberingSystem", "id", false);
		//				for( Map.Entry<NoType,ERROR> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					ERROR keyvalue = each.getValue();
		//					temp += _getFile("numberingSystems", "/supplementalData/numberingSystems/numberingSystem[@id=\\'" + key + "\\']", "digits", key);
		//					if (temp.key == null) {
		//						unset(temp.key);
		//					}
		//
		//				}
		//
		//				break;
		//			}
		//			case "chartofallback": {
		//				_temp = _getFile("characters", "/supplementalData/characters/character-fallback/character", "value", false);
		//				for( Map.Entry<NoType,ERROR> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					ERROR keyvalue = each.getValue();
		//					temp2 = _getFile("characters", "/supplementalData/characters/character-fallback/character[@value=\\'" + key + "\\']/substitute", "", key);
		//					temp.current(temp2) = key;
		//				}
		//
		//				break;
		//			}
		//			case "fallbacktochar": {
		//				_temp = _getFile("characters", "/supplementalData/characters/character-fallback/character", "value", false);
		//				for( Map.Entry<NoType,ERROR> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					ERROR keyvalue = each.getValue();
		//					temp += _getFile("characters", "/supplementalData/characters/character-fallback/character[@value=\\'" + key + "\\']/substitute", "", key);
		//				}
		//
		//				break;
		//			}
		//			case "localeupgrade": {
		//				_temp = _getFile("likelySubtags", "/supplementalData/likelySubtags/likelySubtag", "from", false);
		//				for( Map.Entry<NoType,ERROR> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					ERROR keyvalue = each.getValue();
		//					temp += _getFile("likelySubtags", "/supplementalData/likelySubtags/likelySubtag[@from=\\'" + key + "\\']", "to", key);
		//				}
		//
		//				break;
		//			}
		//			case "unit": {
		//				_temp = _getFile(locale, "/ldml/units/unit", "type", false);
		//				for( Map.Entry<NoType,ERROR> each : _temp.entrySet() ) {
		//					NoType key = each.getKey();
		//					ERROR keyvalue = each.getValue();
		//					_temp2 = _getFile(locale, "/ldml/units/unit[@type=\\'" + key + "\\']/unitPattern", "count", false);
		//					temp.key = _temp2;
		//				}
		//
		//				break;
		//			}
		//			default: {
		//				throw new Zend_Locale_Exception("Unknown list ($path) for parsing locale data.");
		//				break;
		//			}
		//		}
		//
		//		if (_cache != null) {
		//			if (_cacheTags) {
		//				_cache.save(serialize(temp), id, Collections.singletonList("Zend_Locale"));
		//			} else {
		//				_cache.save(serialize(temp), id);
		//			}
		//
		//		}
		//
		//		return temp;
	}

	/**
	 * Read the LDML file, get a single path defined value
	 * 
	 * @param locale
	 * @param path
	 * @param value
	 * @return string
	 */
	public static String getContent(String locale, String path, Object value/* = false */) {
		throw new UnsupportedOperationException();
		//		Map val;
		//		NoType date;
		//		NoType temp;
		//		Map _temp;
		//		NoType keyvalue;
		//		NoType temp2;
		//		String[] _temp3;
		//		Object result;
		//		Map _temp2;
		//		NoType datetime;
		//		String found;
		//		String id;
		//		NoType time;
		//		Object key;
		//		locale = _checkLocale(locale);
		//		if (!((_cache != null) && (!(_cacheDisabled)))) {
		//			_cache = Zend_Cache.factory("Core", "File", Collections.singletonMap("automatic_serialization", true));
		//		}
		//
		//		val = value;
		//		if (is_array(value)) {
		//			val = StringUtils.join(value, "_");
		//		}
		//
		//		val = urlencode(val);
		//		id = strtr("Zend_LocaleC_" + locale + "_" + path + "_" + val, MageAtomArray.createMap(new Object[]{"-", "_"}, new Object[]{"%", "_"}, new Object[]{"+", "_"}));
		//		if (!(_cacheDisabled && result = _cache.load(id))) {
		//			return unserialize(result);
		//		}
		//
		//		switch(path.toLowerCase()) {
		//			case "language": {
		//				temp = _getFile(locale, "/ldml/localeDisplayNames/languages/language[@type=\\'" + value + "\\']", "type", false);
		//				break;
		//			}
		//			case "script": {
		//				temp = _getFile(locale, "/ldml/localeDisplayNames/scripts/script[@type=\\'" + value + "\\']", "type", false);
		//				break;
		//			}
		//			case "country": {
		//
		//			}
		//			case "territory": {
		//				temp = _getFile(locale, "/ldml/localeDisplayNames/territories/territory[@type=\\'" + value + "\\']", "type", false);
		//				break;
		//			}
		//			case "variant": {
		//				temp = _getFile(locale, "/ldml/localeDisplayNames/variants/variant[@type=\\'" + value + "\\']", "type", false);
		//				break;
		//			}
		//			case "key": {
		//				temp = _getFile(locale, "/ldml/localeDisplayNames/keys/key[@type=\\'" + value + "\\']", "type", false);
		//				break;
		//			}
		//			case "defaultcalendar": {
		//				temp = _getFile(locale, "/ldml/dates/calendars/default", "choice", "default");
		//				break;
		//			}
		//			case "monthcontext": {
		//				if (value == null) {
		//					value = "gregorian";
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/months/default", "choice", "context");
		//				break;
		//			}
		//			case "defaultmonth": {
		//				if (StringUtils.isEmpty(value)) {
		//					value = "gregorian";
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/months/monthContext[@type=\\'format\\']/default", "choice", "default");
		//				break;
		//			}
		//			case "month": {
		//				if (!(is_array(value))) {
		//					temp = value;
		//					value = Arrays.asList("gregorian","format","wide",temp);
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value.get(0) + "\\']/months/monthContext[@type=\\'" + value.get(1) + "\\']/monthWidth[@type=\\'" + value.get(2) + "\\']/month[@type=\\'" + value.get(3) + "\\']", "type", false);
		//				break;
		//			}
		//			case "daycontext": {
		//				if (value == null) {
		//					value = "gregorian";
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/days/default", "choice", "context");
		//				break;
		//			}
		//			case "defaultday": {
		//				if (StringUtils.isEmpty(value)) {
		//					value = "gregorian";
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/days/dayContext[@type=\\'format\\']/default", "choice", "default");
		//				break;
		//			}
		//			case "day": {
		//				if (!(is_array(value))) {
		//					temp = value;
		//					value = Arrays.asList("gregorian","format","wide",temp);
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value.get(0) + "\\']/days/dayContext[@type=\\'" + value.get(1) + "\\']/dayWidth[@type=\\'" + value.get(2) + "\\']/day[@type=\\'" + value.get(3) + "\\']", "type", false);
		//				break;
		//			}
		//			case "quarter": {
		//				if (!(is_array(value))) {
		//					temp = value;
		//					value = Arrays.asList("gregorian","format","wide",temp);
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value.get(0) + "\\']/quarters/quarterContext[@type=\\'" + value.get(1) + "\\']/quarterWidth[@type=\\'" + value.get(2) + "\\']/quarter[@type=\\'" + value.get(3) + "\\']", "type", false);
		//				break;
		//			}
		//			case "am": {
		//				if (value == null) {
		//					value = "gregorian";
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/am", "", "am");
		//				break;
		//			}
		//			case "pm": {
		//				if (StringUtils.isEmpty(value)) {
		//					value = "gregorian";
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/pm", "", "pm");
		//				break;
		//			}
		//			case "era": {
		//				if (!(is_array(value))) {
		//					temp = value;
		//					value = Arrays.asList("gregorian","Abbr",temp);
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value.get(0) + "\\']/eras/era" + value.get(1) + "/era[@type=\\'" + value.get(2) + "\\']", "type", false);
		//				break;
		//			}
		//			case "defaultdate": {
		//				if (value == null) {
		//					value = "gregorian";
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/dateFormats/default", "choice", "default");
		//				break;
		//			}
		//			case "date": {
		//				if (StringUtils.isEmpty(value)) {
		//					value = Arrays.asList("gregorian","medium");
		//				}
		//
		//				if (!(is_array(value))) {
		//					temp = value;
		//					value = Arrays.asList("gregorian",temp);
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value.get(0) + "\\']/dateFormats/dateFormatLength[@type=\\'" + value.get(1) + "\\']/dateFormat/pattern", "", "pattern");
		//				break;
		//			}
		//			case "defaulttime": {
		//				if (value == null) {
		//					value = "gregorian";
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value + "\\']/timeFormats/default", "choice", "default");
		//				break;
		//			}
		//			case "time": {
		//				if (StringUtils.isEmpty(value)) {
		//					value = Arrays.asList("gregorian","medium");
		//				}
		//
		//				if (!(is_array(value))) {
		//					temp = value;
		//					value = Arrays.asList("gregorian",temp);
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value.get(0) + "\\']/timeFormats/timeFormatLength[@type=\\'" + value.get(1) + "\\']/timeFormat/pattern", "", "pattern");
		//				break;
		//			}
		//			case "datetime": {
		//				if (value == null) {
		//					value = Arrays.asList("gregorian","medium");
		//				}
		//
		//				if (!(is_array(value))) {
		//					temp = value;
		//					value = Arrays.asList("gregorian",temp);
		//				}
		//
		//				date = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value.get(0) + "\\']/dateFormats/dateFormatLength[@type=\\'" + value.get(1) + "\\']/dateFormat/pattern", "", "pattern");
		//				time = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value.get(0) + "\\']/timeFormats/timeFormatLength[@type=\\'" + value.get(1) + "\\']/timeFormat/pattern", "", "pattern");
		//				datetime = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value.get(0) + "\\']/dateTimeFormats/dateTimeFormatLength[@type=\\'" + value.get(1) + "\\']/dateTimeFormat/pattern", "", "pattern");
		//				temp = current(datetime).replace(Arrays.asList("{0}","{1}"), Arrays.asList(current(time),current(date)));
		//				break;
		//			}
		//			case "dateitem": {
		//				if (value == null) {
		//					value = Arrays.asList("gregorian","yyMMdd");
		//				}
		//
		//				if (!(is_array(value))) {
		//					temp = value;
		//					value = Arrays.asList("gregorian",temp);
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value.get(0) + "\\']/dateTimeFormats/availableFormats/dateFormatItem[@id=\\'" + value.get(1) + "\\']", "", false);
		//				break;
		//			}
		//			case "dateinterval": {
		//				if (value == null) {
		//					value = Arrays.asList("gregorian","yMd","y");
		//				}
		//
		//				if (!(is_array(value))) {
		//					temp = value;
		//					value = Arrays.asList("gregorian",temp,temp.get(0));
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value.get(0) + "\\']/dateTimeFormats/intervalFormats/intervalFormatItem[@id=\\'" + value.get(1) + "\\']/greatestDifference[@id=\\'" + value.get(2) + "\\']", "", false);
		//				break;
		//			}
		//			case "field": {
		//				if (!(is_array(value))) {
		//					temp = value;
		//					value = Arrays.asList("gregorian",temp);
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value.get(0) + "\\']/fields/field[@type=\\'" + value.get(1) + "\\']/displayName", "", value.get(1));
		//				break;
		//			}
		//			case "relative": {
		//				if (!(is_array(value))) {
		//					temp = value;
		//					value = Arrays.asList("gregorian",temp);
		//				}
		//
		//				temp = _getFile(locale, "/ldml/dates/calendars/calendar[@type=\\'" + value.get(0) + "\\']/fields/field/relative[@type=\\'" + value.get(1) + "\\']", "", value.get(1));
		//				break;
		//			}
		//			case "defaultnumberingsystem": {
		//				temp = _getFile(locale, "/ldml/numbers/defaultNumberingSystem", "", "default");
		//				break;
		//			}
		//			case "decimalnumber": {
		//				temp = _getFile(locale, "/ldml/numbers/decimalFormats/decimalFormatLength/decimalFormat/pattern", "", "default");
		//				break;
		//			}
		//			case "scientificnumber": {
		//				temp = _getFile(locale, "/ldml/numbers/scientificFormats/scientificFormatLength/scientificFormat/pattern", "", "default");
		//				break;
		//			}
		//			case "percentnumber": {
		//				temp = _getFile(locale, "/ldml/numbers/percentFormats/percentFormatLength/percentFormat/pattern", "", "default");
		//				break;
		//			}
		//			case "currencynumber": {
		//				temp = _getFile(locale, "/ldml/numbers/currencyFormats/currencyFormatLength/currencyFormat/pattern", "", "default");
		//				break;
		//			}
		//			case "nametocurrency": {
		//				temp = _getFile(locale, "/ldml/numbers/currencies/currency[@type=\\'" + value + "\\']/displayName", "", value);
		//				break;
		//			}
		//			case "currencytoname": {
		//				temp = _getFile(locale, "/ldml/numbers/currencies/currency[@type=\\'" + value + "\\']/displayName", "", value);
		//				_temp = _getFile(locale, "/ldml/numbers/currencies/currency", "type", false);
		//				temp = Collections.emptyMap();
		//				for( Map.Entry<Object,ERROR> each : _temp.entrySet() ) {
		//					Object key = each.getKey();
		//					ERROR keyvalue = each.getValue();
		//					val = _getFile(locale, "/ldml/numbers/currencies/currency[@type=\\'" + key + "\\']/displayName", "", key);
		//					if (!((val.get(key) != null) || (val.get(key) != value))) {
		//						continue;
		//					}
		//
		//					if (!(temp.get(val.get(key)) != null)) {
		//						temp.put(val.get(key), key);
		//					} else {
		//						temp.val.get(key) += (" " + key);
		//					}
		//
		//				}
		//
		//				break;
		//			}
		//			case "currencysymbol": {
		//				temp = _getFile(locale, "/ldml/numbers/currencies/currency[@type=\\'" + value + "\\']/symbol", "", value);
		//				break;
		//			}
		//			case "question": {
		//				temp = _getFile(locale, "/ldml/posix/messages/" + value + "str", "", value);
		//				break;
		//			}
		//			case "currencyfraction": {
		//				if (value == null) {
		//					value = "DEFAULT";
		//				}
		//
		//				temp = _getFile("supplementalData", "/supplementalData/currencyData/fractions/info[@iso4217=\\'" + value + "\\']", "digits", "digits");
		//				break;
		//			}
		//			case "currencyrounding": {
		//				if (StringUtils.isEmpty(value)) {
		//					value = "DEFAULT";
		//				}
		//
		//				temp = _getFile("supplementalData", "/supplementalData/currencyData/fractions/info[@iso4217=\\'" + value + "\\']", "rounding", "rounding");
		//				break;
		//			}
		//			case "currencytoregion": {
		//				temp = _getFile("supplementalData", "/supplementalData/currencyData/region[@iso3166=\\'" + value + "\\']/currency", "iso4217", value);
		//				break;
		//			}
		//			case "regiontocurrency": {
		//				_temp = _getFile("supplementalData", "/supplementalData/currencyData/region", "iso3166", false);
		//				temp = Collections.emptyMap();
		//				for( Map.Entry<Object,ERROR> each : _temp.entrySet() ) {
		//					Object key = each.getKey();
		//					ERROR keyvalue = each.getValue();
		//					val = _getFile("supplementalData", "/supplementalData/currencyData/region[@iso3166=\\'" + key + "\\']/currency", "iso4217", key);
		//					if (!((val.get(key) != null) || (val.get(key) != value))) {
		//						continue;
		//					}
		//
		//					if (!(temp.get(val.get(key)) != null)) {
		//						temp.put(val.get(key), key);
		//					} else {
		//						temp.val.get(key) += (" " + key);
		//					}
		//
		//				}
		//
		//				break;
		//			}
		//			case "regiontoterritory": {
		//				temp = _getFile("supplementalData", "/supplementalData/territoryContainment/group[@type=\\'" + value + "\\']", "contains", value);
		//				break;
		//			}
		//			case "territorytoregion": {
		//				_temp2 = _getFile("supplementalData", "/supplementalData/territoryContainment/group", "type", false);
		//				_temp = Collections.emptyMap();
		//				for( Map.Entry<Object,String> each : _temp2.entrySet() ) {
		//					Object key = each.getKey();
		//					String found = each.getValue();
		//					_temp += _getFile("supplementalData", "/supplementalData/territoryContainment/group[@type=\\'" + key + "\\']", "contains", key);
		//				}
		//
		//				temp = Collections.emptyMap();
		//				for( Map.Entry<Object,String> each : _temp.entrySet() ) {
		//					Object key = each.getKey();
		//					String found = each.getValue();
		//					_temp3 = found.split(" ");
		//					for( Object found3 : _temp3 ) {
		//						if (found3 != value) {
		//							continue;
		//						}
		//
		//						if (!(temp.get(found3) != null)) {
		//							temp.put(found3, (String)key);
		//						} else {
		//							temp.found3 += (" " + key);
		//						}
		//
		//					}
		//
		//				}
		//
		//				break;
		//			}
		//			case "scripttolanguage": {
		//				temp = _getFile("supplementalData", "/supplementalData/languageData/language[@type=\\'" + value + "\\']", "scripts", value);
		//				break;
		//			}
		//			case "languagetoscript": {
		//				_temp2 = _getFile("supplementalData", "/supplementalData/languageData/language", "type", false);
		//				_temp = Collections.emptyMap();
		//				for( Map.Entry<Object,String> each : _temp2.entrySet() ) {
		//					Object key = each.getKey();
		//					String found = each.getValue();
		//					_temp += _getFile("supplementalData", "/supplementalData/languageData/language[@type=\\'" + key + "\\']", "scripts", key);
		//				}
		//
		//				temp = Collections.emptyMap();
		//				for( Map.Entry<Object,String> each : _temp.entrySet() ) {
		//					Object key = each.getKey();
		//					String found = each.getValue();
		//					_temp3 = found.split(" ");
		//					for( Object found3 : _temp3 ) {
		//						if (found3 != value) {
		//							continue;
		//						}
		//
		//						if (!(temp.get(found3) != null)) {
		//							temp.put(found3, (String)key);
		//						} else {
		//							temp.found3 += (" " + key);
		//						}
		//
		//					}
		//
		//				}
		//
		//				break;
		//			}
		//			case "territorytolanguage": {
		//				temp = _getFile("supplementalData", "/supplementalData/languageData/language[@type=\\'" + value + "\\']", "territories", value);
		//				break;
		//			}
		//			case "languagetoterritory": {
		//				_temp2 = _getFile("supplementalData", "/supplementalData/languageData/language", "type", false);
		//				_temp = Collections.emptyMap();
		//				for( Map.Entry<Object,String> each : _temp2.entrySet() ) {
		//					Object key = each.getKey();
		//					String found = each.getValue();
		//					_temp += _getFile("supplementalData", "/supplementalData/languageData/language[@type=\\'" + key + "\\']", "territories", key);
		//				}
		//
		//				temp = Collections.emptyMap();
		//				for( Map.Entry<Object,String> each : _temp.entrySet() ) {
		//					Object key = each.getKey();
		//					String found = each.getValue();
		//					_temp3 = found.split(" ");
		//					for( Object found3 : _temp3 ) {
		//						if (found3 != value) {
		//							continue;
		//						}
		//
		//						if (!(temp.get(found3) != null)) {
		//							temp.put(found3, (String)key);
		//						} else {
		//							temp.found3 += (" " + key);
		//						}
		//
		//					}
		//
		//				}
		//
		//				break;
		//			}
		//			case "timezonetowindows": {
		//				temp = _getFile("supplementalData", "/supplementalData/timezoneData/mapTimezones[@type=\\'windows\\']/mapZone[@other=\\'" + value + "\\']", "type", value);
		//				break;
		//			}
		//			case "windowstotimezone": {
		//				temp = _getFile("supplementalData", "/supplementalData/timezoneData/mapTimezones[@type=\\'windows\\']/mapZone[@type=\\'" + value + "\\']", "other", value);
		//				break;
		//			}
		//			case "territorytotimezone": {
		//				temp = _getFile("supplementalData", "/supplementalData/timezoneData/zoneFormatting/zoneItem[@type=\\'" + value + "\\']", "territory", value);
		//				break;
		//			}
		//			case "timezonetoterritory": {
		//				temp = _getFile("supplementalData", "/supplementalData/timezoneData/zoneFormatting/zoneItem[@territory=\\'" + value + "\\']", "type", value);
		//				break;
		//			}
		//			case "citytotimezone": {
		//				temp = _getFile(locale, "/ldml/dates/timeZoneNames/zone[@type=\\'" + value + "\\']/exemplarCity", "", value);
		//				break;
		//			}
		//			case "timezonetocity": {
		//				_temp = _getFile(locale, "/ldml/dates/timeZoneNames/zone", "type", false);
		//				temp = Collections.emptyMap();
		//				for( Map.Entry<Object,String> each : _temp.entrySet() ) {
		//					Object key = each.getKey();
		//					String found = each.getValue();
		//					temp += _getFile(locale, "/ldml/dates/timeZoneNames/zone[@type=\\'" + key + "\\']/exemplarCity", "", key);
		//					if (!(temp.get(key) == null)) {
		//						if (temp.get(key) == value) {
		//							temp.put(temp.get(key), key);
		//						}
		//
		//					}
		//
		//					unset(temp.key);
		//				}
		//
		//				break;
		//			}
		//			case "phonetoterritory": {
		//				temp = _getFile("telephoneCodeData", "/supplementalData/telephoneCodeData/codesByTerritory[@territory=\\'" + value + "\\']/telephoneCountryCode", "code", value);
		//				break;
		//			}
		//			case "territorytophone": {
		//				_temp2 = _getFile("telephoneCodeData", "/supplementalData/telephoneCodeData/codesByTerritory", "territory", false);
		//				_temp = Collections.emptyMap();
		//				for( Map.Entry<Object,String> each : _temp2.entrySet() ) {
		//					Object key = each.getKey();
		//					String found = each.getValue();
		//					_temp += _getFile("telephoneCodeData", "/supplementalData/telephoneCodeData/codesByTerritory[@territory=\\'" + key + "\\']/telephoneCountryCode", "code", key);
		//				}
		//
		//				temp = Collections.emptyMap();
		//				for( Map.Entry<Object,String> each : _temp.entrySet() ) {
		//					Object key = each.getKey();
		//					String found = each.getValue();
		//					_temp3 = found.split(" ");
		//					for( Object found3 : _temp3 ) {
		//						if (found3 != value) {
		//							continue;
		//						}
		//
		//						if (!(temp.get(found3) != null)) {
		//							temp.put(found3, (String)key);
		//						} else {
		//							temp.found3 += (" " + key);
		//						}
		//
		//					}
		//
		//				}
		//
		//				break;
		//			}
		//			case "numerictoterritory": {
		//				temp = _getFile("supplementalData", "/supplementalData/codeMappings/territoryCodes[@type=\\'" + value + "\\']", "numeric", value);
		//				break;
		//			}
		//			case "territorytonumeric": {
		//				temp = _getFile("supplementalData", "/supplementalData/codeMappings/territoryCodes[@numeric=\\'" + value + "\\']", "type", value);
		//				break;
		//			}
		//			case "alpha3toterritory": {
		//				temp = _getFile("supplementalData", "/supplementalData/codeMappings/territoryCodes[@type=\\'" + value + "\\']", "alpha3", value);
		//				break;
		//			}
		//			case "territorytoalpha3": {
		//				temp = _getFile("supplementalData", "/supplementalData/codeMappings/territoryCodes[@alpha3=\\'" + value + "\\']", "type", value);
		//				break;
		//			}
		//			case "postaltoterritory": {
		//				temp = _getFile("postalCodeData", "/supplementalData/postalCodeData/postCodeRegex[@territoryId=\\'" + value + "\\']", "territoryId", false);
		//				break;
		//			}
		//			case "numberingsystem": {
		//				temp = _getFile("numberingSystems", "/supplementalData/numberingSystems/numberingSystem[@id=\\'" + value.toLowerCase() + "\\']", "digits", value);
		//				break;
		//			}
		//			case "chartofallback": {
		//				_temp = _getFile("characters", "/supplementalData/characters/character-fallback/character", "value", false);
		//				for( Map.Entry<Object,ERROR> each : _temp.entrySet() ) {
		//					Object key = each.getKey();
		//					ERROR keyvalue = each.getValue();
		//					temp2 = _getFile("characters", "/supplementalData/characters/character-fallback/character[@value=\\'" + key + "\\']/substitute", "", key);
		//					if (current(temp2) == value) {
		//						temp = key;
		//					}
		//
		//				}
		//
		//				break;
		//				temp = _getFile("characters", "/supplementalData/characters/character-fallback/character[@value=\\'" + value + "\\']/substitute", "", value);
		//				break;
		//			}
		//			case "fallbacktochar": {
		//				temp = _getFile("characters", "/supplementalData/characters/character-fallback/character[@value=\\'" + value + "\\']/substitute", "", false);
		//				break;
		//			}
		//			case "localeupgrade": {
		//				temp = _getFile("likelySubtags", "/supplementalData/likelySubtags/likelySubtag[@from=\\'" + value + "\\']", "to", value);
		//				break;
		//			}
		//			case "unit": {
		//				temp = _getFile(locale, "/ldml/units/unit[@type=\\'" + value.0 + "\\']/unitPattern[@count=\\'" + value.1 + "\\']", "", false);
		//				break;
		//			}
		//			default: {
		//				throw new Zend_Locale_Exception("Unknown detail ($path) for parsing locale data.");
		//				break;
		//			}
		//		}
		//
		//		if (is_array(temp)) {
		//			temp = current(temp);
		//		}
		//
		//		if (_cache != null) {
		//			if (_cacheTags) {
		//				_cache.save(serialize(temp), id, Collections.singletonList("Zend_Locale"));
		//			} else {
		//				_cache.save(serialize(temp), id);
		//			}
		//
		//		}
		//
		//		return temp;
	}

	/**
	 * Returns the set cache
	 * 
	 * @return Zend_Cache_Core  The set cache
	 */
	public static Zend_Cache_Core getCache() {
		return _cache;
	}

	/**
	 * Set a cache for Zend_Locale_Data
	 * 
	 * @param cache frontend 
	 */
	public static void setCache(Zend_Cache_Core cache) {
		_cache = cache;
		_getTagSupportForCache();
	}

	/**
	 * Returns true when a cache is set
	 * 
	 * @return boolean
	 */
	public static boolean hasCache() {
		if (_cache != null) {
			return true;
		}

		return false;
	}

	/**
	 * Removes any set cache
	 * 
	 * @return void
	 */
	public static void removeCache() {
		_cache = null;
	}

	/**
	 * Clears all set cache data
	 * 
	 * @return void
	 */
	public static void clearCache() {
		if (_cacheTags) {
			_cache.clean(Zend_Cache.CLEANING_MODE_MATCHING_TAG, Collections.singletonList("Zend_Locale"));
		} else {
			_cache.clean(Zend_Cache.CLEANING_MODE_ALL, null);
		}

	}

	/**
	 * Disables the cache
	 * 
	 * @param flag
	 */
	public static void disableCache(boolean flag) {
		_cacheDisabled = flag;
	}

	/**
	 * Internal method to check if the given cache supports tags
	 * 
	 * @param cache
	 */
	private static boolean _getTagSupportForCache() {
		throw new UnsupportedOperationException();
//		Map cacheOptions;
//		NoType backend;
//		backend = _cache.getBackend();
//		if (backend instanceof Zend_Cache_Backend_ExtendedInterface) {
//			cacheOptions = backend.getCapabilities();
//			_cacheTags = cacheOptions.get("tags");
//		} else {
//			_cacheTags = false;
//		}
//
//		return _cacheTags;
	}

}