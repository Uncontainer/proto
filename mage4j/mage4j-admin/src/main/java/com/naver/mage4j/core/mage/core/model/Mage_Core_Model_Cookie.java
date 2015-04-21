package com.naver.mage4j.core.mage.core.model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.controller.request.Mage_Core_Controller_Request_Http;
import com.naver.mage4j.core.mage.core.controller.response.Mage_Core_Controller_Response_Http;
import com.naver.mage4j.core.mage.core.model.resource.store.Store;

/**
 * Core cookie model
 */
public class Mage_Core_Model_Cookie {
	public static final String XML_PATH_COOKIE_DOMAIN = "web/cookie/cookie_domain";
	public static final String XML_PATH_COOKIE_PATH = "web/cookie/cookie_path";
	public static final String XML_PATH_COOKIE_LIFETIME = "web/cookie/cookie_lifetime";
	public static final String XML_PATH_COOKIE_HTTPONLY = "web/cookie/cookie_httponly";

	public static final int AGE_YEAR = 3600 * 24 * 365;

	protected Integer _lifetime;

	/**
	 * Store object
	 */
	protected Store _store;

	HttpServletRequest servletRequest;
	HttpServletResponse servletResponse;

	public Mage_Core_Model_Cookie(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
		super();
		this.servletRequest = servletRequest;
		this.servletResponse = servletResponse;
	}

	/**
	 * Set Store object
	 * 
	 * @param store
	 * @return Mage_Core_Model_Cookie
	 */
	public Mage_Core_Model_Cookie setStore(Store store) {
		if (store == null) {
			this._store = AppContext.getCurrent().getStore();
		} else {
			this._store = store;
		}

		return this;
	}

	/**
	 * Retrieve Store object
	 * 
	 * @return Mage_Core_Model_Store
	 */
	public Store getStore() {
		if (this._store == null) {
			this._store = AppContext.getCurrent().getStore();
		}

		return this._store;
	}

	/**
	 * Retrieve Request object
	 * 
	 * @return Mage_Core_Controller_Request_Http
	 */
	protected Mage_Core_Controller_Request_Http _getRequest() {
		return AppContext.getCurrent().getRequest();
	}

	/**
	 * Retrieve Response object
	 * 
	 * @return Mage_Core_Controller_Response_Http
	 */
	protected Mage_Core_Controller_Response_Http _getResponse() {
		return AppContext.getCurrent().getResponse();
	}

	/**
	 * Retrieve Domain for cookie
	 * 
	 * @return string
	 */
	public String getDomain() {
		String domain;
		domain = this.getConfigDomain();
		if (domain == null) {
			domain = this._getRequest().getHttpHost();
		}

		return domain;
	}

	/**
	 * Retrieve Config Domain for cookie
	 * 
	 * @return string
	 */
	public String getConfigDomain() {
		return this.getStore().getHelper().getConfigAsString(XML_PATH_COOKIE_DOMAIN, null);
	}

	/**
	 * Retrieve Path for cookie
	 * 
	 * @return string
	 */
	public String getPath() {
		String path = this.getStore().getHelper().getConfigAsString(XML_PATH_COOKIE_PATH, null);
		if (path == null) {
			path = this._getRequest().getBasePath();
		}

		return path;
	}

	/**
	 * Retrieve cookie lifetime
	 * 
	 * @return int
	 */
	public int getLifetime() {
		int lifetime;
		if (this._lifetime != null) {
			lifetime = this._lifetime;
		} else {
			String lifetimeString = getStore().getHelper().getConfigAsString(XML_PATH_COOKIE_LIFETIME, null);
			if (NumberUtils.isDigits(lifetimeString)) {
				lifetime = Integer.parseInt(lifetimeString);
			} else {
				lifetime = 3600;
			}
		}

		return lifetime;
	}

	/**
	 * Set cookie lifetime
	 * 
	 * @param lifetime
	 * @return Mage_Core_Model_Cookie
	 */
	public Mage_Core_Model_Cookie setLifetime(int lifetime) {
		this._lifetime = lifetime;
		return this;
	}

