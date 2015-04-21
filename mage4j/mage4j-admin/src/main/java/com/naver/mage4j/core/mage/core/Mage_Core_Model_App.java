package com.naver.mage4j.core.mage.core;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.naver.mage4j.core.mage.core.controller.request.Mage_Core_Controller_Request_Http;
import com.naver.mage4j.core.mage.core.controller.response.Mage_Core_Controller_Response_Http;
import com.naver.mage4j.core.mage.core.controller.varien.Mage_Core_Controller_Varien_Front;
import com.naver.mage4j.core.mage.core.helper.Mage_Core_Helper_Data;
import com.naver.mage4j.core.mage.core.helper.Mage_Core_Helper_Http;
import com.naver.mage4j.core.mage.core.layout.Mage_Core_Model_Layout_Update;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Cache;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Config;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Cookie;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Layout;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Locale;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Message;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Session;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Translate;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Url;
import com.naver.mage4j.core.mage.core.model.ScopeType;
import com.naver.mage4j.core.mage.core.model.app.AreaContext;
import com.naver.mage4j.core.mage.core.model.app.Mage_Core_Model_App_Area;
import com.naver.mage4j.core.mage.core.model.app.Mage_Core_Model_App_Area.AreaType;
import com.naver.mage4j.core.mage.core.model.app.Mage_Core_Model_App_Area.PartType;
import com.naver.mage4j.core.mage.core.model.design.Mage_Core_Model_Design_Config;
import com.naver.mage4j.core.mage.core.model.design.Mage_Core_Model_Design_Package;
import com.naver.mage4j.core.mage.core.model.event.EventDispatcher;
import com.naver.mage4j.core.mage.core.model.message.Mage_Core_Model_Message_Collection;
import com.naver.mage4j.core.mage.core.model.resource.store.Store;
import com.naver.mage4j.core.mage.core.model.resource.store.StoreContext;
import com.naver.mage4j.core.mage.core.model.resource.store.StoreHelper;
import com.naver.mage4j.core.mage.core.model.resource.store.group.StoreGroup;
import com.naver.mage4j.core.mage.core.model.resource.store.group.StoreGroupContext;
import com.naver.mage4j.core.mage.core.model.resource.website.Website;
import com.naver.mage4j.core.mage.core.model.resource.website.WebsiteContext;
import com.naver.mage4j.core.mage.core.model.session.Mage_Core_Model_Session_Abstract;
import com.naver.mage4j.core.mage.core.model.store.Mage_Core_Model_Store_Exception;
import com.naver.mage4j.core.mage.core.model.translate.Mage_Core_Model_Translate_Inline;
import com.naver.mage4j.core.util.PhpStringUtils;
import com.naver.mage4j.external.php.Core;
import com.naver.mage4j.external.varien.Varien_Profiler;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;
import com.naver.mage4j.external.zend.cache.Zend_Cache_Core;

@Component(Mage_Core_Model_App.NAME)
@Scope("prototype")
public class Mage_Core_Model_App {
	public static final String NAME = "app";

	public static final String DEFAULT_ERROR_HANDLER = "mageCoreErrorHandler";

	/**
	 * Request object
	 */
	private Mage_Core_Controller_Request_Http _request;

	/**
	 * Response object
	 */
	private Mage_Core_Controller_Response_Http _response;

	private Mage_Core_Model_Cookie cookie;

	/**
	 * Admin store Id
	 */
	public static final short ADMIN_STORE_ID = 0;

	@Autowired
	private Mage_Core_Model_Config _config;

	//	@Autowired
	//	private ModelLoader modelLoader;

	public Mage_Core_Model_Config getConfig() {
		return _config;
	}

	public boolean isInstalled() {
		return getConfig().isInstalled();
	}

	/**
	 * Cache object
	 * 
	 * @var Zend_Cache_Core
	 */
	@Autowired
	private Mage_Core_Model_Cache _cache;

	//	/**
	//	 * Cache locked flag
	//	 */
	//	private Boolean _isCacheLocked = null;

	public static final String CACHE_TAG = "MAGE";

