package com.naver.mage4j.core.mage.core.model.url.rewrite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.Mage_Core_Model_App;
import com.naver.mage4j.core.mage.core.controller.request.Mage_Core_Controller_Request_Http;
import com.naver.mage4j.core.mage.core.controller.response.Mage_Core_Controller_Response_Http;
import com.naver.mage4j.core.mage.core.controller.varien.router.Mage_Core_Controller_Varien_Router_Abstract;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Cookie;
import com.naver.mage4j.core.mage.core.model.resource.store.Store;
import com.naver.mage4j.core.mage.core.model.resource.store.StoreContext;
import com.naver.mage4j.core.mage.core.model.resource.store.StoreHelper;
import com.naver.mage4j.core.mage.core.model.resource.url.UrlRewrite;
import com.naver.mage4j.core.mage.core.model.resource.url.UrlRewriteContext;
import com.naver.mage4j.core.mage.core.model.resource.url.UrlRewriteHelper;
import com.naver.mage4j.core.util.QueryStringUtils;
import com.naver.mage4j.external.php.Standard;
import com.naver.mage4j.external.varien.Varien_Profiler;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;

public class Mage_Core_Model_Url_Rewrite_Request {
	/**
	 * Collection of front controller"s routers
	 *
	 * @var array
	 */
	protected Map<String, Mage_Core_Controller_Varien_Router_Abstract> _routers;

	/**
	 * Instance of url rewrite model
	 */
	protected UrlRewrite _rewrite;

	private Mage_Core_Controller_Request_Http _request;

	//    /**
	//     * Mage Factory model
	//     */
	//    protected Mage_Core_Model_Factory _factory;

	/**
	 * Constructor
	 * Arguments:
	 *   request  - Zend_Controller_Request_Http
	 *   config   - Mage_Core_Model_Config
	 *   factory  - Mage_Core_Model_Factory
	 *   routers  - array
	 *
	 * @param array $args
	 */
	public Mage_Core_Model_Url_Rewrite_Request(Map<String, Mage_Core_Controller_Varien_Router_Abstract> router) {
		//        $this._factory = !empty($args["factory"]) ? $args["factory"] : Mage::getModel("core/factory");
		//        $this._rewrite = !empty($args["rewrite"]) ? $args["rewrite"] : $this._factory.getModel("core/url_rewrite");

		_request = AppContext.getCurrent().getRequest();

		if (router != null) {
			_routers = router;
		} else {
			_routers = new HashMap<String, Mage_Core_Controller_Varien_Router_Abstract>();
		}
	}

	/**
	 * Implement logic of custom rewrites
	 *
	 * @return bool
	 */
	public boolean rewrite() {
		Mage_Core_Model_App app = AppContext.getCurrent();
		if (!app.getConfig().isInstalled()) {
			return false;
		}

		if (!_request.isStraight(null)) {
			Varien_Profiler.start("mage::dispatch::db_url_rewrite");
			_rewriteDb();
			Varien_Profiler.stop("mage::dispatch::db_url_rewrite");
		}

		Varien_Profiler.start("mage::dispatch::config_url_rewrite");
		_rewriteConfig();
		Varien_Profiler.stop("mage::dispatch::config_url_rewrite");

		return true;
	}

