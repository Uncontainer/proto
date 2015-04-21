package com.naver.mage4j.core.mage.core.model.resource.store;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.Mage_Core_Exception;
import com.naver.mage4j.core.mage.core.Mage_Core_Model_App;
import com.naver.mage4j.core.mage.core.controller.request.Mage_Core_Controller_Request_Http.ServerOption;
import com.naver.mage4j.core.mage.core.helper.file.storage.Mage_Core_Helper_File_Storage_Database;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Config;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Url;
import com.naver.mage4j.core.mage.core.model.ScopeType;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;

public class StoreHelper {
	public static final String XML_PATH_STORE_STORE_NAME = "general/store_information/name";
	public static final String XML_PATH_STORE_STORE_PHONE = "general/store_information/phone";
	public static final String XML_PATH_STORE_IN_URL = "web/url/use_store";
	public static final String XML_PATH_USE_REWRITES = "web/seo/use_rewrites";
	public static final String XML_PATH_UNSECURE_BASE_URL = "web/unsecure/base_url";
	public static final String XML_PATH_SECURE_BASE_URL = "web/secure/base_url";
	public static final String XML_PATH_SECURE_IN_FRONTEND = "web/secure/use_in_frontend";
	public static final String XML_PATH_SECURE_IN_ADMINHTML = "web/secure/use_in_adminhtml";
	public static final String XML_PATH_SECURE_BASE_LINK_URL = "web/secure/base_link_url";
	public static final String XML_PATH_UNSECURE_BASE_LINK_URL = "web/unsecure/base_link_url";
	public static final String XML_PATH_OFFLOADER_HEADER = "web/secure/offloader_header";
	public static final String XML_PATH_PRICE_SCOPE = "catalog/price/scope";

	public static final String CACHE_TAG = "store";

	public static final String COOKIE_NAME = "store";
	public static final String COOKIE_CURRENCY = "currency";

	/**
	 * Script name, which returns all the images
	 */
	public static final String MEDIA_REWRITE_SCRIPT = "get.php/";

	protected final List<String> _configCacheBaseNodes;

	private final Store store;

	//	@Autowired
	//	private Mage_Core_Model_Resource_Store resource;
	//
	//	@Autowired
	//	private Mage_Core_Model_Resource_Store_Collection resourceCollection;

	public StoreHelper(Store store) {
		//		super("core/store", null);
		_configCacheBaseNodes = Collections.unmodifiableList(Arrays.asList(
			XML_PATH_PRICE_SCOPE,
			XML_PATH_SECURE_BASE_URL,
			XML_PATH_SECURE_IN_ADMINHTML,
			XML_PATH_SECURE_IN_FRONTEND,
			XML_PATH_STORE_IN_URL,
			XML_PATH_UNSECURE_BASE_URL,
			XML_PATH_USE_REWRITES,
			XML_PATH_UNSECURE_BASE_LINK_URL,
			XML_PATH_SECURE_BASE_LINK_URL,
			"general/locale/code"));
		this.store = store;
	}

	//	@Override
	//	public Mage_Core_Model_Resource_Store getResource() {
	//		return resource;
	//	}
	//
	//	@Override
	//	public Mage_Core_Model_Resource_Store_Collection getCollection() {
	//		return resourceCollection;
	//	}

	/**
	* Retrieve base URL
	*
	* @param string $type
	* @param boolean|null $secure
	* @return string
	*/
	public String getBaseUrl(UrlType type, Boolean secure) {
		//		String cacheKey = type + "/" + secure;
		String url;
		switch (type) {
			case WEB:
				if (secure == null) {
					secure = isCurrentlySecure();
				}
				url = (String)getConfig("web/" + (secure ? "secure" : "unsecure") + "/base_url");
				break;
			case LINK:
				if (secure == null) {
					secure = false;
				}
				url = (String)getConfig("web/" + (secure ? "secure" : "unsecure") + "/base_link_url");
				url = _updatePathUseRewrites(url);
				url = _updatePathUseStoreView(url);
				break;
			case DIRECT_LINK:
				if (secure == null) {
					secure = false;
				}
				url = (String)getConfig("web/" + (secure ? "secure" : "unsecure") + "/base_link_url");
				url = _updatePathUseRewrites(url);
				break;

			case SKIN:
			case JS:
				if (secure == null) {
					secure = isCurrentlySecure();
				}
				url = (String)getConfig("web/" + (secure ? "secure" : "unsecure") + "/base_" + type.getCode() + "_url");
				break;
			case MEDIA:
				url = _updateMediaPathUseRewrites(secure, UrlType.MEDIA);
				break;
			default:
				throw new Mage_Core_Exception("Invalid base url type.(" + type + ")");
		}

		if (url.contains("{{base_url}}")) {
			String baseUrl = AppContext.getCurrent().getConfig().substDistroServerVars("{{base_url}}");
			url = url.replace("{{base_url}}", baseUrl);
		}

		return url.endsWith("/") ? url : url + "/";
	}