	/**
	 * Default store code
	 */
	private Store _currentStore;

	private Website _website;

	/**
	 * Use session in URL flag
	 *
	 * @see Mage_Core_Model_Url
	 * @var bool
	 */
	private boolean _useSessionInUrl = true;

	/**
	 * Use session var instead of SID for session in URL
	 */
	private boolean _useSessionVar = false;

	@Autowired
	private StoreContext storeContext;

	@Autowired
	private StoreGroupContext storeGroupContext;

	@Autowired
	private WebsiteContext websiteContext;

	@Autowired
	private EventDispatcher eventDispatcher;

	@Autowired
	private AreaContext areaContext;

	private Mage_Core_Controller_Varien_Front _frontController;

	private String code;
	private ScopeType type;

	public Mage_Core_Model_App() {

	}

	public void init(HttpServletRequest servlerRequest, HttpServletResponse servlerResponse, String code, ScopeType type, Map<String, Object> options) {
		_request = new Mage_Core_Controller_Request_Http(servlerRequest);
		_response = new Mage_Core_Controller_Response_Http(servlerResponse);
		cookie = new Mage_Core_Model_Cookie(servlerRequest, servlerResponse);
		this.code = code;
		this.type = type != null ? type : ScopeType.STORE;

		_response.setHeader("Content-Type", "text/html; charset=UTF-8");
		//		_response.headersSentThrowsException = Mage.headersSentThrowsException;

		if (options == null) {
			options = new HashMap<String, Object>();
		}

		init(options);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("scope_code", code);
		params.put("scope_type", type);
		params.put("options", options);

		register("application_params", params);
	}

	/**
	 * Initialize application without request processing
	 */
	private void init(Map<String, Object> options) {
		if (options == null) {
			options = new HashMap<String, Object>();
		}

		_initEnvironment();

		Varien_Profiler.start("mage::app::init::config");
		_config.setOptions(options);
		_config.loadBase();
		//		Map<String, Object> cacheOptions = (Map<String, Object>)options.get("cache");
		//		if (cacheOptions == null) {
		//			cacheOptions = new HashMap<String, Object>();
		//		}
		//		_initCache(cacheOptions);
		_config.init(options);
		Varien_Profiler.stop("mage::app::init::config");

		if (_config.isInstalled(options)) {
			_initCurrentStore();
			_initRequest();
		}
	}

	/**
	 * Initialize PHP environment
	 * 
	 * @return Mage_Core_Model_App
	 */
	private Mage_Core_Model_App _initEnvironment() {
		setErrorHandler(Mage_Core_Model_App.DEFAULT_ERROR_HANDLER);
		Core.date_default_timezone_set(Mage_Core_Model_Locale.DEFAULT_TIMEZONE);
		return this;
	}

	public void run() throws IOException {
		if (_cache.processRequest()) {
			getResponse().sendResponse();
		} else {
			loadAreaPart(Mage_Core_Model_App_Area.AreaType.GLOBAL, Mage_Core_Model_App_Area.PartType.EVENTS);

			if (_config.isLocalConfigLoaded()) {
				// TODO 내용 파악 이후 구현
				// Mage_Core_Model_Resource_Setup::applyAllDataUpdates();
			}

			getFrontController().dispatch();
		}
	}

	public EventDispatcher getEventDispatcher() {
		return eventDispatcher;
	}

	/**
	 * Retrieve front controller object
	 *
	 * @return Mage_Core_Controller_Varien_Front
	 */
	public Mage_Core_Controller_Varien_Front getFrontController() {
		if (_frontController == null) {
			_frontController = new Mage_Core_Controller_Varien_Front(this);
			register("controller", _frontController);
			Varien_Profiler.start("mage::app::init_front_controller");
			_frontController.init();
			Varien_Profiler.stop("mage::app::init_front_controller");
		}

		return _frontController;
	}

	/**
	* Set use session var instead of SID for URL
	*/
	public void setUseSessionVar(boolean var) {
		_useSessionVar = var;
	}

	/**
	 * Retrieve use flag session var instead of SID for URL
	 */
	public boolean getUseSessionVar() {
		return _useSessionVar;
	}

