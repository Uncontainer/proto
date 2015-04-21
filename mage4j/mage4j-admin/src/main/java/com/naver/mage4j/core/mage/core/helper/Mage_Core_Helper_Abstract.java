package com.naver.mage4j.core.mage.core.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.controller.request.Mage_Core_Controller_Request_Http;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Layout;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Url;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Url.RouteParams;
import com.naver.mage4j.core.mage.core.model.translate.Mage_Core_Model_Translate_Expr;
import com.naver.mage4j.core.util.EncodeUtils;
import com.naver.mage4j.core.util.Flags;
import com.naver.mage4j.core.util.HtmlUtils;
import com.naver.mage4j.core.util.PhpStringUtils;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;

/**
 * Abstract helper
 */
public abstract class Mage_Core_Helper_Abstract {
	/**
	 * Helper module name
	 */
	protected String _moduleName;

	/**
	 * Request object
	 */
	protected Mage_Core_Controller_Request_Http _request;

	/**
	 * Layout model object
	 */
	protected Mage_Core_Model_Layout _layout;

	@Autowired
	protected Mage_Core_Model_Url modelUrl;

	/**
	 * Retrieve request object
	 * 
	 * @return Zend_Controller_Request_Http
	 */
	protected Mage_Core_Controller_Request_Http _getRequest() {
		if (this._request == null) {
			this._request = AppContext.getCurrent().getRequest();
		}

		return this._request;
	}

	/**
	 * Loading cache data
	 * 
	 * @param id
	 * @return mixed
	 */
	protected Object _loadCache(String id) {
		return AppContext.getCurrent().loadCache(id);
	}

	/**
	 * Saving cache
	 * 
	 * @param data
	 * @param id
	 * @param tags
	 * @return Mage_Core_Helper_Abstract
	 */
	protected Mage_Core_Helper_Abstract _saveCache(String data, String id, List<String> tags/* = Collections.emptyMap() */, int lifeTimeInSeconds/* = false */) {
		AppContext.getCurrent().saveCache(data, id, tags, lifeTimeInSeconds);
		return this;
	}

	/**
	 * Removing cache
	 * 
	 * @param id
	 * @return Mage_Core_Helper_Abstract
	 */
	protected Mage_Core_Helper_Abstract _removeCache(String id) {
		AppContext.getCurrent().removeCache(id);
		return this;
	}

	/**
	 * Cleaning cache
	 * 
	 * @param tags
	 * @return Mage_Core_Helper_Abstract
	 */
	protected Mage_Core_Helper_Abstract _cleanCache(List<String> tags/* = Collections.emptyMap() */) {
		AppContext.getCurrent().cleanCache(tags);
		return this;
	}

	/**
	 * Retrieve helper module name
	 * 
	 * @return string
	 */
	protected String _getModuleName() {
		if (this._moduleName == null) {
			String moduleName = getClass().getSimpleName();
			this._moduleName = moduleName.substring(0, moduleName.indexOf("_Helper"));
		}

		return this._moduleName;
	}

	/**
	 * Check whether or not the module output is enabled in Configuration
	 * 
	 * @param moduleName name 
	 * @return boolean
	 */
	public boolean isModuleOutputEnabled(String moduleName/* = null */) {
		if ((moduleName) == (null)) {
			(moduleName) = (this._getModuleName());
		}

		if (!(this.isModuleEnabled(moduleName))) {
			return false;
		}

		if (AppContext.getCurrent().getStoreConfigFlag("advanced/modules_disable_output/" + moduleName)) {
			return false;
		}

		return true;
	}

	/**
	 * Check is module exists and enabled in global config.
	 * 
	 * @param moduleName Mage_Core 
	 * @return boolean
	 */
	public boolean isModuleEnabled(String moduleName/* = null */) {
		if ((moduleName) == (null)) {
			(moduleName) = (this._getModuleName());
		}

		Simplexml_Element node = AppContext.getCurrent().getConfig().getNode("modules/" + moduleName);
		if (node == null) {
			return false;
		}

		String isActive = node.getString("active");
		if ((isActive == null) || (!("true".equals(isActive) || "1".equals(isActive)))) {
			return false;
		}

		return true;
	}

	/**
	 * Translate
	 * 
	 * @return string
	 */
	public String __(Object... args) {
		Mage_Core_Model_Translate_Expr expr = new Mage_Core_Model_Translate_Expr((String)args[0], this._getModuleName());

		List<Object> translateArgs = new ArrayList<Object>(args.length);
		for (int i = 1; i < args.length; i++) {
			translateArgs.add(args[i]);
		}

		return AppContext.getCurrent().getTranslator().translate(expr, translateArgs);
	}

