package com.naver.mage4j.core.mage.core.helper;

import java.util.ArrayList;
import java.util.List;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.model.config.Mage_Core_Model_Config_Element;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;

/**
 * Core Http Helper
 */
public class Mage_Core_Helper_Http extends Mage_Core_Helper_Abstract {
	public static final String XML_NODE_REMOTE_ADDR_HEADERS = "global/remote_addr_headers";

	/**
	 * Remote address cache
	 */
	protected String _remoteAddr;

	/**
	 * Validate and retrieve user and password from HTTP
	 * 
	 * @return array
	 */
	//	public List authValidate(Object headers/* = null */){
	//		String pass;
	//		String auth;
	//		Object v;
	//		Map _SERVER;
	//		String k;
	//		String user;
	//		if (!(headers == null)) {
	//			_SERVER = headers;
	//		}
	//
	//		user = "";
	//		pass = "";
	//		if (StringUtils.isEmpty(_SERVER."HTTP_AUTHORIZATION")) {
	//			for( Map.Entry<String,Object> each : _SERVER.entrySet() ) {
	//				String k = each.getKey();
	//				Object v = each.getValue();
	//				if ((k.substring(-18) == "HTTP_AUTHORIZATION") && (!(v == null))) {
	//					_SERVER.put("HTTP_AUTHORIZATION", v);
	//					break;
	//				}
	//
	//			}
	//
	//		}
	//
	//		if ((_SERVER."PHP_AUTH_USER" != null) && (_SERVER."PHP_AUTH_PW" != null)) {
	//			user = _SERVER."PHP_AUTH_USER";
	//			pass = _SERVER."PHP_AUTH_PW";
	//		} else if (!(StringUtils.isEmpty(_SERVER."HTTP_AUTHORIZATION"))) {
	//			auth = _SERVER."HTTP_AUTHORIZATION";
	//			list(user, pass) = StringUtils.split(":");
	//		} else if (!(StringUtils.isEmpty(_SERVER."Authorization"))) {
	//			auth = _SERVER."Authorization";
	//			list(user, pass) = StringUtils.split(":");
	//		}
	//
	//		if ((!(user)) || (!(pass))) {
	//			this.authFailed();
	//		}
	//
	//		return Arrays.asList(user,pass);
	//	}

	/**
	 * Send auth failed Headers and exit
	 * 
	 */
	//	public void authFailed(){
	//		AppContext.getCurrent().getResponse().setHeader("HTTP/1.1", "401 Unauthorized").setHeader("HTTP/1.1", "401 Unauthorized", "WWW-Authenticate", "Basic realm=\"RSS Feeds\"").setBody("HTTP/1.1", "401 Unauthorized", "WWW-Authenticate", "Basic realm=\"RSS Feeds\"", "<h1>401 Unauthorized</h1>").sendResponse("HTTP/1.1", "401 Unauthorized", "WWW-Authenticate", "Basic realm=\"RSS Feeds\"", "<h1>401 Unauthorized</h1>");
	//		exit;
	//	}

	/**
	 * Retrieve Remote Addresses Additional check Headers
	 * 
	 * @return array
	 */
	public List<String> getRemoteAddrHeaders() {
		List<String> headers = new ArrayList<>();
		Simplexml_Element element = AppContext.getCurrent().getConfig().getNode(XML_NODE_REMOTE_ADDR_HEADERS);
		if (element instanceof Mage_Core_Model_Config_Element) {
			for (Simplexml_Element node : element.children()) {
				headers.add(node.getText());
			}

		}

		return headers;
	}

	/**
	 * Retrieve Client Remote Address
	 * 
	 * @param ipToLong format 
	 * @return string  IPv4|long
	 */
	public String getRemoteAddr() {
		if (this._remoteAddr == null) {
			List<String> headers = this.getRemoteAddrHeaders();
			for (String var : headers) {
				this._remoteAddr = this._getRequest().getServer(var, null);
				if (this._remoteAddr != null) {
					break;
				}
			}

			if (this._remoteAddr == null) {
				this._remoteAddr = this._getRequest().getRemoteAddr();
			}

		}

		return this._remoteAddr;
	}

	/**
	 * Retrieve Server IP address
	 * 
	 * @param ipToLong format 
	 * @return string  IPv4|long
	 */
	//	public boolean getServerAddr(boolean ipToLong/* = false */){
	//		NoType address;
	//		address = this._getRequest().getServer("SERVER_ADDR");
	//		if (!(address)) {
	//			return false;
	//		}
	//
	//		return (ipToLong) ? (ip2long(address)) : (address);
	//	}

	/**
	 * Retrieve HTTP "clean" value
	 * 
	 * @param var
	 * @param clean characters 
	 * @return string
	 */
	//	protected String _getHttpCleanValue(String var, boolean clean/* = true */) {
	//		NoType value;
	//		value = this._getRequest().getServer(var, "");
	//		if (clean) {
	//			value = Mage.helper("core/string").cleanString(value);
	//		}
	//
	//		return value;
	//	}

	/**
	 * Retrieve HTTP HOST
	 * 
	 * @param clean characters 
	 * @return string
	 */
	public String getHttpHost(boolean clean/* = true */) {
		return this._getRequest().getHttpHost();
		//		return this._getHttpCleanValue("HTTP_HOST", clean);
	}

	/**
	 * Retrieve HTTP USER AGENT
	 * 
	 * @param clean characters 
	 * @return string
	 */
	public String getHttpUserAgent(boolean clean/* = true */) {
		return this._getRequest().getUserAgent();
		//		return this._getHttpCleanValue("HTTP_USER_AGENT", clean);
	}

	/**
	 * Retrieve HTTP ACCEPT LANGUAGE
	 * 
	 * @param clean characters 
	 * @return string
	 */
	//	public String getHttpAcceptLanguage(boolean clean/* = true */){
	//		return this._getHttpCleanValue("HTTP_ACCEPT_LANGUAGE", clean);
	//	}

	/**
	 * Retrieve HTTP ACCEPT CHARSET
	 * 
	 * @param clean characters 
	 * @return string
	 */
	//	public String getHttpAcceptCharset(boolean clean/* = true */){
	//		return this._getHttpCleanValue("HTTP_ACCEPT_CHARSET", clean);
	//	}

	/**
	 * Retrieve HTTP REFERER
	 * 
	 * @param clean characters 
	 * @return string
	 */
	public String getHttpReferer(boolean clean/* = true */) {
		return this._getRequest().getReferer();
		//		return this._getHttpCleanValue("HTTP_REFERER", clean);
	}

	/**
	 * Returns the REQUEST_URI taking into account
	 * 
	 * platform differences between Apache and IIS
	 * 
	 * @param clean characters 
	 * @return string
	 */
	//	public String getRequestUri(boolean clean/* = false */){
	//		NoType uri;
	//		uri = this._getRequest().getRequestUri();
	//		if (clean) {
	//			uri = Mage.helper("core/string").cleanString("core/string", uri);
	//		}
	//
	//		return uri;
	//	}

	/**
	 * Validate IP address
	 * 
	 * @param address
	 * @return boolean
	 */
	public boolean validateIpAddr(String address) {
		return address.matches("^(1?\\d{1,2}|2([0-4]\\d|5[0-5]))(\\.(1?\\d{1,2}|2([0-4]\\d|5[0-5]))){3}$");
	}
}