	public String getBaseUrl() {
		return getBaseUrl(UrlType.LINK, null);
	}

	/**
	 * Remove script file name from url in case when server rewrites are enabled
	 */
	private String _updatePathUseRewrites(String url) {
		Mage_Core_Model_Config config = AppContext.getCurrent().getConfig();
		if (isAdmin() || getConfig(XML_PATH_USE_REWRITES) == null || !config.isInstalled()) {
			String indexFileName = _isCustomEntryPoint() ? "index.php" : FilenameUtils.getBaseName(AppContext.getCurrent().getRequest().getServerOption().getScriptFilename());
			url += indexFileName + "/";
		}
		return url;
	}

	/**
	 * Check if used entry point is custom
	 */
	protected boolean _isCustomEntryPoint() {
		Boolean result = (Boolean)Mage_Core_Model_App.registry("custom_entry_point");
		return result != null ? result : false;
	}

	/**
	 * Check if store is admin store
	 *
	 * @return unknown
	 */
	public boolean isAdmin() {
		return store.getStoreId() == Mage_Core_Model_App.ADMIN_STORE_ID;
	}

	/**
	 * Add store code to url in case if it is enabled in configuration
	 */
	private String _updatePathUseStoreView(String url) {
		if (getStoreInUrl()) {
			url += store.getCode() + "/";
		}
		return url;
	}

	/**
	* Returns whether url forming scheme prepends url path with store view code
	*/
	public boolean getStoreInUrl() {
		return AppContext.getCurrent().getConfig().isInstalled() && getConfig(XML_PATH_STORE_IN_URL) != null;
	}

	/**
	* Retrieve URL for media catalog
	*
	* If we use Database file storage and server doesn't support rewrites (.htaccess in media folder)
	* we have to put name of fetching media script exactly into URL
	*
	* @param null|boolean $secure
	* @param string $type
	* @return string
	*/
	private String _updateMediaPathUseRewrites(Boolean secure, UrlType type/* = self::URL_TYPE_MEDIA*/) {
		if (secure == null) {
			secure = isCurrentlySecure();
		}
		String secureStringFlag = secure ? "secure" : "unsecure";
		String url = (String)getConfig("web/" + secureStringFlag + "/base_" + type.getCode() + "_url");
		if (getConfig(XML_PATH_USE_REWRITES) == null && Mage_Core_Helper_File_Storage_Database.checkDbUsage()) {
			String urlStart = (String)getConfig("web/" + secureStringFlag + "/base_url");
			url = url.replace(urlStart, urlStart + MEDIA_REWRITE_SCRIPT);
		}
		return url;
	}

	/**
	 * Check if request was secure
	 *
	 * @return boolean
	 */
	public boolean isCurrentlySecure() {
		Mage_Core_Model_App app = AppContext.getCurrent();
		Mage_Core_Model_Config config = app.getConfig();
		ServerOption serverOption = app.getRequest().getServerOption();
		boolean standardRule = serverOption.isSecureSupported();
		String offloaderHeader = config.getNode(XML_PATH_OFFLOADER_HEADER, ScopeType.DEFAULT, null).getText();
		if (offloaderHeader != null) {
			offloaderHeader = offloaderHeader.trim();
		}

		if (StringUtils.isNotEmpty(offloaderHeader) && StringUtils.isNotBlank(app.getRequest().getHeader(offloaderHeader)) || standardRule) {
			return true;
		}

		if (config.isInstalled()) {
			String secureBaseUrl = (String)getConfig(Mage_Core_Model_Url.XML_PATH_SECURE_URL);

			if (secureBaseUrl == null) {
				return false;
			}

			URI uri;
			try {
				uri = new URI(secureBaseUrl);
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}

			return "https".equals(uri.getScheme()) && uri.getPort() == serverOption.getServerPort();
		} else {
			return 443 == serverOption.getServerPort();
		}
	}

