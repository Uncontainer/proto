package com.naver.mage4j.core.mage.core.helper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.Mage_Core_Exception;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Cache;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Encryption;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Locale;
import com.naver.mage4j.core.mage.core.model.resource.store.Store;
import com.naver.mage4j.core.mage.core.model.resource.store.StoreContext;
import com.naver.mage4j.external.varien.simplexml.SimplexmlUtils;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;

/**
 * Core data helper
 */
public class Mage_Core_Helper_Data extends Mage_Core_Helper_Abstract {
	public static final String XML_PATH_DEFAULT_COUNTRY = "general/country/default";
	public static final String XML_PATH_PROTECTED_FILE_EXTENSIONS = "general/file/protected_extensions";
	public static final String XML_PATH_PUBLIC_FILES_VALID_PATHS = "general/file/public_files_valid_paths";
	public static final String XML_PATH_ENCRYPTION_MODEL = "global/helpers/core/encryption_model";
	public static final String XML_PATH_DEV_ALLOW_IPS = "dev/restrict/allow_ips";
	public static final String XML_PATH_CACHE_BETA_TYPES = "global/cache/betatypes";
	public static final String XML_PATH_CONNECTION_TYPE = "global/resources/default_setup/connection/type";
	public static final String CHARS_LOWERS = "abcdefghijklmnopqrstuvwxyz";
	public static final String CHARS_UPPERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String CHARS_DIGITS = "0123456789";
	public static final String CHARS_SPECIALS = "!$*+-+=?@^_|~";
	public static final String CHARS_PASSWORD_LOWERS = "abcdefghjkmnpqrstuvwxyz";
	public static final String CHARS_PASSWORD_UPPERS = "ABCDEFGHJKLMNPQRSTUVWXYZ";
	public static final String CHARS_PASSWORD_DIGITS = "23456789";
	public static final String CHARS_PASSWORD_SPECIALS = "!$*-+=?@_";
	public static final String XML_PATH_MERCHANT_COUNTRY_CODE = "general/store_information/merchant_country";
	public static final String XML_PATH_MERCHANT_VAT_NUMBER = "general/store_information/merchant_vat_number";
	public static final String XML_PATH_EU_COUNTRIES_LIST = "general/country/eu_countries";
	public static final int DIVIDE_EPSILON = 10000;

	protected Mage_Core_Model_Encryption _encryptor = null;

	protected List<String> _allowedFormats = Arrays.asList(
		Mage_Core_Model_Locale.FORMAT_TYPE_FULL,
		Mage_Core_Model_Locale.FORMAT_TYPE_LONG,
		Mage_Core_Model_Locale.FORMAT_TYPE_MEDIUM,
		Mage_Core_Model_Locale.FORMAT_TYPE_SHORT);

	/**
	 * @return Mage_Core_Model_Encryption
	 */
	//	public Mage_Core_Model_Encryption getEncryptor() {
	//		if (this._encryptor == null) {
	//			String encryptionModel = AppContext.getCurrent().getConfig().getNode(XML_PATH_ENCRYPTION_MODEL).getText();
	//			if (StringUtils.isNotEmpty(encryptionModel)) {
	//				this._encryptor = MageInstanceLoader.get().getInstance(encryptionModel);
	//			} else {
	//				this._encryptor = Mage.getModel("core/encryption");
	//			}
	//
	//			this._encryptor.setHelper(this);
	//		}
	//
	//		return this._encryptor;
	//	}

	/**
	 * Convert and format price value for current application store
	 * 
	 * @param value
	 * @param format
	 * @param includeContainer
	 * @return mixed
	 */
	//	public static Object currency(double value, boolean format/* = true */, boolean includeContainer/* = true */) {
	//		return currencyByStore(value, null, format, includeContainer);
	//	}