	/**
	 * Implement logic of custom rewrites
	 *
	 * @return bool
	 */
	protected boolean _rewriteDb() {
		Mage_Core_Model_App app = AppContext.getCurrent();
		List<String> requestCases = _getRequestCases();
		UrlRewriteContext urlRewriteContext = UrlRewriteContext.getContext();
		_rewrite = urlRewriteContext.getByRequestPath(app.getStore(), requestCases);

		String fromStoreCode = _request.getQuery("___from_store");
		if (_rewrite == null && fromStoreCode != null) {
			Store fromStore = StoreContext.getContext().getByCode(fromStoreCode);
			if (fromStore == null) {
				return false;
			}

			UrlRewrite fromStoreRewrite = urlRewriteContext.getByRequestPath(fromStore, requestCases);
			if (fromStoreRewrite == null) {
				return false;
			}

			// Load rewrite by id_path
			Store currentStore = app.getStore();
			String idPath = fromStoreRewrite.getIdPath();
			if (idPath != null) {
				_rewrite = new UrlRewrite(fromStoreRewrite);
				_rewrite.setStore(currentStore);
			}

			_setStoreCodeCookie(currentStore.getCode());

			String targetUrl = currentStore.getHelper().getBaseUrl() + _rewrite.getRequestPath();
			_sendRedirectHeaders(targetUrl, true);
		}

		if (_rewrite == null) {
			return false;
		}

		_request.setAlias(UrlRewriteHelper.REWRITE_REQUEST_PATH_ALIAS, _rewrite.getRequestPath());
		_processRedirectOptions();

		return true;
	}

	/**
	 * Set store code to a cookie
	*/
	protected void _setStoreCodeCookie(String storeCode) {
		AppContext.getCurrent().getCookie().set(StoreHelper.COOKIE_NAME, storeCode, Mage_Core_Model_Cookie.AGE_YEAR);
	}

	/**
	 * Process redirect (R) and permanent redirect (RP)
	 */
	protected void _processRedirectOptions() {
		boolean isPermanentRedirectOption = _rewrite.getHelper().hasOption("RP");

		Mage_Core_Model_App _app = AppContext.getCurrent();
		String targetPath = _rewrite.getTargetPath();
		if (targetPath.startsWith("http:") || targetPath.startsWith("https:")) {
			String destinationStoreCode = _rewrite.getStore().getCode();
			_setStoreCodeCookie(destinationStoreCode);
			_sendRedirectHeaders(_rewrite.getTargetPath(), isPermanentRedirectOption);
		}

		String targetUrl = _request.getBaseUrl() + "/" + _rewrite.getTargetPath();

		String storeCode = _app.getStore().getCode();
		if (_app.getStoreConfigAsString("web/url/use_store") != null && storeCode != null) {
			targetUrl = _request.getBaseUrl() + "/" + storeCode + "/" + _rewrite.getTargetPath();
		}

		if (_rewrite.getHelper().hasOption("R") || isPermanentRedirectOption) {
			_sendRedirectHeaders(targetUrl, isPermanentRedirectOption);
		}

		String queryString = _getQueryString();
		if (queryString != null) {
			targetUrl += "?" + queryString;
		}

		_request.setRequestUri(targetUrl);
		_request.setPathInfo(_rewrite.getTargetPath());
	}

	/**
	 * Add location header and disable browser page caching
	 *
	 * @param string $url
	 * @param bool $isPermanent
	 */
	protected void _sendRedirectHeaders(String url, boolean isPermanent) {
		Mage_Core_Controller_Response_Http response = AppContext.getCurrent().getResponse();
		if (isPermanent) {
			//			response.setRawHeader("HTTP/1.1 301 Moved Permanently");
			response.setHttpResponseCode(HttpStatus.MOVED_PERMANENTLY.value());
		}

		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Location", url);
		response.sendHeaders();

		throw new RewriteAndExitException();
	}

	/**
	 * Prepare request cases.
	 *
	 * We have two cases of incoming paths - with and without slashes at the end ("/somepath/" and "/somepath").
	 * Each of them matches two url rewrite request paths
	 * - with and without slashes at the end ("/somepath/" and "/somepath").
	 * Choose any matched rewrite, but in priority order that depends on same presence of slash and query params.
	 */
	protected List<String> _getRequestCases() {
		String pathInfo = AppContext.getCurrent().getRequest().getPathInfo();
		String requestPath = Standard.trim(pathInfo, "/");
		String origSlash = pathInfo.endsWith("/") ? "/" : "";
		// If there were final slash - add nothing to less priority paths. And vice versa.
		String altSlash = !origSlash.isEmpty() ? "" : "/";

		List<String> requestCases = new ArrayList<String>();
		// Query params in request, matching "path + query" has more priority
		String queryString = _getQueryString();
		if (queryString != null) {
			requestCases.add(requestPath + origSlash + "?" + queryString);
			requestCases.add(requestPath + altSlash + "?" + queryString);
		}

		requestCases.add(requestPath + origSlash);
		requestCases.add(requestPath + altSlash);

		return requestCases;
	}

