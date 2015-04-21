package com.naver.mage4j.core.mage.core.model.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.model.message.Mage_Core_Model_Message_Abstract;
import com.naver.mage4j.core.mage.core.model.message.Mage_Core_Model_Message_Collection;
import com.naver.mage4j.core.mage.core.model.session.abstract_.Mage_Core_Model_Session_Abstract_Varien;
import com.naver.mage4j.core.util.PhpStringUtils;
import com.naver.mage4j.external.php.Standard;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;

/**
 * Core Session Abstract model
 */
public class Mage_Core_Model_Session_Abstract extends Mage_Core_Model_Session_Abstract_Varien {
	private final Logger log = LoggerFactory.getLogger(Mage_Core_Model_Session_Abstract.class);

	public static final String XML_PATH_COOKIE_DOMAIN = "web/cookie/cookie_domain";
	public static final String XML_PATH_COOKIE_PATH = "web/cookie/cookie_path";
	public static final String XML_PATH_COOKIE_LIFETIME = "web/cookie/cookie_lifetime";
	public static final String XML_NODE_SESSION_SAVE = "global/session_save";
	public static final String XML_NODE_SESSION_SAVE_PATH = "global/session_save_path";
	public static final String XML_PATH_USE_REMOTE_ADDR = "web/session/use_remote_addr";
	public static final String XML_PATH_USE_HTTP_VIA = "web/session/use_http_via";
	public static final String XML_PATH_USE_X_FORWARDED = "web/session/use_http_x_forwarded_for";
	public static final String XML_PATH_USE_USER_AGENT = "web/session/use_http_user_agent";
	public static final String XML_PATH_USE_FRONTEND_SID = "web/session/use_frontend_sid";
	public static final String XML_NODE_USET_AGENT_SKIP = "global/session/validation/http_user_agent_skip";
	public static final String XML_PATH_LOG_EXCEPTION_FILE = "dev/log/exception_file";
	public static final String SESSION_ID_QUERY_PARAM = "SID";

	/**
	 * URL host cache
	 */
	protected static Map<String, String> _urlHostCache = new HashMap<String, String>();

	/**
	 * Encrypted session id cache
	 */
	protected static String _encryptedSessionId;

	/**
	 * Skip session id flag
	 */
	protected boolean _skipSessionIdFlag = false;

	public Mage_Core_Model_Session_Abstract(Map<String, Object> args) {
		super(args);
	}

	/**
	 * Init session
	 * 
	 * @param namespace
	 * @param sessionName
	 * @return Mage_Core_Model_Session_Abstract
	 */
	@Override
	public Mage_Core_Model_Session_Abstract init(String namespace, String sessionName/* = null */) {
		super.init(namespace, sessionName);
		this.addHost();
		return this;
	}

	/**
	 * Retrieve Cookie domain
	 * 
	 * @return string
	 */
	public String getCookieDomain() {
		return this.getCookie().getDomain();
	}

	/**
	 * Retrieve cookie path
	 * 
	 * @return string
	 */
	public String getCookiePath() {
		return this.getCookie().getPath();
	}

	/**
	 * Retrieve cookie lifetime
	 * 
	 * @return int
	 */
	public int getCookieLifetime() {
		return this.getCookie().getLifetime();
	}

	/**
	 * Use REMOTE_ADDR in validator key
	 * 
	 * @return bool
	 */
	@Override
	public boolean useValidateRemoteAddr() {
		String use = AppContext.getCurrent().getStoreConfigAsString(XML_PATH_USE_REMOTE_ADDR);
		if (use == null) {
			return super.useValidateRemoteAddr();
		}

		return PhpStringUtils.toBoolean(use);
	}

	/**
	 * Use HTTP_VIA in validator key
	 * 
	 * @return bool
	 */
	@Override
	public boolean useValidateHttpVia() {
		String use = AppContext.getCurrent().getStoreConfigAsString(XML_PATH_USE_HTTP_VIA);
		if (use == null) {
			return super.useValidateHttpVia();
		}

		return PhpStringUtils.toBoolean(use);
	}

	/**
	 * Use HTTP_X_FORWARDED_FOR in validator key
	 * 
	 * @return bool
	 */
	@Override
	public boolean useValidateHttpXForwardedFor() {
		String use = AppContext.getCurrent().getStoreConfigAsString(XML_PATH_USE_X_FORWARDED);
		if (use == null) {
			return super.useValidateHttpXForwardedFor();
		}

		return PhpStringUtils.toBoolean(use);
	}