	public Object getConfig(String path) {
		//		if (isset($this->_configCache[$path])) {
		//			return $this->_configCache[$path];
		//		}
		Mage_Core_Model_Config config = AppContext.getCurrent().getConfig();
		String fullPath = "stores/" + store.getCode() + "/" + path;
		Simplexml_Element data = config.getNode(fullPath);
		if (data.isNull()) {
			data = config.getNode("default/" + path);
		}
		if (data.isNull()) {
			return null;
		}

		return _processConfigValue(fullPath, path, data);
	}

	public String getConfigAsString(String path, String defaultValue) {
		String result = (String)getConfig(path);
		return (result != null) ? result : defaultValue;
	}

	@SuppressWarnings("unchecked")
	public <V> Map<String, V> getConfigAsMap(String path) {
		Map<String, V> result = (Map<String, V>)getConfig(path);
		return result != null ? result : Collections.<String, V> emptyMap();
	}

	/**
	 * Retrieve config flag for store by path
	 */
	public boolean getConfigFlag(String path) {
		String flag = (String)getConfig(path);

		return flag != null && !"false".equalsIgnoreCase(flag);
	}

	private Object _processConfigValue(String fullPath, String path, Simplexml_Element node) {
		//       if (isset($this->_configCache[$path])) {
		//           return $this->_configCache[$path];
		//       }
		List<Simplexml_Element> children = node.children();
		if (!children.isEmpty()) {
			Map<String, Object> mapResult = new HashMap<String, Object>();
			for (Simplexml_Element child : children) {
				String key = child.getName();
				Object value = _processConfigValue(fullPath + "/" + key, path + "/" + key, child);
				mapResult.put(key, value);
			}

			//           $this->_configCache[$path] = $aValue;
			return mapResult;
		} else {
			String stringResult = node.getText();
			if (stringResult == null) {
				return null;
			}
			//       if (!empty($node["backend_model"]) && !empty($sValue)) {
			//           $backend = Mage::getModel((string) $node["backend_model"]);
			//           $backend->setPath($path)->setValue($sValue)->afterLoad();
			//           $sValue = $backend->getValue();
			//       }
			//
			if (stringResult.indexOf("{{") >= 0) {
				if (stringResult.indexOf("{{unsecure_base_url}}") >= 0) {
					String unsecureBaseUrl = (String)getConfig(XML_PATH_UNSECURE_BASE_URL);
					stringResult = stringResult.replace("{{unsecure_base_url}}", unsecureBaseUrl);
				} else if (stringResult.indexOf("{{secure_base_url}}") >= 0) {
					String secureBaseUrl = (String)getConfig(XML_PATH_SECURE_BASE_URL);
					stringResult = stringResult.replace("{{secure_base_url}}", secureBaseUrl);
				} else if (stringResult.indexOf("{{base_url}}") >= 0) {
					stringResult = AppContext.getCurrent().getConfig().substDistroServerVars(stringResult);
				}
			}

			//       $this->_configCache[$path] = $sValue;

			return stringResult;
		}
	}

	/**
	 * Flag that shows that backend URLs are secure
	 */
	protected Boolean _isAdminSecure = null;

	/**
	 * Flag that shows that frontend URLs are secure
	 */
	protected Boolean _isFrontSecure = null;

	public boolean isAdminUrlSecure() {
		if (_isAdminSecure == null) {
			String text = AppContext.getCurrent().getConfig().getNode(Mage_Core_Model_Url.XML_PATH_SECURE_IN_ADMIN).getText();
			_isAdminSecure = Integer.parseInt(text) != 0;
		}

		return _isAdminSecure;
	}

	/**
	 * Check if frontend URLs should be secure
	 *
	 * @return boolean
	 */
	public boolean isFrontUrlSecure() {
		if (_isFrontSecure == null) {
			_isFrontSecure = getConfigFlag(Mage_Core_Model_Url.XML_PATH_SECURE_IN_FRONT);
		}

		return _isFrontSecure;
	}
}
