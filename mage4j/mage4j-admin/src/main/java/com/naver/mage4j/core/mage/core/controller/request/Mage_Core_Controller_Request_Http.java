package com.naver.mage4j.core.mage.core.controller.request;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.controller.varien.router.Mage_Core_Controller_Varien_Router_Abstract;
import com.naver.mage4j.external.php.Standard;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;

public class Mage_Core_Controller_Request_Http {
	private final Logger log = LoggerFactory.getLogger(Mage_Core_Controller_Request_Http.class);

	public static final String XML_NODE_DIRECT_FRONT_NAMES = "global/request/direct_front_name";
	public static final int DEFAULT_HTTP_PORT = 80;
	public static final int DEFAULT_HTTPS_PORT = 443;

	public final HttpServletRequest servletRequest;

	/**
	 * Streight request flag.
	 * If flag is determined no additional logic is applicable
	 *
	 * @var $_isStraight bool
	 */
	private boolean _isStraight = false;

	/**
	 * Has the action been dispatched?
	 */
	protected boolean _dispatched = false;

	/**
	 * PATH_INFO
	 * @var string
	 */
	private String _pathInfo = "";

	/**
	* Base URL of request
	*/
	protected String _baseUrl = null;

	/**
	 * Module
	 */
	protected String _module;

	/**
	 * Module key for retrieving module from params
	 */
	protected String _moduleKey = "module";

	/**
	 * Controller
	 */
	protected String _controller;

	/**
	 * Controller key for retrieving controller from params
	 */
	protected String _controllerKey = "controller";

	protected String _controllerModule = null;

	/**
	 * Action
	 */
	protected String _action;

	/**
	 * Action key for retrieving action from params
	 */
	protected String _actionKey = "action_key";

	/**
	 * Alias keys for request parameters
	 */
	protected Map<String, String> _aliases = new HashMap<String, String>();

	/**
	* Path info array used before applying rewrite from config
	*
	* @var null || array
	*/
	protected String[] _rewritedPathInfo = null;
	protected String _requestedRouteName = null;
	protected Map<String, String> _routingInfo = new HashMap<String, String>();

	protected Map<String, String> _directFrontNames = null;

	protected String _route;