	public String htmlEscape(String data, List<String> allowedTags/* = null */) {
		return this.escapeHtml(data, allowedTags);
	}

	/**
	 * Escape html entities
	 * 
	 * @param data
	 * @param allowedTags
	 * @return mixed
	 */
	public String escapeHtml(String data, List<String> allowedTags/* = null */) {
		return HtmlUtils.escapeHtml(data, allowedTags);
	}

	/**
	 * Remove html tags, but leave "<" and ">" signs
	 * 
	 * @param html
	 * @return string
	 */
	public String removeTags(String html) {
		html = html.replaceAll("# <(?![/a-z]) | (?<=\\s)>(?![a-z]) #exi", "htmlentities('$0')");
		html = HtmlUtils.strip_tags(html);
		return HtmlUtils.htmlspecialchars_decode(html);
	}

	/**
	 * Wrapper for standart strip_tags() function with extra functionality for html entities
	 * 
	 * @param data
	 * @param allowableTags
	 * @param escape
	 * @return string
	 */
	public String stripTags(String data, List<String> allowableTags/* = null */, boolean escape/* = false */) {
		String result = HtmlUtils.strip_tags(data, allowableTags);
		return escape ? this.escapeHtml(result, allowableTags) : result;
	}

	public String urlEscape(String data) {
		return this.escapeUrl(data);
	}

	/**
	 * Escape html entities in url
	 * 
	 * @param data
	 * @return string
	 */
	public String escapeUrl(String data) {
		return HtmlUtils.htmlspecialchars(data);
	}

	/**
	 * Escape quotes in java script
	 * 
	 * @param data
	 * @param quote
	 * @return mixed
	 */
	public List<String> jsQuoteEscape(List<String> data, String quote/* = "\\'" */) {
		List<String> result = new ArrayList<String>();
		for (String item : data) {
			result.add(item.replace(quote, "\\\\" + quote));
		}

		return result;
	}

	public String jsQuoteEscape(String data, String quote/* = "\\'" */) {
		return data.replace(quote, "\\\\" + quote);
	}

	/**
	 * Escape quotes inside html attributesUse $addSlashes = false for escaping js that inside html attribute (onClick, onSubmit etc)
	 * 
	 * @param data
	 * @param addSlashes
	 * @return string
	 */
	public String quoteEscape(String data, boolean addSlashes/* = false */) {
		if ((addSlashes) == (true)) {
			(data) = (HtmlUtils.addslashes(data));
		}

		return HtmlUtils.htmlspecialchars(data, Flags.ENT_QUOTES, null, false);
	}

	/**
	 * Retrieve url
	 * 
	 * @param route
	 * @param params
	 * @return string
	 */
	protected String _getUrl(String route, RouteParams params/* = Collections.emptyMap() */) {
		return modelUrl.getUrl(route, params);
	}

	/**
	 * Declare layout
	 * 
	 * @param layout
	 * @return Mage_Core_Helper_Abstract
	 */
	public Mage_Core_Helper_Abstract setLayout(Mage_Core_Model_Layout layout) {
		this._layout = layout;
		return this;
	}

	/**
	 * Retrieve layout model object
	 * 
	 * @return Mage_Core_Model_Layout
	 */
	public Mage_Core_Model_Layout getLayout() {
		return this._layout;
	}

	/**
	 * base64_encode() for URLs encoding
	 * 
	 * @param url
	 * @return string
	 */
	public String urlEncode(String url) {
		return PhpStringUtils.replaceEach(EncodeUtils.base64_encode(url), "+/=", "-_,");
	}

	/**
	 * base64_dencode() for URLs dencoding
	 * 
	 * @param url
	 * @return string
	 */
	public String urlDecode(String url) {
		url = EncodeUtils.base64_decode(PhpStringUtils.replaceEach(url, "-_,", "+/="));
		return modelUrl.sessionUrlVar(url);
	}

	/**
	 * Translate array
	 * 
	 * @param arr
	 * @return array
	 */
	public Map<String, Object> translateArray(Map<String, Object> arr/* = Collections.emptyMap() */) {
		for (Map.Entry<String, Object> each : arr.entrySet()) {
			String k = each.getKey();
			Object v = each.getValue();
			if (v instanceof Map) {
				(v) = (translateArray((Map)v));
			} else if ((k) == ("label")) {
				(v) = (__(v));
			}

			arr.put(k, v);
		}

		return arr;
	}
}