	/**
	 * Convert and format price value for specified store
	 * 
	 * @param value
	 * @param store
	 * @param format
	 * @param includeContainer
	 * @return mixed
	 */
	//	public static Object currencyByStore(double value, Store store/* = null */, boolean format/* = true */, boolean includeContainer/* = true */) {
	//		try {
	//			return defaultStore(store).getHelper().convertPrice(value, format, includeContainer);
	//		} catch (Exception e) {
	//			return e.getMessage();
	//		}
	//	}

	/**
	 * Format and convert currency using current store option
	 * 
	 * @param value
	 * @param includeContainer
	 * @return string
	 */
	//	public Object formatCurrency(float value, boolean includeContainer/* = true */) {
	//		return this.currency(value, true, includeContainer);
	//	}

	/**
	 * Formats price
	 * 
	 * @param price
	 * @param includeContainer
	 * @return string
	 */
	//	public String formatPrice(float price, boolean includeContainer/* = true */) {
	//		return AppContext.getCurrent().getStore().getHelper().formatPrice(price, includeContainer);
	//	}

	/**
	 * Format date using current locale options and time zone.
	 * 
	 * @param dateString
	 * @param  constants 
	 * @param  time 
	 * @return string
	 */
	//	public String formatDate(String dateString/* = null */, String format/* = Mage_Core_Model_Locale.FORMAT_TYPE_SHORT */, boolean showTime/* = false */) {
	//		Zend_Date date;
	//		if (dateString != null) {
	//			try {
	//				long timestamp = PhpDateUtils.parseAndGetTime(dateString);
	//				date = AppContext.getCurrent().getLocale().date(timestamp);
	//			} catch (Exception e) {
	//				log.info("Invalid dateString: {}", new Object[] {dateString, e});
	//				return "";
	//			}
	//		} else {
	//			date = null;
	//		}
	//
	//		return formatDate(date, format, showTime);
	//	}

	//	public String formatDate(Zend_Date date/* = null */, String format/* = Mage_Core_Model_Locale.FORMAT_TYPE_SHORT */, boolean showTime/* = false */) {
	//		if (!this._allowedFormats.contains(format)) {
	//			throw new IllegalArgumentException("Not allowed date format.(" + format + ")");
	//		}
	//
	//		if (date == null) {
	//			date = AppContext.getCurrent().getLocale().date(Mage.getSingleton("core/date").gmtTimestamp(), null, null);
	//		}
	//
	//		if (showTime) {
	//			format = AppContext.getCurrent().getLocale().getDateTimeFormat(format);
	//		} else {
	//			format = AppContext.getCurrent().getLocale().getDateFormat(format);
	//		}
	//
	//		return date.toString(format);
	//	}

	/**
	 * Format time using current locale options
	 * 
	 * @param time
	 * @param  $format 
	 * @param  $showDate 
	 * @return string
	 */
	//	public String formatTime(String time/* = null */, String format/* = Mage_Core_Model_Locale.FORMAT_TYPE_SHORT */, boolean showDate/* = false */) {
	//		if (!this._allowedFormats.contains(format)) {
	//			return time;
	//		}
	//
	//		NoType date;
	//		if (time == null) {
	//			date = AppContext.getCurrent().getLocale().date(time());
	//		} else {
	//			if (time instanceof Zend_Date) {
	//				date = time;
	//			} else {
	//				date = AppContext.getCurrent().getLocale().date(DateUtil.parseAndGetTime(time));
	//			}
	//		}
	//
	//		if (showDate) {
	//			format = AppContext.getCurrent().getLocale().getDateTimeFormat(format);
	//		} else {
	//			format = AppContext.getCurrent().getLocale().getTimeFormat(format);
	//		}
	//
	//		return date.toString(format);
	//	}

	/**
	 * Encrypt data using application key
	 * 
	 * @param data
	 * @return string
	 */
	//	public String encrypt(String data) {
	//		if (!AppContext.getCurrent().isInstalled()) {
	//			return data;
	//		}
	//
	//		return this.getEncryptor().encrypt(data);
	//	}

	/**
	 * Decrypt data using application key
	 * 
	 * @param data
	 * @return string
	 */
	//	public String decrypt(String data) {
	//		if (!AppContext.getCurrent().isInstalled()) {
	//			return data;
	//		}
	//
	//		return this.getEncryptor().decrypt(data);
	//	}