	/**
	 * Retrieve use HTTP only flag
	 * 
	 * @return bool
	 */
	public boolean getHttponly() {
		return this.getStore().getHelper().getConfigFlag(XML_PATH_COOKIE_HTTPONLY);
	}

	/**
	 * Is https secure requestUse secure on adminhtml only
	 * 
	 * @return bool
	 */
	public boolean isSecure() {
		if (this.getStore().getHelper().isAdmin()) {
			return this._getRequest().isSecure();
		}

		return false;
	}

	/**
	 * Set cookie
	 * 
	 * @param name name 
	 * @param value value 
	 * @param period period 
	 * @param path
	 * @param domain
	 * @param secure
	 * @param httponly
	 * @return Mage_Core_Model_Cookie
	 */
	public Mage_Core_Model_Cookie set(String name, String value, Integer period/* = null */, String path/* = null */, String domain/* = null */, Boolean secure/* = null */, Boolean httponly/* = null */) {
		if (!(this._getResponse().canSendHeaders(false))) {
			return this;
		}

		if (period == null) {
			period = this.getLifetime();
		}

		if (path == null) {
			path = this.getPath();
		}

		if (domain == null) {
			domain = this.getDomain();
		}

		if (secure == null) {
			secure = this.isSecure();
		}

		if (httponly == null) {
			httponly = this.getHttponly();
		}

		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(period);
		cookie.setDomain(domain);
		cookie.setPath(path);
		cookie.setSecure(secure);

		servletResponse.addCookie(cookie);

		return this;
	}

	public Mage_Core_Model_Cookie set(String name, String value, int maxAge) {
		return set(name, value, maxAge, null, null, false, null);
	}

	/**
	 * Postpone cookie expiration time if cookie value defined
	 * 
	 * @param name name 
	 * @param period period 
	 * @param path
	 * @param domain
	 * @param secure
	 * @return Mage_Core_Model_Cookie
	 */
	public Mage_Core_Model_Cookie renew(String name, Integer period/* = null */, String path/* = null */, String domain/* = null */, boolean secure/* = null */, Boolean httponly/* = null */) {
		if (period == null && this.getLifetime() > 0) {
			return this;
		}

		String value = get(name);
		if (value != null) {
			this.set(name, value, period, path, domain, secure, httponly);
		}

		return this;
	}

	/**
	 * Retrieve cookie or false if not exists
	 */
	public String get(String cookieName) {
		Cookie[] cookies = servletRequest.getCookies();
		if (cookies == null) {
			return null;
		}

		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(cookieName)) {
				return cookie.getValue();
			}
		}

		return null;
	}

	/**
	 * Delete cookie
	 * 
	 * @param name
	 * @param path
	 * @param domain
	 * @param secure
	 * @param httponly
	 * @return Mage_Core_Model_Cookie
	 */
	public Mage_Core_Model_Cookie delete(String name, String path/* = null */, String domain/* = null */, Boolean secure/* = null */, Boolean httponly/* = null */) {
		if (!(this._getResponse().canSendHeaders(false))) {
			return this;
		}

		if (path == null) {
			path = this.getPath();
		}

		if (domain == null) {
			domain = this.getDomain();
		}

		if (secure == null) {
			secure = this.isSecure();
		}

		if (httponly == null) {
			httponly = this.getHttponly();
		}

		Cookie cookie = new Cookie(name, "");
		cookie.setMaxAge(0);
		cookie.setDomain(domain);
		cookie.setPath(path);
		cookie.setSecure(secure);
		servletResponse.addCookie(cookie);

		return this;
	}

	public Mage_Core_Model_Cookie delete(String name, String path/* = null */, String domain/* = null */) {
		return delete(name, path, domain, null, null);
	}

	public Mage_Core_Model_Cookie delete(String name) {
		return delete(name, null, null, null, null);
	}
}
