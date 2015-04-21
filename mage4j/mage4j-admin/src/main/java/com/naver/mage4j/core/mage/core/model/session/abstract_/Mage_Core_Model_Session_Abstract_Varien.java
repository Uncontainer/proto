package com.naver.mage4j.core.mage.core.model.session.abstract_;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Cookie;
import com.naver.mage4j.external.varien.Varien_Object;

public class Mage_Core_Model_Session_Abstract_Varien extends Varien_Object {
	public static final String VALIDATOR_KEY = "_session_validator_data";

	public static final String VALIDATOR_HTTP_USER_AGENT_KEY = "http_user_agent";

	public static final String VALIDATOR_HTTP_X_FORVARDED_FOR_KEY = "http_x_forwarded_for";

	public static final String VALIDATOR_HTTP_VIA_KEY = "http_via";

	public static final String VALIDATOR_REMOTE_ADDR_KEY = "remote_addr";

	public Mage_Core_Model_Session_Abstract_Varien(Map<String, Object> args) {
		super(args);
	}

	/**
	 * Configure and start session
	 * 
	 * @param sessionName
	 * @return Mage_Core_Model_Session_Abstract_Varien
	 */
	public Mage_Core_Model_Session_Abstract_Varien start(String sessionName/* = null */) {
		throw new UnsupportedOperationException();
		//		if ((_SESSION != null) && (!(this.getSkipEmptySessionCheck()))) {
		//			return this;
		//		}
		//
		//		String moduleName = this.getSessionSaveMethod();
		//		switch (moduleName) {
		//			case "db": {
		//				moduleName = "user";
		//				Mage_Core_Model_Session sessionResource = AppContext.getCurrent().getSession(null);
		//				sessionResource.setSaveHandler();
		//				break;
		//			}
		//			case "user": {
		//				call_user_func(this.getSessionSavePath());
		//				break;
		//			}
		//			case "files": {
		//				if (!(is_writable(this.getSessionSavePath()))) {
		//					break;
		//				}
		//
		//			}
		//			default: {
		//				session_save_path(this.getSessionSavePath());
		//				break;
		//			}
		//		}
		//
		//		session_module_name(moduleName);
		//		Mage_Core_Model_Cookie cookie = this.getCookie();
		//		if (AppContext.getCurrent().getStore().getHelper().isAdmin()) {
		//			long sessionMaxLifetime = CoreSession.SEESION_MAX_COOKIE_LIFETIME;
		//			long adminSessionLifetime = Long.parseLong(AppContext.getCurrent().getStoreConfigAsString("admin/security/session_cookie_lifetime"));
		//			if (adminSessionLifetime > sessionMaxLifetime) {
		//				adminSessionLifetime = sessionMaxLifetime;
		//			}
		//
		//			if (adminSessionLifetime > 60) {
		//				cookie.setLifetime(adminSessionLifetime);
		//			}
		//		}
		//
		//		Map<String, Object> cookieParams = MageAtomArray.createMap(
		//			new Object[] {"lifetime", cookie.getLifetime()},
		//			new Object[] {"path", cookie.getPath()},
		//			new Object[] {"domain", cookie.getConfigDomain()},
		//			new Object[] {"secure", cookie.isSecure()},
		//			new Object[] {"httponly", cookie.getHttponly()});
		//		if (!(cookieParams.get("httponly"))) {
		//			unset(cookieParams.get("httponly"));
		//			if (!(cookieParams.get("secure"))) {
		//				unset(cookieParams.get("secure"));
		//				if (!(cookieParams.get("domain"))) {
		//					unset(cookieParams.get("domain"));
		//				}
		//			}
		//		}
		//
		//		if (cookieParams.get("domain") != null) {
		//			cookieParams.put("domain", cookie.getDomain());
		//		}
		//
		//		call_user_func_array("session_set_cookie_params", cookieParams);
		//		if (StringUtils.isNotEmpty(sessionName)) {
		//			this.setSessionName(sessionName);
		//		}
		//
		//		this.setSessionId();
		//		Varien_Profiler.start(__METHOD__ + "/start");
		//		String sessionCacheLimiter = AppContext.getCurrent().getConfig().selectDescendant("global/session_cache_limiter").getText();
		//		if (sessionCacheLimiter != null) {
		//			session_cache_limiter(sessionCacheLimiter);
		//		}
		//
		//		session_start();
		//		if (cookie.get(session_name()) == this.getSessionId()) {
		//			cookie.renew(session_name());
		//		}
		//
		//		Varien_Profiler.stop(__METHOD__ + "/start");
		//		return this;
	}

