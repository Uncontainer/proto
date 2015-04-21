package com.naver.mage4j.external.zend.http;


/**
 * Zend_Http_Client is an implementation of an HTTP client in PHP. The clientsupports basic features like sending different HTTP requests and handlingredirections, as well as more advanced features like proxy settings, HTTPauthentication and cookie persistence (using a Zend_Http_CookieJar object)
 */
public class Zend_Http_Client {
	/**
	 * Status for unmasking GET array params
	 */
	protected boolean _unmaskStatus = false;

	/**
	 * Set the unmask feature for GET parameters as arrayExample:foo%5B0%5D=a&foo%5B1%5D=bbecomesfoo=a&foo=bThis is usefull for some services
	 * 
	 * @param status
	 * @return Zend_Http_Client
	 */
	public Zend_Http_Client setUnmaskStatus(boolean status/* = true */){
		(this._unmaskStatus) = (status);
		return this;
	}


}