	/**
	 * Decrypt data using application key
	 * 
	 * @param data
	 * @return string
	 */
	//	public Varien_Crypt_Mcrypt validateKey(String key) {
	//		return this.getEncryptor().validateKey(key);
	//	}

	public String getRandomString(int len, String chars/* = null */) {
		if (chars == null) {
			chars = (CHARS_LOWERS + CHARS_UPPERS + CHARS_DIGITS);
		}

		StringBuilder result = new StringBuilder(len);
		Random rand = new Random();
		for (int i = 0; i < len; i++) {
			int index = rand.nextInt(chars.length());
			result.append(chars.charAt(index));
		}

		return result.toString();
	}

	/**
	 * Generate salted hash from password
	 * 
	 * @param password
	 * @param salt
	 */
	//	public String getHash(String password, String salt/* = false */) {
	//		return this.getEncryptor().getHash(password, salt);
	//	}
	//
	//	public boolean validateHash(String password, String hash) {
	//		return this.getEncryptor().validateHash(password, hash);
	//	}

	/**
	 * Retrieve store identifier
	 * 
	 * @param store
	 * @return int
	 */
	public Short getStoreId(Store store/* = null */) {
		return defaultStore(store).getStoreId();
	}

	public String removeAccents(String string, boolean german/* = false */) {
		return StringUtils.stripAccents(string);
	}

	/**
	 * Retrieve store identifier
	 * 
	 * @param store
	 * @return int
	 */
	public boolean isDevAllowed(Store store) {
		String allowedIpsString = defaultStore(store).getHelper().getConfigAsString(XML_PATH_DEV_ALLOW_IPS, null);
		Mage_Core_Helper_Http helperHttp = AppContext.getCurrent().getHelperHttp();
		String remoteAddr = helperHttp.getRemoteAddr();

		boolean allow = true;
		if (allowedIpsString != null && remoteAddr != null) {
			String[] allowedIps = allowedIpsString.split("\\s*,\\s*");
			if (!ArrayUtils.contains(allowedIps, remoteAddr) && !ArrayUtils.contains(allowedIps, helperHttp.getHttpHost(true))) {
				allow = false;
			}
		}

		return allow;
	}

	public boolean isDevAllowed(String code/* = null */) {
		return isDevAllowed(defaultStore(code));
	}

	/**
	 * Get information about available cache types
	 * 
	 * @return array
	 */
	public Map<String, String> getCacheTypes() {
		Map<String, String> types = new HashMap<String, String>();
		Simplexml_Element config = AppContext.getCurrent().getConfig().getNode(Mage_Core_Model_Cache.XML_PATH_TYPES);
		for (Simplexml_Element each : config.children()) {
			String type = each.getName();
			// TODO label이 attribute or element에 명시되는지 확인.
			types.put(type, each.getString("label"));
		}

		return types;
	}

	/**
	 * Get information about available cache beta types
	 * 
	 * @return array
	 */
	public Map getCacheBetaTypes() {
		Map<String, String> types = new HashMap<String, String>();
		Simplexml_Element config = AppContext.getCurrent().getConfig().getNode(XML_PATH_CACHE_BETA_TYPES);
		if (SimplexmlUtils.isNotNull(config)) {
			for (Simplexml_Element each : config.children()) {
				String type = each.getName();
				types.put(type, each.getString("label"));
			}
		}

		return types;
	}