	/**
	 * Retrieve cookie object
	 * 
	 * @return Mage_Core_Model_Cookie
	 */
	public Mage_Core_Model_Cookie getCookie() {
		return AppContext.getCurrent().getCookie();
	}

	/**
	 * Revalidate cookie
	 * 
	 * @return Mage_Core_Model_Session_Abstract_Varien
	 */
	public Mage_Core_Model_Session_Abstract_Varien revalidateCookie() {
		return this;
	}

	/**
	 * Init session with namespace
	 * 
	 * @param namespace
	 * @param sessionName
	 * @return Mage_Core_Model_Session_Abstract_Varien
	 */
	public Mage_Core_Model_Session_Abstract_Varien init(String namespace, String sessionName/* = null */) {
		HttpSession session = getSession();
		if (session == null) {
			this.start(sessionName);
			// TODO session 초기화 이후 재할당 소스 추가.
		}

		this._data = (Map<String, Object>)session.getAttribute(namespace);
		if (this._data == null) {
			this._data = new HashMap<String, Object>();
			session.setAttribute(namespace, this._data);
		}

		this.validate();
		this.revalidateCookie();
		return this;
	}

	private HttpSession getSession() {
		return AppContext.getCurrent().getRequest().getServletRequest().getSession();
	}

	/**
	 * Additional get data with clear mode
	 * 
	 * @param key
	 * @param clear
	 * @return mixed
	 */
	public Object getData(String key/* = "" */, boolean clear/* = false */) {
		Object data = super.getData(key);
		if (clear && data != null) {
			this._data.remove(key);
		}

		return data;
	}

	/**
	 * Retrieve session Id
	 * 
	 * @return string
	 */
	public String getSessionId() {
		HttpSession session = getSession();
		return session != null ? session.getId() : null;
	}

	/**
	 * Set custom session id
	 * 
	 * @param id
	 * @return Mage_Core_Model_Session_Abstract_Varien
	 */
	public Mage_Core_Model_Session_Abstract_Varien setSessionId(String id/* = null */) {
		if (id != null && id.matches("^[0-9a-zA-Z,-]+$")) {
			throw new UnsupportedOperationException();
			//			session_id(id);
		}

		return this;
	}

	/**
	 * Retrieve session name
	 * 
	 * @return string
	 */
	public String getSessionName() {
		return getSessionId();
	}

	/**
	 * Set session name
	 * 
	 * @param name
	 * @return Mage_Core_Model_Session_Abstract_Varien
	 */
	//	public Mage_Core_Model_Session_Abstract_Varien setSessionName(String name) {
	//		session_name(name);
	//		return this;
	//	}

	/**
	 * Unset all data
	 * 
	 * @return Mage_Core_Model_Session_Abstract_Varien
	 */
	public Mage_Core_Model_Session_Abstract_Varien unsetAll() {
		this.unsetData(null);
		return this;
	}

	/**
	 * Alias for unsetAll
	 * 
	 * @return Mage_Core_Model_Session_Abstract_Varien
	 */
	public Mage_Core_Model_Session_Abstract_Varien clear() {
		return this.unsetAll();
	}

	/**
	 * Retrieve session save methodDefault files
	 * 
	 * @return string
	 */
	public String getSessionSaveMethod() {
		return "files";
	}

	/**
	 * Get sesssion save path
	 * 
	 * @return string
	 */
	public String getSessionSavePath() {
		return AppContext.getCurrent().getConfig().getBaseDir("session");
	}

	/**
	 * Use REMOTE_ADDR in validator key
	 * 
	 * @return bool
	 */
	public boolean useValidateRemoteAddr() {
		return true;
	}

	/**
	 * Use HTTP_VIA in validator key
	 * 
	 * @return bool
	 */
	public boolean useValidateHttpVia() {
		return true;
	}

	/**
	 * Use HTTP_X_FORWARDED_FOR in validator key
	 * 
	 * @return bool
	 */
	public boolean useValidateHttpXForwardedFor() {
		return true;
	}

	/**
	 * Use HTTP_USER_AGENT in validator key
	 * 
	 * @return bool
	 */
	public boolean useValidateHttpUserAgent() {
		return true;
	}

	/**
	 * Retrieve skip User Agent validation strings (Flash etc)
	 * 
	 * @return array
	 */
	public List<String> getValidateHttpUserAgentSkip() {
		return Collections.emptyList();
	}