	//	/**
	//	 * Initialize application cache instance
	//	 */
	//	private Mage_Core_Model_App _initCache(Map<String, Object> cacheInitOptions) {
	//		_isCacheLocked = true;
	//		Map<String, Object> options;
	//		Varien_Simplexml_Element config = _config.selectDescendant("global/cache");
	//		if (config != null) {
	//			options = config.toMap();
	//		} else {
	//			options = new HashMap<String, Object>();
	//		}
	//
	//		if (cacheInitOptions != null) {
	//			options.putAll(cacheInitOptions);
	//		}
	//		_cache = (Mage_Core_Model_Cache)modelLoader.getModel("core/cache", options);
	//
	//		_isCacheLocked = false;
	//		return this;
	//	}

	/**
	 * Cleaning cache
	 *
	 * @param   array $tags
	 * @return  Mage_Core_Model_App
	 */
	public Mage_Core_Model_App cleanCache(List<String> tags) {
		_cache.clean(tags);
		eventDispatcher.dispatchEvent("application_clean_cache", Collections.singletonMap("tags", (Object)tags));
		return this;
	}

	/**
	 * Redeclare custom error handler
	 * 
	 * @param string
	 *            $handler
	 * @return Mage_Core_Model_App
	 */
	public Mage_Core_Model_App setErrorHandler(String handler) {
		Core.set_error_handler(handler);
		return this;
	}

	/**
	 * Retrieve request object
	 * 
	 * @return Mage_Core_Controller_Request_Http
	 */
	public Mage_Core_Controller_Request_Http getRequest() {
		return _request;
	}

	/**
	 * Retrieve response object
	 * 
	 * @return Zend_Controller_Response_Http
	 */
	public Mage_Core_Controller_Response_Http getResponse() {
		return _response;
	}

	public Mage_Core_Model_Cookie getCookie() {
		return cookie;
	};

	/**
	* Loading application area
	*
	* @param   string $code
	 * @return 
	* @return  Mage_Core_Model_App
	*/
	public Mage_Core_Model_App loadArea(AreaType area) {
		loadAreaPart(area, null);

		return this;
	}

	/**
	 * Loding part of area data
	 * @return  Mage_Core_Model_App
	 */
	private void loadAreaPart(AreaType area, PartType part) {
		areaContext.get(area, part);
	}

	/**
	 * Init request object
	 */
	private void _initRequest() {
		_request.setPathInfo(null);
	}

	/**
	 * Initialize currently ran store
	 *
	 * @param string $scopeCode code of default scope (website/store_group/store code)
	 * @param string $scopeType type of default scope (website/group/store)
	 * @return unknown_type
	 */
	private Mage_Core_Model_App _initCurrentStore() {
		Varien_Profiler.start("mage::app::init::stores");
		_initStores();
		Varien_Profiler.stop("mage::app::init::stores");

		if (StringUtils.isEmpty(code) && _website != null) {
			code = _website.getCode();
			type = ScopeType.WEBSITE;
		}

		switch (type) {
			case STORE:
				_currentStore = storeContext.getByCode(code);
				break;
			case GROUP:
				_currentStore = _getStoreByGroup(Short.parseShort(code));
				break;
			case WEBSITE:
				_currentStore = _getStoreByWebsite(code);
				break;
			default:
				throw new Mage_Core_Model_Store_Exception();

		}

		if (_currentStore != null) {
			_checkCookieStore();
			_checkGetStore();
		}

		_useSessionInUrl = StringUtils.isNotBlank(getStoreConfigAsString(Mage_Core_Model_Session_Abstract.XML_PATH_USE_FRONTEND_SID));

		return this;
	}

	private Store _getStoreByWebsite(String websiteCode) {
		Website website = websiteContext.getByCode(websiteCode);

		return (website != null) ? _getStoreByGroup(website.getDefaultGroupId()) : null;
	}

	protected Store _getStoreByGroup(short groupId) {
		StoreGroup storeGroup = storeGroupContext.getById(groupId);
		if (storeGroup == null) {
			return null;
		}

		return storeContext.getById(storeGroup.getDefaultStoreId());
	}