	/**
	 * Copy data from object|array to object|array containing fieldsfrom fieldset matching an aspect.Contents of $aspect are a field name in target object or array.If '*' - will be used the same name as in the source object or array.
	 * 
	 * @param fieldset
	 * @param aspect
	 * @param source
	 * @param target
	 * @param root
	 * @return boolean
	 */
	//	public boolean copyFieldset(String fieldset, String aspect, Map source, Map target, String root/* = "global" */) {
	//		Map<String, Object> fields = AppContext.getCurrent().getConfig().getFieldset(fieldset, root);
	//		if (!(fields)) {
	//			return false;
	//		}
	//
	//		boolean sourceIsArray = is_array(source);
	//		boolean targetIsArray = is_array(target);
	//		boolean result = false;
	//		for (Map.Entry<String, Map> each : fields.entrySet()) {
	//			String code = each.getKey();
	//			Map node = each.getValue();
	//			if (node.get(aspect) == null) {
	//				continue;
	//			}
	//
	//			Object value;
	//			if (sourceIsArray) {
	//				value = ((source.get(code) != null) ? (source.get(code)) : (null));
	//			} else {
	//				value = source.get(getDataUsingMethod(code));
	//			}
	//
	//			Object targetCode = (node.get(aspect));
	//			targetCode = ((targetCode == "*") ? (code) : (targetCode));
	//			if (targetIsArray) {
	//				target.put(targetCode, value);
	//			} else {
	//				target.setDataUsingMethod(targetCode, value);
	//			}
	//
	//			result = true;
	//		}
	//
	//		String eventName = String.format("core_copy_fieldset_%s_%s", fieldset, aspect);
	//		AppContext.getCurrent().getEventDispatcher().dispatchEvent(eventName, MageAtomArray.createMap(new Object[] {"target", target}, new Object[] {"source", source}, new Object[] {"root", root}));
	//		return result;
	//	}

	/**
	 * Decorate a plain array of arrays or objectsThe array actually can be an object with Iterator interfaceKeys with prefix_* will be set:*_is_first - if the element is first*_is_odd / *_is_even - for odd/even elements*_is_last - if the element is lastThe respective key/attribute will be set to element, depending on object it is or array.Varien_Object is supported.$forceSetAll true will cause to set all possible values for all elements.When false (default), only non-empty values will be set.
	 * 
	 * @param array
	 * @param prefix
	 * @param forceSetAll
	 * @return mixed
	 */
	//	public Map decorateArray(Map<String, Object> array, String prefix/* = "decorated_" */, boolean forceSetAll/* = false */) {
	//		String keyIsFirst = prefix + "is_first";
	//		String keyIsOdd = prefix + "is_odd";
	//		String keyIsEven = prefix + "is_even";
	//		String keyIsLast = prefix + "is_last";
	//		int count = array.size();
	//		int i = 0;
	//		boolean isEven = false;
	//		for (Map.Entry<String, Object> each : array.entrySet()) {
	//			String key = each.getKey();
	//			Object element = each.getValue();
	//			if (is_object(element)) {
	//				this._decorateArrayObject(element, keyIsFirst, 0 == i, forceSetAll || (0 == i));
	//				this._decorateArrayObject(element, keyIsOdd, !(isEven), forceSetAll || (!(isEven)));
	//				this._decorateArrayObject(element, keyIsEven, isEven, forceSetAll || isEven);
	//				isEven = (!(isEven));
	//				(i)++;
	//				this._decorateArrayObject(element, keyIsLast, i == count, forceSetAll || (i == count));
	//			} else if (is_array(element)) {
	//				if (forceSetAll || (0 == i)) {
	//					array.get(key).put(keyIsFirst, 0 == i);
	//				}
	//
	//				if (forceSetAll || (!(isEven))) {
	//					array.get(key).put(keyIsOdd, !(isEven));
	//				}
	//
	//				if (forceSetAll || isEven) {
	//					array.get(key).put(keyIsEven, isEven);
	//				}
	//
	//				isEven = (!(isEven));
	//				(i)++;
	//				if (forceSetAll || (i == count)) {
	//					array.get(key).put(keyIsLast, i == count);
	//				}
	//			}
	//		}
	//
	//		return array;
	//	}