	/**
	 * Use HTTP_USER_AGENT in validator key
	 * 
	 * @return bool
	 */
	@Override
	public boolean useValidateHttpUserAgent() {
		String use = AppContext.getCurrent().getStoreConfigAsString(XML_PATH_USE_USER_AGENT);
		if (use == null) {
			return super.useValidateHttpUserAgent();
		}

		return PhpStringUtils.toBoolean(use);
	}

	/**
	 * Check whether SID can be used for session initializationAdmin area will always have this feature enabled
	 * 
	 * @return bool
	 */
	public boolean useSid() {
		return AppContext.getCurrent().getStore().getHelper().isAdmin() || AppContext.getCurrent().getStoreConfigAsBoolean(XML_PATH_USE_FRONTEND_SID);
	}

	/**
	 * Retrieve skip User Agent validation strings (Flash etc)
	 * 
	 * @return array
	 */
	@Override
	public List<String> getValidateHttpUserAgentSkip() {
		List<String> userAgents = new ArrayList<String>();
		Simplexml_Element skip = AppContext.getCurrent().getConfig().getNode(XML_NODE_USET_AGENT_SKIP);
		for (Simplexml_Element userAgent : skip.children()) {
			userAgents.add(userAgent.getText());
		}

		return userAgents;
	}

	/**
	 * Retrieve messages from session
	 * 
	 * @param clear
	 * @return Mage_Core_Model_Message_Collection
	 */
	public Mage_Core_Model_Message_Collection getMessages(boolean clear/* = false */){
		Mage_Core_Model_Message_Collection messages = (Mage_Core_Model_Message_Collection)this.getData("messages");
		if (messages == null) {
			messages = AppContext.getCurrent().getMessageCollection();
			setData("messages", messages);
		}

		if (clear) {
			throw new UnsupportedOperationException();
//			Mage_Core_Model_Message_Collection result = clone messages;
//			messages.clear();
//			AppContext.getCurrent().getEventDispatcher().dispatchEvent("core_session_abstract_clear_messages", null);
//			return result;
		}

		return messages;
	}

	/**
	 * Not Mage exception handling
	 * 
	 * @param exception
	 * @param alternativeText
	 * @return Mage_Core_Model_Session_Abstract
	 */
	public Mage_Core_Model_Session_Abstract addException(Exception exception, String alternativeText) {
		//		String message = sprintf("Exception message: %s%sTrace: %s", exception.getMessage(), "\\n", exception.getTraceAsString());
		//		String file = AppContext.getCurrent().getStoreConfigAsString(XML_PATH_LOG_EXCEPTION_FILE);
		//		Mage.log(message, Zend_Log.DEBUG, file);
		log.debug("", exception);
		this.addMessage(AppContext.getCurrent().getMessage().error("core/message", alternativeText, null));
		return this;
	}

	/**
	 * Adding new message to message collection
	 * 
	 * @param message
	 * @return Mage_Core_Model_Session_Abstract
	 */
	public Mage_Core_Model_Session_Abstract addMessage(Mage_Core_Model_Message_Abstract message) {
		this.getMessages(false).add(message);
		AppContext.getCurrent().getEventDispatcher().dispatchEvent("core_session_abstract_add_message", null);
		return this;
	}

	/**
	 * Adding new error message
	 * 
	 * @param message
	 * @return Mage_Core_Model_Session_Abstract
	 */
	public Mage_Core_Model_Session_Abstract addError(String message) {
		this.addMessage(AppContext.getCurrent().getMessage().error(message, null, null));
		return this;
	}

	/**
	 * Adding new warning message
	 * 
	 * @param message
	 * @return Mage_Core_Model_Session_Abstract
	 */
	public Mage_Core_Model_Session_Abstract addWarning(String message) {
		this.addMessage(AppContext.getCurrent().getMessage().warning(message, null, null));
		return this;
	}

	/**
	 * Adding new notice message
	 * 
	 * @param message
	 * @return Mage_Core_Model_Session_Abstract
	 */
	public Mage_Core_Model_Session_Abstract addNotice(String message) {
		this.addMessage(AppContext.getCurrent().getMessage().notice(message, null, null));
		return this;
	}