	/**
	 * Check cookie store
	 */
	private void _checkCookieStore() {
		String storeCode = cookie.get(StoreContext.COOKIE_NAME);
		if (StringUtils.isBlank(storeCode)) {
			return;
		}

		Store store = storeContext.getByCode(storeCode);
		if (store != null && store.getStoreId() != null && store.getIsActive()) {
			switch (type) {
				case WEBSITE:
					if (store.getWebsite().getWebsiteId() == _currentStore.getWebsite().getWebsiteId()) {
						_currentStore = store;
					}
					break;
				case GROUP:
					if (store.getCoreStoreGroup().getGroupId() == _currentStore.getCoreStoreGroup().getGroupId()) {
						_currentStore = store;
					}
					break;
				case STORE:
					_currentStore = store;
					break;
			}
		}
	}

	/**
	 * Init store, group and website collections
	 *
	 */
	private void _initStores() {
		_website = websiteContext.getDefaultWebsite();
	}

	/**
	 * Check get store
	 *
	 * @return Mage_Core_Model_App
	 */
	private void _checkGetStore() {
		if (!_request.isGet()) {
			return;
		}

		String storeCode = _request.getParamterValue("___store");
		if (storeCode == null) {
			return;
		}

		Store store = storeContext.getByCode(storeCode);
		if (store == null || store.getStoreId() == null || !store.getIsActive()) {
			return;
		}
		switch (type) {
			case WEBSITE:
				if (store.getWebsite().getWebsiteId() == _currentStore.getWebsite().getWebsiteId()) {
					_currentStore = store;
				}
				break;
			case GROUP:
				if (store.getCoreStoreGroup().getGroupId() == _currentStore.getCoreStoreGroup().getGroupId()) {
					_currentStore = store;
				}
				break;
			case STORE:
				_currentStore = store;
				break;
		}

		if (_currentStore == store) {
			if (store.getWebsite().getDefaultGroupId() == store.getStoreId()) {
				cookie.delete(StoreContext.COOKIE_NAME);
			} else {
				cookie.set(StoreContext.COOKIE_NAME, _currentStore.getCode(), Mage_Core_Model_Cookie.AGE_YEAR);
			}
			//		            if ($store->getWebsite()->getDefaultStore()->getId() == $store->getId()) {
			//		            } else {
			//		            }
		}
	}

	/**
	 * Check whether to use cache for specific component
	 */
	public boolean useCache(String type) {
		return _cache.canUse(type);
	}

	/**
	 * Loading cache data
	 * 
	 * @param string
	 *            $id
	 * @return mixed
	 */
	public Object loadCache(String id) {
		return _cache.load(id);
	}

	/**
	* Retrieve cache object
	*
	* @return Zend_Cache_Core
	*/
	public Zend_Cache_Core getCache() {
		return _cache.getFrontend();
	}

	/**
	* Saving cache data
	*
	* @param   mixed $data
	* @param   string $id
	* @param   array $tags
	* @return  Mage_Core_Model_App
	*/
	public Mage_Core_Model_App saveCache(Object data, String id, List<String> tags, int lifeTimeInSeconds) {
		_cache.save(data, id, tags, lifeTimeInSeconds);

		return this;
	}

	public Mage_Core_Model_App saveCache(Object data, String id, List<String> tags) {
		return this.saveCache(data, id, tags, Integer.MAX_VALUE);
	}

	public void removeCache(String id) {
		_cache.remove(id);
	}

	private static Map<String, Object> _registry = new HashMap<String, Object>();

	public static void register(String key, Object value) {
		register(key, value, false);
	}

	public static void register(String key, Object value, boolean graceful) {
		if (_registry.containsKey(key)) {
			if (graceful) {
				return;
			} else {
				throw new RuntimeException("Mage registry key " + key + " already exists");
			}
		} else {
			_registry.put(key, value);
		}
	}

	/**
	 * Unregister a variable from register by key
	 * 
	 * @param string
	 *            $key
	 */
	public static void unregister(String key) {
		Object object = _registry.get(key);
		if (object != null) {
			if (object instanceof Destructable) {
				((Destructable)object).destruct();
			}

			_registry.remove(key);
		}
	}