	public Mage_Core_Controller_Request_Http(HttpServletRequest servlerRequest) {
		this.servletRequest = servlerRequest;
	}

	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}

	public void setRouteName(String route) {
		_route = route;
		Mage_Core_Controller_Varien_Router_Abstract router = AppContext.getCurrent().getFrontController().getRouterByRoute(route);
		if (router == null) {
			return;
		}

		String module = router.getFrontNameByRoute(route);
		if (module != null) {
			setModuleName(module);
		}
	}

	public String getRouteName() {
		return _route;
	}

	/**
	 * Set a key alias
	 *
	 * Set an alias used for key lookups. $name specifies the alias, $target
	 * specifies the actual key to use.
	 */
	public void setAlias(String name, String target) {
		_aliases.put(name, target);
	}

	/**
	 * Retrieve an alias
	 *
	 * Retrieve the actual key represented by the alias $name.
	 *
	 * @param string $name
	 * @return string|null Returns null when no alias exists
	 */
	public String getAlias(String name) {
		return _aliases.get(name);
	}

	/**
	 * Retrieve the module name
	 *
	 * @return string
	 */
	public String getModuleName() {
		if (null == _module) {
			_module = getParamterValue(_moduleKey);
		}

		return _module;
	}

	/**
	 * Set the module name to use
	 */
	public void setModuleName(String value) {
		_module = value;
	}

	/**
	 * Retrieve the controller name
	 *
	 * @return string
	 */
	public String getControllerName() {
		if (null == _controller) {
			_controller = getParamterValue(_controllerKey);
		}

		return _controller;
	}

	/**
	 * Set the controller name to use
	 *
	 * @param string $value
	 * @return Zend_Controller_Request_Abstract
	 */
	public void setControllerName(String value) {
		_controller = value;
	}

	/**
	 * Retrieve the action name
	 *
	 * @return string
	 */
	public String getActionName() {
		if (null == _action) {
			_action = getParamterValue(_actionKey);
		}

		return _action;
	}

	/**
	 * Set the action name
	 */
	public Mage_Core_Controller_Request_Http setActionName(String value) {
		_action = value;
		/**
		 * @see ZF-3465
		 */
		if (null == value) {
			// TODO 파마미터 변경 허용할 지 여부 결정 필요.
			//            $this->setParam($this->getActionKey(), $value);
		}

		return this;
	}

	public boolean isStraight(Boolean flag) {
		if (flag != null) {
			_isStraight = flag;
		}

		return _isStraight;
	}

	public String getQueryString() {
		return servletRequest.getQueryString();
	}

	public boolean isGet() {
		return "GET".equalsIgnoreCase(servletRequest.getMethod());
	}

	public boolean isPost() {
		return "POST".equalsIgnoreCase(servletRequest.getMethod());
	}

	public String getParamterValue(String name) {
		return servletRequest.getParameter(name);
	}

	public String getParam(String name) {
		return servletRequest.getParameter(name);
	}

	/**
	 * _GET, _POST, _COOKIE의 값을 추출
	 * @param name
	 * @return
	 */
	public String getRequestParam(String name) {
		String result = servletRequest.getParameter(name);
		if (result == null) {
			result = AppContext.getCurrent().getCookie().get(name);
		}

		return result;
	}

	public void setPathInfo(String pathInfo) {
		if (pathInfo == null) {
			String baseUrl = getBaseUrl(false); // this actually calls setBaseUrl() & setRequestUri()
			String baseUrlRaw = getBaseUrl(false);
			String baseUrlEncoded;
			try {
				baseUrlEncoded = URLEncoder.encode(baseUrlRaw, AppContext.DEFAULT_ENCODING);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}

			String requestUri = getRequestUri();
			if (requestUri == null) {
				return;
			}

			// Remove the query string from REQUEST_URI
			int pos = requestUri.indexOf("?");
			if (pos >= 0) {
				requestUri = requestUri.substring(0, pos);
			}

			if (StringUtils.isNotEmpty(baseUrl) || StringUtils.isNotEmpty(baseUrlRaw)) {
				if (requestUri.startsWith(baseUrl)) {
					pathInfo = requestUri.substring(baseUrl.length());
				} else if (requestUri.startsWith(baseUrlRaw)) {
					pathInfo = requestUri.substring(baseUrlRaw.length());
				} else if (requestUri.startsWith(baseUrlEncoded)) {
					pathInfo = requestUri.substring(baseUrlEncoded.length());
				} else {
					pathInfo = requestUri;
				}
			} else {
				pathInfo = requestUri;
			}
		}

		_pathInfo = pathInfo;
	}

	public String getPathInfo() {
		if (_pathInfo == null) {
			setPathInfo(null);
		}

		return _pathInfo;

		// TODO megento에서 사용하는 pathInfo의 semantic을 제대로 이해 필요.
		// setPathInfo로 초기화한 코드를 그대로 사용할지 여부 확인 필요.
		//        return servletRequest.getPathInfo();
	}

	public String getOriginalPathInfo() {
		// TOOD pathInfo와 origianPathInfo의 차이를 조사하고 이에 따른 처리 추가.
		throw new UnsupportedOperationException();
	}

	/**
	 * Specify new path info
	 * It happen when occur rewrite based on configuration
	 */
	public void rewritePathInfo(String pathInfo) {
		if (!StringUtils.equals(pathInfo, getPathInfo()) && _rewritedPathInfo == null) {
			_rewritedPathInfo = StringUtils.split(Standard.trim(getPathInfo(), "/"), "/");
		}

		setPathInfo(pathInfo);
	}

	/**
	 * Set the base URL of the request; i.e., the segment leading to the script name
	 *
	 * E.g.:
	 * - /admin
	 * - /myapp
	 * - /subdir/index.php
	 *
	 * Do not use the full URI when providing the base. The following are
	 * examples of what not to use:
	 * - http://example.com/admin (should be just /admin)
	 * - http://example.com/subdir/index.php (should be just /subdir/index.php)
	 *
	 * If no $baseUrl is provided, attempts to determine the base URL from the
	 * environment, using SCRIPT_FILENAME, SCRIPT_NAME, PHP_SELF, and
	 * ORIG_SCRIPT_NAME in its determination.
	 */
	public void setBaseUrl(String baseUrl) {
		_baseUrl = servletRequest.getContextPath();
		if (_baseUrl != null) {
			return;
		}

		// TODO baseUrl의 의미를 파악한 이후 다시 로직 작성.
		if (baseUrl == null) {
			ServerOption serverOption = getServerOption();
			String scriptFilename = StringUtils.defaultString(serverOption.getScriptFilename(), "");
			String scriptName = serverOption.getScriptName();
			if (scriptName != null && FilenameUtils.getBaseName(scriptName).equals(scriptFilename)) {
				baseUrl = scriptName;
			} else {
				String phpSelf = serverOption.getPhpSelf();
				if (phpSelf != null && FilenameUtils.getBaseName(phpSelf).equals(scriptFilename)) {
					baseUrl = phpSelf;
				} else {
					String origScriptName = serverOption.getOrigScriptName();
					if (origScriptName != null && FilenameUtils.getBaseName(origScriptName).equals(scriptFilename)) {
						baseUrl = origScriptName;
					} else {
						// Backtrack up the script_filename to find the portion matching
						// php_self
						String path = StringUtils.defaultString(phpSelf, "");
						String file = StringUtils.defaultString(scriptName, "");
						String[] segs = Standard.trim(file, "/").split("/");
						ArrayUtils.reverse(segs);
						int index = 0;
						int last = segs.length;
						baseUrl = "";
						do {
							String seg = segs[index];
							baseUrl = "/" + seg + baseUrl;
							index++;
						} while ((last > index) && (path.indexOf(baseUrl) > 0));
					}
				}
			}

			// Does the baseUrl have anything in common with the request_uri?
			String requestUri = getRequestUri();

			if (requestUri.startsWith(baseUrl)) {
				// full $baseUrl matches
				_baseUrl = baseUrl;
				return;
			}

			String basename = FilenameUtils.getBaseName(baseUrl);
			if (requestUri.startsWith(basename)) {
				// directory portion of $baseUrl matches
				_baseUrl = Standard.rtrim(basename, "/");
				return;
			}

			String truncatedRequestUri;
			int questionMarkIndex = requestUri.indexOf('?');
			if (questionMarkIndex >= 0) {
				truncatedRequestUri = requestUri.substring(0, questionMarkIndex);
			} else {
				truncatedRequestUri = requestUri;
			}

			if (basename.isEmpty() || truncatedRequestUri.indexOf(basename) > 0) {
				// no match whatsoever; set it blank
				_baseUrl = "";
				return;
			}

			// If using mod_rewrite or ISAPI_Rewrite strip the script filename
			// out of baseUrl. $pos !== 0 makes sure it is not matching a value
			// from PATH_INFO or QUERY_STRING
			if (requestUri.length() >= baseUrl.length()) {
				int pos = requestUri.indexOf(baseUrl);
				if (pos > 0) {
					baseUrl = requestUri.substring(0, pos + baseUrl.length());
				}
			}
		}

		_baseUrl = Standard.rtrim(baseUrl, "/");
	}

	/**
	 * Everything in REQUEST_URI before PATH_INFO
	 * <form action="<?=$baseUrl?>/news/submit" method="POST"/>
	 */
	public String getBaseUrl(boolean raw) {
		if (_baseUrl == null) {
			setBaseUrl(null);
		}

		String url;
		try {
			url = raw ? _baseUrl : URLDecoder.decode(_baseUrl, AppContext.DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		return url.replace("\\", "/");
	}

	public String getBaseUrl() {
		return getBaseUrl(false);
	}

	public String getBasePath() {
		String path = getBaseUrl();
		if (StringUtils.isEmpty(path)) {
			path = "/";
		} else {
			path = path.replace("\\", "/");
		}

		return Standard.rtrim(path, "/");
	}

	/**
	 * Set the REQUEST_URI on which the instance operates
	 *
	 * If no request URI is passed, uses the value in $_SERVER['REQUEST_URI'],
	 * $_SERVER['HTTP_X_REWRITE_URL'], or $_SERVER['ORIG_PATH_INFO'] + $_SERVER['QUERY_STRING'].
	 */
	public void setRequestUri(String requestUri) {
		// TODO [MVC] request URI를 수정할 필요가 있을지의 여부와 우회방법 모색.
		//        if ($requestUri === null) {
		//            if (isset($_SERVER['HTTP_X_ORIGINAL_URL'])) { 
		//                // IIS with Microsoft Rewrite Module
		//                $requestUri = $_SERVER['HTTP_X_ORIGINAL_URL'];
		//            } elseif (isset($_SERVER['HTTP_X_REWRITE_URL'])) { 
		//                // IIS with ISAPI_Rewrite
		//                $requestUri = $_SERVER['HTTP_X_REWRITE_URL'];
		//            } elseif (
		//                // IIS7 with URL Rewrite: make sure we get the unencoded url (double slash problem)
		//                isset($_SERVER['IIS_WasUrlRewritten'])
		//                && $_SERVER['IIS_WasUrlRewritten'] == '1'
		//                && isset($_SERVER['UNENCODED_URL'])
		//                && $_SERVER['UNENCODED_URL'] != ''
		//                ) {
		//                $requestUri = $_SERVER['UNENCODED_URL'];
		//            } elseif (isset($_SERVER['REQUEST_URI'])) {
		//                $requestUri = $_SERVER['REQUEST_URI'];
		//                // Http proxy reqs setup request uri with scheme and host [and port] + the url path, only use url path
		//                $schemeAndHttpHost = $this->getScheme() . '://' . $this->getHttpHost();
		//                if (strpos($requestUri, $schemeAndHttpHost) === 0) {
		//                    $requestUri = substr($requestUri, strlen($schemeAndHttpHost));
		//                }
		//            } elseif (isset($_SERVER['ORIG_PATH_INFO'])) { // IIS 5.0, PHP as CGI
		//                $requestUri = $_SERVER['ORIG_PATH_INFO'];
		//                if (!empty($_SERVER['QUERY_STRING'])) {
		//                    $requestUri .= '?' . $_SERVER['QUERY_STRING'];
		//                }
		//            } else {
		//                return $this;
		//            }
		//        } elseif (!is_string($requestUri)) {
		//            return $this;
		//        } else {
		//            // Set GET items, if available
		//            if (false !== ($pos = strpos($requestUri, '?'))) {
		//                // Get key => value pairs and set $_GET
		//                $query = substr($requestUri, $pos + 1);
		//                parse_str($query, $vars);
		//                $this->setQuery($vars);
		//            }
		//        }
		//
		//        _requestUri = $requestUri;
		throw new UnsupportedOperationException();
	}

	public String getRequestUri() {
		return servletRequest.getRequestURI();
	}

	public String getScheme() {
		return servletRequest.getScheme();
	}

	public String getHttpHost() {
		String scheme = servletRequest.getScheme();
		String name = servletRequest.getLocalAddr();
		int port = servletRequest.getLocalPort();
		if (("http".equalsIgnoreCase(scheme) && port == 80)
			|| ("https".equalsIgnoreCase(scheme) && port == 443)) {
			return name;
		} else {
			return name + ":" + port;
		}
	}

	public void setDispatched(boolean flag) {
		_dispatched = flag;
	}

	/**
	 * Determine if the request has been dispatched
	 */
	public boolean isDispatched() {
		return _dispatched;
	}

	/**
	 * Is https secure request
	 */
	public boolean isSecure() {
		return "https".equalsIgnoreCase(servletRequest.getScheme());
	}

	/**
	 * Specify module name where was found currently used controller
	 *
	 * @param   string $module
	 * @return  Mage_Core_Controller_Request_Http
	 */
	public void setControllerModule(String module) {
		_controllerModule = module;
	}

	/**
	 * Get module name of currently used controller
	 *
	 * @return  string
	 */
	public String getControllerModule() {
		return _controllerModule;
	}

	/**
	 * Retrieve a member of the $_GET superglobal
	 *
	 * If no $key is passed, returns the entire $_GET array.
	 *
	 * @todo How to retrieve from nested arrays
	 * @param string $key
	 * @param mixed $default Default value to use if key not found
	 * @return mixed Returns null if key does not exist
	 */
	public String getQuery(String key) {
		// TODO GET의 파라미터만 가져와야 하는지 스펙 확인 필요.
		return servletRequest.getParameter(key);
	}

	public Map<String, List<String>> getQueryMap() {
		return servletRequest.getParameterMap();
	}

	public String getRequestedRouteName() {
		String result = _routingInfo.get("requested_route");
		if (result != null) {
			return result;
		}

		if (_requestedRouteName == null) {
			if (_rewritedPathInfo != null && _rewritedPathInfo.length > 0) {
				String fronName = _rewritedPathInfo[0];
				Mage_Core_Controller_Varien_Router_Abstract router = AppContext.getCurrent().getFrontController().getRouterByFrontName(fronName);
				_requestedRouteName = router.getRouteByFrontName(fronName);
			} else {
				// no rewritten path found, use default route name
				return getRouteName();
			}
		}
		return _requestedRouteName;
	}

	public String getRequestedControllerName() {
		String result = _routingInfo.get("requested_controller");
		if (result != null) {
			return result;
		}

		if (_rewritedPathInfo != null && _rewritedPathInfo.length > 1) {
			return _rewritedPathInfo[1];
		}

		return getControllerName();
	}

	public String getRequestedActionName() {
		String result = _routingInfo.get("requested_action");
		if (result != null) {
			return result;
		}

		if (_rewritedPathInfo != null && _rewritedPathInfo.length > 2) {
			return _rewritedPathInfo[2];
		}

		return getActionName();
	}

	public String getUserParam(String key) {
		// TODO Request 객체에 넘어온 파라미터 이외에 사용자가 직접 설정하여 사용할지 여부 확인.
		throw new UnsupportedOperationException();
	}

	public Map<String, String> getUserParams() {
		// TODO Request 객체에 넘어온 파라미터 이외에 사용자가 직접 설정하여 사용할지 여부 확인.
		throw new UnsupportedOperationException();
	}

	/**
	 * Check if code declared as direct access frontend name
	 * this mean what this url can be used without store code
	 */
	public boolean isDirectAccessFrontendName(String code) {
		Map<String, String> names = getDirectFrontNames();
		return names.containsKey(code);
	}

	/**
	 * Get list of front names available with access without store code
	 *
	 * @return array
	 */
	public Map<String, String> getDirectFrontNames() {
		if (_directFrontNames == null) {
			Simplexml_Element names = AppContext.getCurrent().getConfig().getNode(XML_NODE_DIRECT_FRONT_NAMES);
			if (names != null) {
				Map<String, Object> map = names.toMap();
				for (Entry<String, Object> each : map.entrySet()) {
					if (each.getValue() == null || each.getValue() instanceof String) {
						throw new IllegalArgumentException("It contains not a string value.(" + each.getKey() + ")");
					}
				}

				_directFrontNames = (Map)map;
			} else {
				return new HashMap<String, String>();
			}
		}

		return _directFrontNames;
	}

	public String getUserAgent() {
		return getHeader("user-agent");
	}

	public String getReferer() {
		return getHeader("referer");
	}

	public String getHeader(String name) {
		return servletRequest.getHeader(name);
	}

	public String getServer(String name, String defaultValue) {
		throw new UnsupportedOperationException();
	}

	/**
	 * http://php.net/manual/en/reserved.variables.server.php
	 * http://ca.php.net/manual/en/reserved.variables.server.php
	 */
	public class ServerOption {
		public boolean isSecureSupported() {
			return isSecure();
			// TODO https 지원 여부 확인 기능 추가.
			//		return !empty($_SERVER['HTTPS']) && ('off' != $_SERVER['HTTPS']);
			//			throw new UnsupportedOperationException();
		}

		public int getServerPort() {
			//			return isset($_SERVER['SERVER_PORT']);
			//			throw new UnsupportedOperationException();
			return servletRequest.getRemotePort();
		}

		/** The absolute pathname of the currently executing script. */
		public String getScriptFilename() {
			//			return $_SERVER["SCRIPT_FILENAME"];
			throw new UnsupportedOperationException();
		}

		/** Contains the current script's path. */
		public String getScriptName() {
			//			return $_SERVER['SCRIPT_NAME'];
			throw new UnsupportedOperationException();
		}

		public String getPhpSelf() {
			//			return $_SERVER['PHP_SELF'];
			throw new UnsupportedOperationException();
		}

		public String getOrigScriptName() {
			//			return $_SERVER['ORIG_SCRIPT_NAME'];
			throw new UnsupportedOperationException();
		}
	}

	ServerOption serverOption = new ServerOption();

	public ServerOption getServerOption() {
		return serverOption;
	}

	public String getRemoteAddr() {
		return servletRequest.getRemoteAddr();
	}

	/**
	 * Request's original information before forward.
	 *
	 * @var array
	 */
	protected Map<String, Object> _beforeForwardInfo = new HashMap<>();

	/**
	 * Collect properties changed by _forward in protected storage
	 * before _forward was called first time.
	 *
	 * @return Mage_Core_Controller_Varien_Action
	 */
	public Mage_Core_Controller_Request_Http initForward() {
		if (MapUtils.isEmpty(this._beforeForwardInfo)) {
			Map<String, Object> forwardInfo = new HashMap<>();
			forwardInfo.put("params", this.getParams());
			forwardInfo.put("action_name", this.getActionName());
			forwardInfo.put("controller_name", this.getControllerName());
			forwardInfo.put("module_name", this.getModuleName());

			_beforeForwardInfo = forwardInfo;
		}

		return this;
	}

	private Map<String, Object> getParams() {
		Map<String, Object> params = new HashMap<>();
		@SuppressWarnings("unchecked")//
		Map<String, Object> parameterMap = servletRequest.getParameterMap();
		for (Map.Entry<String, Object> each : parameterMap.entrySet()) {
			String[] value = (String[])each.getValue();
			if (value == null || value.length == 0) {
				params.put(each.getKey(), value);
			} else if (value.length == 1) {
				params.put(each.getKey(), value[0]);
			} else {
				params.put(each.getKey(), value);
			}
		}

		return params;
	}

	public void setParams(Map<String, Object> params) {
		throw new UnsupportedOperationException();
	}
}