	/**
	 * Decorate a plain array of arrays or objectsThe array actually can be an object with Iterator interfaceKeys with prefix_* will be set:*_is_first - if the element is first*_is_odd / *_is_even - for odd/even elements*_is_last - if the element is lastThe respective key/attribute will be set to element, depending on object it is or array.Varien_Object is supported.$forceSetAll true will cause to set all possible values for all elements.When false (default), only non-empty values will be set.
	 * 
	 * @param array
	 * @param prefix
	 * @param forceSetAll
	 * @return mixed
	 */
	//	private void _decorateArrayObject(Map element, String key, Object value, boolean dontSkip) {
	//		if (dontSkip) {
	//			if (element instanceof Varien_Object) {
	//				element.setData(key, value);
	//			} else {
	//				element.put(key, value);
	//			}
	//		}
	//	}

	/**
	 * Transform an assoc array to SimpleXMLElement objectArray has some limitations. Appropriate exceptions will be thrown
	 * 
	 * @param array
	 * @param rootName
	 * @return SimpleXMLElement
	 */
	//	public SimpleXMLElement assocToXml(Map array, String rootName/* = "_" */) {
	//		String xmlstr;
	//		SimpleXMLElement xml;
	//		NoType value;
	//		NoType key;
	//		if (StringUtils.isEmpty(rootName) || NumberUtils.isDigits(rootName)) {
	//			throw new Exception("Root element must not be empty or numeric");
	//		}
	//
	//		xmlstr = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>" +
	//			"<$rootName></$rootName>";
	//		xml = (new SimpleXMLElement(xmlstr));
	//		for (Map.Entry<ERROR, ERROR> each : array.entrySet()) {
	//			ERROR key = each.getKey();
	//			ERROR value = each.getValue();
	//			if (is_numeric(key)) {
	//				throw new Exception("Array root keys must not be numeric.");
	//			}
	//
	//		}
	//
	//		return _assocToXml(array, rootName, xml);
	//	}

	/**
	 * Function, that actually recursively transforms array to xml
	 * 
	 * @param array
	 * @param rootName
	 * @param xml
	 * @return SimpleXMLElement
	 */
	//	private String _assocToXml(Map array, String rootName, String xml) {
	//		boolean hasNumericKey;
	//		Map value;
	//		boolean hasStringKey;
	//		String key;
	//		hasNumericKey = false;
	//		hasStringKey = false;
	//		for (Map.Entry<String, Map> each : array.entrySet()) {
	//			String key = each.getKey();
	//			Map value = each.getValue();
	//			if (!(is_array(value))) {
	//				if (is_string(key)) {
	//					if (key == rootName) {
	//						throw new Exception("Associative key must not be the same as its parent associative key.");
	//					}
	//
	//					hasStringKey = true;
	//					xml.key = value;
	//				} else if (is_int(key)) {
	//					hasNumericKey = true;
	//					xml.rootName.key = value;
	//				}
	//
	//			} else {
	//				_assocToXml(value, key, xml.key);
	//			}
	//
	//		}
	//
	//		if (hasNumericKey && hasStringKey) {
	//			throw new Exception("Associative and numeric keys must not be mixed at one level.");
	//		}
	//
	//		return xml;
	//	}

	/**
	 * Transform SimpleXMLElement to associative arraySimpleXMLElement must be conform structure, generated by assocToXml()
	 * 
	 * @param xml
	 * @return array
	 */
	//	public Map xmlToAssoc(Map xml) {
	//		Map array = Collections.emptyMap();
	//		for (Map.Entry<String, Map> each : xml.entrySet()) {
	//			String key = each.getKey();
	//			Map value = each.getValue();
	//			if (value.get(key) != null) {
	//				int i = 0;
	//				for (ERROR v : value.get(key)) {
	//					array.get(key).put((i)++, (String)v);
	//				}
	//			} else {
	//				array.put(key, trim((String)value));
	//				if ((array.key == null) && (!(value == null))) {
	//					array.key = xmlToAssoc(value);
	//				} else {
	//					array.key = ((String)value);
	//				}
	//
	//			}
	//
	//		}
	//
	//		return array;
	//	}