	/**
	 * Adding new success message
	 * 
	 * @param message
	 * @return Mage_Core_Model_Session_Abstract
	 */
	public Mage_Core_Model_Session_Abstract addSuccess(String message) {
		this.addMessage(AppContext.getCurrent().getMessage().success(message, null, null));
		return this;
	}

	/**
	 * Adding messages array to message collection
	 * 
	 * @param messages
	 * @return Mage_Core_Model_Session_Abstract
	 */
	public Mage_Core_Model_Session_Abstract addMessages(List<Mage_Core_Model_Message_Abstract> messages) {
		for (Mage_Core_Model_Message_Abstract message : messages) {
			this.addMessage(message);
		}

		return this;
	}

	/**
	 * Adds messages array to message collection, but doesn't add duplicates to it
	 * 
	 * @param messages
	 * @return Mage_Core_Model_Session_Abstract
	 */
	public Mage_Core_Model_Session_Abstract addUniqueMessages(List<Mage_Core_Model_Message_Abstract> messages) {
		if (CollectionUtils.isEmpty(messages)) {
			return this;
		}

		Set<String> messagesAlready = new HashSet<String>();
		Map<String, Mage_Core_Model_Message_Abstract> items = this.getMessages(false).getItems(null);
		for (Map.Entry<String, Mage_Core_Model_Message_Abstract> item : items.entrySet()) {
			String text = item.getValue().getText();
			//			if (item instanceof Mage_Core_Model_Message_Abstract) {
			//				text = item.getText();
			//			} else {
			//				if (is_string(item)) {
			//					text = item;
			//				} else {
			//					continue;
			//				}
			//			}

			messagesAlready.add(text);
		}

		for (Mage_Core_Model_Message_Abstract message : messages) {
			String text = message.getText();
			//			if (message instanceof Mage_Core_Model_Message_Abstract) {
			//				text = message.getText();
			//			} else {
			//				if (is_string(message)) {
			//					text = message;
			//				} else {
			//					text = null;
			//				}
			//			}

			if (text != null) {
				if (messagesAlready.contains(text)) {
					continue;
				}

				messagesAlready.add(text);
			}

			this.addMessage(message);
		}

		return this;
	}

	/**
	 * Specify session identifier
	 * 
	 * @param id
	 * @return Mage_Core_Model_Session_Abstract
	 */
	@Override
	public Mage_Core_Model_Session_Abstract setSessionId(String id/* = null */) {
		if ((id == null) && this.useSid()) {
			String _queryParam = this.getSessionIdQueryParam();
			String idInParam = AppContext.getCurrent().getRequest().getParamterValue(_queryParam);
			if (StringUtils.isNotEmpty(idInParam) && AppContext.getCurrent().getUrl().isOwnOriginUrl()) {
				id = idInParam;
			}
		}

		this.addHost();
		return (Mage_Core_Model_Session_Abstract)super.setSessionId(id);
	}

	/**
	 * Get encrypted session identifier.No reason use crypt key for session id encryption, we can use session identifier as is.
	 * 
	 * @return string
	 */
	public String getEncryptedSessionId() {
		if (_encryptedSessionId == null) {
			_encryptedSessionId = this.getSessionId();
		}

		return _encryptedSessionId;
	}

	/**
	 * Get encrypted session identifier.No reason use crypt key for session id encryption, we can use session identifier as is.
	 * 
	 * @return string
	 */
	public String getSessionIdQueryParam() {
		String sessionName = this.getSessionName();
		if (StringUtils.isNotEmpty(sessionName)) {
			String queryParam = AppContext.getCurrent().getConfig().getNode(sessionName + "/session/query_param").getText();
			if (StringUtils.isNotEmpty(queryParam)) {
				return queryParam;
			}
		}

		return SESSION_ID_QUERY_PARAM;
	}

	/**
	 * Set skip flag if need skip generating of _GET session_id_key param
	 * 
	 * @param flag
	 * @return Mage_Core_Model_Session_Abstract
	 */
	public Mage_Core_Model_Session_Abstract setSkipSessionIdFlag(boolean flag) {
		this._skipSessionIdFlag = flag;
		return this;
	}

	/**
	 * Retrieve session id skip flag
	 * 
	 * @return bool
	 */
	public boolean getSkipSessionIdFlag() {
		return this._skipSessionIdFlag;
	}