	/**
	 * Validate session
	 * 
	 * @param namespace
	 * @return Mage_Core_Model_Session_Abstract_Varien
	 */
	public Mage_Core_Model_Session_Abstract_Varien validate() {
		throw new UnsupportedOperationException();
		//		if (this._data.get(VALIDATOR_KEY) == null) {
		//			this._data.put(VALIDATOR_KEY, this.getValidatorData());
		//		} else {
		//			if (!this._validate()) {
		//				this.getCookie().delete(session_name());
		//				throw new Mage_Core_Model_Session_Exception();
		//			}
		//		}
		//
		//		return this;
	}

	/**
	 * Validate data
	 * 
	 * @return bool
	 */
	//	protected boolean _validate() {
	//		Map<String, String> sessionData = (Map<String, String>)this._data.get(VALIDATOR_KEY);
	//		Map<String, String> validatorData = this.getValidatorData();
	//		if (this.useValidateRemoteAddr() && (sessionData.get(VALIDATOR_REMOTE_ADDR_KEY) != validatorData.get(VALIDATOR_REMOTE_ADDR_KEY))) {
	//			return false;
	//		}
	//
	//		if (this.useValidateHttpVia() && (sessionData.get(VALIDATOR_HTTP_VIA_KEY) != validatorData.get(VALIDATOR_HTTP_VIA_KEY))) {
	//			return false;
	//		}
	//
	//		String sessionValidateHttpXForwardedForKey = sessionData.get(VALIDATOR_HTTP_X_FORVARDED_FOR_KEY);
	//		String validatorValidateHttpXForwardedForKey = validatorData.get(VALIDATOR_HTTP_X_FORVARDED_FOR_KEY);
	//		if (this.useValidateHttpXForwardedFor() && !StringUtils.equals(sessionValidateHttpXForwardedForKey, validatorValidateHttpXForwardedForKey)) {
	//			return false;
	//		}
	//
	//		if (this.useValidateHttpUserAgent() && (sessionData.get(VALIDATOR_HTTP_USER_AGENT_KEY) != validatorData.get(VALIDATOR_HTTP_USER_AGENT_KEY))) {
	//			List<String> userAgentValidated = this.getValidateHttpUserAgentSkip();
	//			for (String agent : userAgentValidated) {
	//				if (validatorData.get(VALIDATOR_HTTP_USER_AGENT_KEY).matches("/" + agent + "/iu")) {
	//					return true;
	//				}
	//
	//			}
	//
	//			return false;
	//		}
	//
	//		return true;
	//	}

	/**
	 * Retrieve unique user data for validator
	 * 
	 * @return array
	 */
	//	public Map<String, String> getValidatorData() {
	//		Map<String, String> parts = new HashMap<String, String>();
	//		parts.put(VALIDATOR_REMOTE_ADDR_KEY, "");
	//		parts.put(VALIDATOR_HTTP_VIA_KEY, "");
	//		parts.put(VALIDATOR_HTTP_X_FORVARDED_FOR_KEY, "");
	//		parts.put(VALIDATOR_HTTP_USER_AGENT_KEY, "");
	//
	//		if (Mage.helper("core/http").getRemoteAddr()) {
	//			parts.put(VALIDATOR_REMOTE_ADDR_KEY, Mage.helper("core/http").getRemoteAddr());
	//		}
	//
	//		if (_ENV.get("HTTP_VIA") != null) {
	//			parts.put(VALIDATOR_HTTP_VIA_KEY, _ENV.get("HTTP_VIA"));
	//		}
	//
	//		if (_ENV.get("HTTP_X_FORWARDED_FOR") != null) {
	//			parts.put(VALIDATOR_HTTP_X_FORVARDED_FOR_KEY, (String)_ENV.get("HTTP_X_FORWARDED_FOR"));
	//		}
	//
	//		if (_SERVER.get("HTTP_USER_AGENT") != null) {
	//			parts.put(VALIDATOR_HTTP_USER_AGENT_KEY, (String)_SERVER.get("HTTP_USER_AGENT"));
	//		}
	//
	//		return parts;
	//	}

	/**
	 * Regenerate session Id
	 * 
	 * @return Mage_Core_Model_Session_Abstract_Varien
	 */
	public Mage_Core_Model_Session_Abstract_Varien regenerateSessionId() {
		throw new UnsupportedOperationException();
		//		session_regenerate_id(true);
		//		return this;
	}
}