	/**
	 * Encode the mixed $valueToEncode into the JSON format
	 * 
	 * @param valueToEncode
	 * @param cycleCheck default 
	 * @param options encoding 
	 * @return string
	 */
	//	public String jsonEncode(Object valueToEncode, boolean cycleCheck/* = false */, Map options/* = Collections.emptyMap() */) {
	//		String json = Zend_Json.encode(valueToEncode, cycleCheck, options);
	//		NoType inline = Mage.getSingleton("core/translate_inline");
	//		if (inline.isAllowed()) {
	//			inline.setIsJson(true);
	//			inline.processResponseBody(json);
	//			inline.setIsJson(false);
	//		}
	//
	//		return json;
	//	}

	/**
	 * Decodes the given $encodedValue string which isencoded in the JSON format
	 * 
	 * @param encodedValue
	 * @return mixed
	 */
	//	public Object jsonDecode(String encodedValue, int objectDecodeType/* = Zend_Json.TYPE_ARRAY */) {
	//		return Zend_Json.decode(encodedValue, objectDecodeType);
	//	}

	/**
	 * Generate a hash from unique ID
	 * 
	 * @param null
	 * @return string
	 */
	//	public String uniqHash(String prefix/* = "" */) {
	//		return prefix + md5(uniqid(microtime() + mt_rand(), true));
	//	}

	/**
	 * Merge specified files into oneBy default will not merge, if there is already merged file exists and itwas modified after its componentsIf target file is specified, will attempt to write merged contents into it,otherwise will return merged contentMay apply callback to each file contents. Callback gets parameters:(<existing system filename>, <file contents>)May filter files by specified extension(s)Returns false on error
	 * 
	 * @param srcFiles
	 * @param targetFile written 
	 * @param mustMerge
	 * @param beforeMergeCallback
	 * @param extensionsFilter
	 * @return bool|string
	 */
	//	public String mergeFiles(Object[] srcFiles, String targetFile/* = false */, boolean mustMerge/* = false */, callback beforeMergeCallback/* = null */, List extensionsFilter/* = Collections.emptyMap() */){
	//		try {
	//			boolean shouldMerge = (mustMerge || (!(targetFile)));
	//			if (!(shouldMerge)) {
	//				if (!(file_exists(targetFile))) {
	//					shouldMerge = true;
	//				} else {
	//					NoType targetMtime = filemtime(targetFile);
	//					for( ERROR file : srcFiles ) {
	//						if ((!(file_exists(file))) || ((@filemtime(file)) > targetMtime)) {
	//							shouldMerge = true;
	//							break;
	//						}
	//					}
	//				}
	//			}
	//
	//			if (shouldMerge) {
	//				if (targetFile && (!(is_writeable(dirname(targetFile))))) {
	//					throw new Exception(sprintf("Path %s is not writeable.", dirname(targetFile)));
	//				}
	//
	//				if (extensionsFilter) {
	//					if (!(is_array(extensionsFilter))) {
	//						extensionsFilter = Collections.singletonList(extensionsFilter);
	//					}
	//
	//					if (!(srcFiles == null)) {
	//						for( Map.Entry<ERROR,ERROR> each : srcFiles.entrySet() ) {
	//							ERROR key = each.getKey();
	//							ERROR file = each.getValue();
	//							String fileExt = pathinfo(file, PATHINFO_EXTENSION).toLowerCase();
	//							if (!(in_array(fileExt, extensionsFilter))) {
	//								unset(srcFiles.get(key));
	//							}
	//						}
	//					}
	//				}
	//
	//				if (srcFiles == null) {
	//					throw new Exception("No files to compile.");
	//				}
	//
	//				String data = "";
	//				for( ERROR file : srcFiles ) {
	//					if (!(file_exists(file))) {
	//						continue;
	//					}
	//
	//					NoType contents = (file_get_contents(file) + "\\n");
	//					if (beforeMergeCallback && is_callable(beforeMergeCallback)) {
	//						contents = call_user_func(beforeMergeCallback, file, contents);
	//					}
	//
	//					data += contents;
	//				}
	//
	//				if (!(data)) {
	//					throw new Exception(sprintf("No content found in files:\\n%s", StringUtils.join(srcFiles, "\\n")));
	//				}
	//
	//				if (targetFile) {
	//					file_put_contents(targetFile, data, LOCK_EX);
	//				} else {
	//					return data;
	//				}
	//
	//			}
	//
	//			return true;
	//		}  catch (Exception e) {
	//			Mage.logException(e);
	//		}
	//
	//
	//		return false;
	//	}