	public static Object registry(String key) {
		return _registry.get(key);
	}

	public void setCurrentStore(Store store) {
		// TODO 이 method가 필요할 지 확인 필요
		_currentStore = store;
		throw new UnsupportedOperationException();
	}

	public Store getStore() {
		if (_currentStore == null) {
			throw new IllegalStateException("Current store has not set.");
		}

		return _currentStore;
	}

	public String getStoreConfigAsString(String path, String defaultValue) {
		return getStore().getHelper().getConfigAsString(path, defaultValue);
	}

	public String getStoreConfigAsString(String path) {
		return getStoreConfigAsString(path, null);
	}

	public boolean getStoreConfigAsBoolean(String path) {
		return PhpStringUtils.toBoolean(getStoreConfigAsString(path, null));
	}

	public <V> Map<String, V> getStoreConfigAsMap(String path) {
		return getStore().getHelper().getConfigAsMap(path);
	}

	/**
	 * Retrieve config flag for store by path
	 */
	public boolean getStoreConfigFlag(String path) {
		return getStore().getHelper().getConfigFlag(path);
	}

	public Store getDefaultStoreView() {
		Website defaultWebsite = websiteContext.getDefaultWebsite();
		if (defaultWebsite != null) {
			StoreGroup storeGroup = storeGroupContext.getById(defaultWebsite.getDefaultGroupId());
			if (storeGroup != null) {
				return storeContext.getById(storeGroup.getDefaultStoreId());
			}
		}

		return null;
	}

	/**
	 * Retrieve use session in URL flag
	 */
	public boolean getUseSessionInUrl() {
		return _useSessionInUrl;
	}

	/**
	 * Storage of validated secure urls
	 *
	 * @var array
	 */
	protected Set<String> _secureUrlCache = new HashSet<String>();

	public boolean shouldUrlBeSecure(String url) {
		if (!getStoreConfigFlag(StoreHelper.XML_PATH_SECURE_IN_FRONTEND)) {
			return false;
		}

		if (!_secureUrlCache.contains(url)) {
			_secureUrlCache.remove(url);

			Simplexml_Element secureUrls = _config.getNode("frontend/secure_url");
			for (Simplexml_Element match : secureUrls.children()) {
				if (url.startsWith(match.getText())) {
					_secureUrlCache.add(url);
					break;
				}
			}
		}

		return _secureUrlCache.contains(url);
	}

	public Mage_Core_Model_Design_Config getDesignConfig() {
		// app에서만 유효한 값으로 셋팅
		throw new UnsupportedOperationException();
	}

	public Mage_Core_Model_Design_Package getDesignPackage() {
		// app에서만 유효한 값으로 셋팅
		throw new UnsupportedOperationException();
	}

	public Mage_Core_Model_Locale getLocale() {
		throw new UnsupportedOperationException();
	}

	public Mage_Core_Model_Layout getLayout() {
		throw new UnsupportedOperationException();
	}

	public Mage_Core_Model_Layout_Update getLayoutUpdate() {
		throw new UnsupportedOperationException();
	}

	public Mage_Core_Model_Session getSession(Map<String, Object> data) {
		throw new UnsupportedOperationException();
	}

	public Mage_Core_Model_Translate getTranslator() {
		throw new UnsupportedOperationException();
	}

	public Mage_Core_Model_Translate_Inline getTranslateInline() {
		throw new UnsupportedOperationException();
	}

	public Mage_Core_Helper_Data getHelperData() {
		throw new UnsupportedOperationException();
	}

	public Mage_Core_Helper_Http getHelperHttp() {
		throw new UnsupportedOperationException();
	}

	public Mage_Core_Model_Url getUrl() {
		throw new UnsupportedOperationException();
	}

	public Mage_Core_Model_Message getMessage() {
		throw new UnsupportedOperationException();
	}

	public Mage_Core_Model_Message_Collection getMessageCollection() {
		throw new UnsupportedOperationException();
	}
}