	/**
	 * If session cookie is not applicable due to host or path mismatch - add session id to query
	 * 
	 * @param urlHost url 
	 * @return string  {session_id_key}={session_id_encrypted}
	 */
	public String getSessionIdForHost(String urlHost) {
		String urlPath;
		if (this.getSkipSessionIdFlag() == true) {
			return "";
		}

		String httpHost = AppContext.getCurrent().getRequest().getHttpHost();
		if (StringUtils.isEmpty(httpHost)) {
			return "";
		}

		String[] urlHostArr = StringUtils.split(urlHost, "/", 4);
		if (urlHostArr.length > 2) {
			urlHost = urlHostArr[2];
		}

		urlPath = (urlHostArr.length > 3) ? urlHostArr[3] : "";
		if (!(_urlHostCache.get(urlHost) != null)) {
			urlHostArr = urlHost.split(":");
			urlHost = urlHostArr[0];
			String sessionId = (!StringUtils.equals(httpHost, urlHost) && !this.isValidForHost(urlHost)) ? this.getEncryptedSessionId() : "";
			_urlHostCache.put(urlHost, sessionId);
		}

		return (AppContext.getCurrent().getStore().getHelper().isAdmin() || this.isValidForPath(urlPath)) ? _urlHostCache.get(urlHost) : this.getEncryptedSessionId();
	}

	/**
	 * Check if session is valid for given hostname
	 * 
	 * @param host
	 * @return bool
	 */
	public boolean isValidForHost(String host) {
		String[] hostArr = host.split(":");

		return this.getSessionHosts().contains(hostArr[0]);
	}

	/**
	 * Check if session is valid for given path
	 * 
	 * @param path
	 * @return bool
	 */
	public boolean isValidForPath(String path) {
		String cookiePath;
		String urlPath;
		cookiePath = Standard.trim(this.getCookiePath(), "/") + "/";
		if (cookiePath == "/") {
			return true;
		}

		urlPath = Standard.trim(path, "/") + "/";
		return urlPath.indexOf(cookiePath) == 0;
	}

	/**
	 * Add hostname to session
	 * 
	 * @param host
	 * @return Mage_Core_Model_Session_Abstract
	 */
	public Mage_Core_Model_Session_Abstract addHost(String host) {
		if (StringUtils.isNotEmpty(host)) {
			Set<String> hosts = getSessionHosts();
			hosts.add(host);
		}

		return this;
	}

	public Mage_Core_Model_Session_Abstract addHost() {
		String host = AppContext.getCurrent().getRequest().getHttpHost();
		if (StringUtils.isNotEmpty(host)) {
			addHost(host);
		}

		return this;
	}

	/**
	 * Retrieve session hostnames
	 * 
	 * @return array
	 */
	public Set<String> getSessionHosts() {
		Set<String> hosts = (Set<String>)this.getData("session_hosts");
		if (hosts == null) {
			hosts = new HashSet<String>();
			this.setData("session_hosts", hosts);
		}

		return hosts;
	}

	/**
	 * Retrieve session save method
	 * 
	 * @return string
	 */
	@Override
	public String getSessionSaveMethod() {
		if (AppContext.getCurrent().isInstalled()) {
			String sessionSave = AppContext.getCurrent().getConfig().getNode(XML_NODE_SESSION_SAVE).getText();
			if (sessionSave != null) {
				return sessionSave;
			}
		}

		return super.getSessionSaveMethod();
	}

	/**
	 * Get session save path
	 * 
	 * @return string
	 */
	@Override
	public String getSessionSavePath() {
		if (AppContext.getCurrent().isInstalled()) {
			String path = AppContext.getCurrent().getConfig().getNode(XML_NODE_SESSION_SAVE_PATH).getText();
			if (path != null) {
				return path;
			}
		}

		return super.getSessionSavePath();
	}

	/**
	 * Renew session id and update session cookie
	 * 
	 * @return Mage_Core_Model_Session_Abstract
	 */
	public Mage_Core_Model_Session_Abstract renewSession() {
		this.getCookie().delete(this.getSessionName());
		this.regenerateSessionId();
		Set<String> sessionHosts = this.getSessionHosts();
		String currentCookieDomain = this.getCookie().getDomain();
		for (String host : sessionHosts) {
			if (currentCookieDomain.indexOf(host) > 0) {
				this.getCookie().delete(this.getSessionName(), null, host);
			}
		}

		return this;
	}
}