	/**
	 * Prepare and return QUERY_STRING
	 *
	 * @return bool|string
	 */
	protected String _getQueryString() {
		String queryString = AppContext.getCurrent().getRequest().getQueryString();

		if (StringUtils.isEmpty(queryString)) {
			return null;
		}

		Map<String, Object> queryParams = QueryStringUtils.parseQuery(queryString);
		Iterator<Entry<String, Object>> iter = queryParams.entrySet().iterator();
		boolean hasChanges = false;
		while (iter.hasNext()) {
			Entry<String, Object> entry = iter.next();
			if (entry.getKey().startsWith("___")) {
				iter.remove();
				hasChanges = true;
			}
		}

		if (hasChanges) {
			return QueryStringUtils.buildQuery(queryParams);
		} else {
			return queryString;
		}
	}

	/**
	 * Apply configuration rewrites to current url
	 *
	 * @return bool
	 */
	protected boolean _rewriteConfig() {
		Simplexml_Element config = AppContext.getCurrent().getConfig().getNode("global/rewrite");
		if (config == null) {
			return false;
		}

		for (Simplexml_Element rewrite : config.children()) {
			String from = rewrite.getString("from");
			String to = rewrite.getString("to");
			if (StringUtils.isEmpty(from) || StringUtils.isEmpty(to)) {
				continue;
			}

			from = _processRewriteUrl(from);
			to = _processRewriteUrl(to);

			String pathInfo = _request.getPathInfo().replaceAll(from, to);
			if (rewrite.getString("complete") != null) {
				_request.setPathInfo(pathInfo);
			} else {
				_request.rewritePathInfo(pathInfo);
			}
		}

		return true;
	}

	/**
	 * Replace route name placeholders in url to front name
	 * @return string
	 */
	protected String _processRewriteUrl(String url) {
		int startPos = url.indexOf("{");
		if (startPos >= 0) {
			int endPos = url.indexOf("}");
			String routeName = url.substring(startPos + 1, endPos - startPos - 1);
			Mage_Core_Controller_Varien_Router_Abstract router = _getRouterByRoute(routeName);
			if (router != null) {
				String frontName = router.getFrontNameByRoute(routeName);
				url = url.replace("{" + routeName + "}", frontName);
			}
		}

		return url;
	}

	/**
	 * Retrieve router by name
	 *
	 * @param string $name
	 * @return Mage_Core_Controller_Varien_Router_Abstract|bool
	 */
	protected Mage_Core_Controller_Varien_Router_Abstract _getRouter(String name) {
		return _routers.get(name);
	}

	/**
	 * Retrieve router by name
	 *
	 * @param string $routeName
	 * @return Mage_Core_Controller_Varien_Router_Abstract
	 */
	protected Mage_Core_Controller_Varien_Router_Abstract _getRouterByRoute(String routeName) {
		// empty route supplied - return base url
		Mage_Core_Controller_Varien_Router_Abstract router;
		if (StringUtils.isEmpty(routeName)) {
			router = _getRouter("standard");
		} else if (_getRouter("admin").getFrontNameByRoute(routeName) != null) {
			// try standard router url assembly
			router = _getRouter("admin");
		} else if (_getRouter("standard").getFrontNameByRoute(routeName) != null) {
			// try standard router url assembly
			router = _getRouter("standard");
		} else {
			// try custom router url assembly
			router = _getRouter(routeName);
			if (router == null) {
				// get default router url
				router = _getRouter("default");
			}
		}
		return router;
	}
}