	/**
	 * Return default country code
	 * 
	 * @param store
	 * @return string
	 */
	public String getDefaultCountry(String store/* = null */) {
		return defaultStore(store).getHelper().getConfigAsString(XML_PATH_DEFAULT_COUNTRY, null);
	}

	/**
	 * Return list with protected file extensions
	 * 
	 * @param store
	 * @return array
	 */
	public String getProtectedFileExtensions(String store/* = null */) {
		return defaultStore(store).getHelper().getConfigAsString(XML_PATH_PROTECTED_FILE_EXTENSIONS, null);
	}

	/**
	 * Return list with public files valid paths
	 * 
	 * @return array
	 */
	public String getPublicFilesValidPath() {
		return AppContext.getCurrent().getStoreConfigAsString(XML_PATH_PUBLIC_FILES_VALID_PATHS);
	}

	/**
	 * Check LFI protection
	 * 
	 * @param name
	 * @return bool
	 */
	public boolean checkLfiProtection(String name) {
		if (name.matches("#\\.\\.[\\\\\\/]#")) {
			throw new Mage_Core_Exception(this.__("Requested file may not include parent directory traversal (\"../\", \"..\\\\\" notation)"));
		}

		return true;
	}

	/**
	 * Check whether database compatible mode is used (configs enable it for MySQL by default).
	 * 
	 * @return bool
	 */
	public boolean useDbCompatibleMode() {
		String connType = AppContext.getCurrent().getConfig().getNode(XML_PATH_CONNECTION_TYPE).getText();
		String path = "global/resource/connection/types/" + connType + "/compatibleMode";
		String value = AppContext.getCurrent().getConfig().getNode(path).getText();
		return StringUtils.isNotEmpty(value);
	}

	/**
	 * Retrieve merchant country code
	 * 
	 * @param store
	 * @return string
	 */
	public String getMerchantCountryCode(String store/* = null */) {
		return defaultStore(store).getHelper().getConfigAsString(XML_PATH_MERCHANT_COUNTRY_CODE, null);
	}

	/**
	 * Retrieve merchant VAT number
	 * 
	 * @param store
	 * @return string
	 */
	public String getMerchantVatNumber(String store/* = null */) {
		return defaultStore(store).getHelper().getConfigAsString(XML_PATH_MERCHANT_VAT_NUMBER, null);
	}

	/**
	 * Check whether specified country is in EU countries list
	 * 
	 * @param countryCode
	 * @param storeId
	 * @return bool
	 */
	public boolean isCountryInEU(String countryCode, String storeCode/* = null */) {
		String[] euCountries = defaultStore(storeCode).getHelper().getConfigAsString(XML_PATH_EU_COUNTRIES_LIST, "").split(",");
		return ArrayUtils.contains(euCountries, countryCode);
	}

	/**
	 * Returns the floating point remainder (modulo) of the division of the arguments
	 * 
	 * @param dividend
	 * @param divisor
	 * @return float|int
	 */
	//	public int getExactDivision(float dividend, float divisor) {
	//		float epsilon;
	//		int remainder;
	//		epsilon = (divisor / DIVIDE_EPSILON);
	//		remainder = fmod(dividend, divisor);
	//		if ((abs(remainder - divisor) < epsilon) || (abs(remainder) < epsilon)) {
	//			remainder = 0;
	//		}
	//
	//		return remainder;
	//	}

	private static Store defaultStore(Store store) {
		return store != null ? store : AppContext.getCurrent().getStore();
	}

	private Store defaultStore(String code) {
		return code != null ? StoreContext.getContext().getByCode(code) : AppContext.getCurrent().getStore();
	}
}