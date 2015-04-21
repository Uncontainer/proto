package com.naver.mage4j.core.mage.core.controller.varien;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.core.mage.adminhtml.Mage_Adminhtml_Helper_Data;
import com.naver.mage4j.core.mage.core.Mage_Core_Exception;
import com.naver.mage4j.core.mage.core.Mage_Core_Model_App;
import com.naver.mage4j.core.mage.core.RedirectAndExitException;
import com.naver.mage4j.core.mage.core.controller.request.Mage_Core_Controller_Request_Http;
import com.naver.mage4j.core.mage.core.controller.response.Mage_Core_Controller_Response_Http;
import com.naver.mage4j.core.mage.core.controller.varien.router.Mage_Core_Controller_Varien_Router_Abstract;
import com.naver.mage4j.core.mage.core.controller.varien.router.Mage_Core_Controller_Varien_Router_Default;
import com.naver.mage4j.core.mage.core.model.event.EventDispatcher;
import com.naver.mage4j.core.mage.core.model.resource.store.UrlType;
import com.naver.mage4j.core.mage.core.model.url.rewrite.Mage_Core_Model_Url_Rewrite_Request;
import com.naver.mage4j.external.php.Standard;
import com.naver.mage4j.external.varien.Varien_Profiler;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;

public class Mage_Core_Controller_Varien_Front {
	public static final String XML_STORE_ROUTERS_PATH = "web/routers";

	private final Mage_Core_Model_App app;

	Map<String, String> _defaults;

	private Mage_Core_Controller_Varien_Action ___action;
	private boolean ___noRender;

	/**
	 * Available routers array
	 */
	private Map<String, Mage_Core_Controller_Varien_Router_Abstract> _routers = new HashMap<String, Mage_Core_Controller_Varien_Router_Abstract>();

	public Mage_Core_Controller_Varien_Front(Mage_Core_Model_App app) {
		this.app = app;
	}

	public void setDefault(Map<String, String> options) {
		_defaults = options;
	}

	public void setDefault(String key, String value) {
		_defaults.put(key, value);
	}

	public String getDefault(String key) {
		return _defaults.get(key);
	}

	/**
	 * Init Front Controller
	 *
	 * @return Mage_Core_Controller_Varien_Front
	 */
	public void init() {
		EventDispatcher eventDispatcher = app.getEventDispatcher();
		eventDispatcher.dispatchEvent("controller_front_init_before", Collections.singletonMap("front", (Object)this));

		Map<String, Map<String, Object>> routersInfo = app.getStoreConfigAsMap(XML_STORE_ROUTERS_PATH);
		if (routersInfo.isEmpty()) {
			throw new IllegalStateException("No routers found.");
		}

		Varien_Profiler.start("mage::app::init_front_controller::collect_routers");
		for (Map.Entry<String, Map<String, Object>> each : routersInfo.entrySet()) {
			String routerCode = each.getKey();
			Map<String, Object> routerInfo = each.getValue();

			String disabled = (String)routerInfo.get("disabled");
			if (disabled != null && !"".equals(disabled)) {
				continue;
			}

			String className = (String)routerInfo.get("class");
			if (className != null) {
				Mage_Core_Controller_Varien_Router_Abstract router = createRouter(className);
				String area = (String)routerInfo.get("area");
				if (StringUtils.isNotBlank(area)) {
					router.collectRoutes(area, routerCode);
				}

				addRouter(routerCode, router);
			}
		}

		Varien_Profiler.stop("mage::app::init_front_controller::collect_routers");

		eventDispatcher.dispatchEvent("controller_front_init_routers", Collections.singletonMap("front", (Object)this));

		// Add default router at the last
		Mage_Core_Controller_Varien_Router_Default defaultRouter = new Mage_Core_Controller_Varien_Router_Default();
		addRouter("default", defaultRouter);
	}

	public void dispatch() throws IOException {
		Mage_Core_Controller_Request_Http request = app.getRequest();

		// If pre-configured, check equality of base URL and requested URL
		_checkBaseUrl(request);
		request.setPathInfo(null);
		request.setDispatched(false);

		_getRequestRewriteController().rewrite();

		Varien_Profiler.start("mage::dispatch::routers_match");
		int i = 0;
		while (!request.isDispatched() && i++ < 100) {
			for (Mage_Core_Controller_Varien_Router_Abstract router : _routers.values()) {
				/** @var $router Mage_Core_Controller_Varien_Router_Abstract */
				if (router.match(request)) {
					break;
				}
			}
		}
		Varien_Profiler.stop("mage::dispatch::routers_match");
		if (i > 100) {
			throw new Mage_Core_Exception("Front controller reached 100 router match iterations");
		}

		// This event gives possibility to launch something before sending output (allow cookie setting)
		EventDispatcher eventDispatcher = app.getEventDispatcher();
		Map<String, Object> eventParams = Collections.singletonMap("front", (Object)this);
		eventDispatcher.dispatchEvent("controller_front_send_response_before", eventParams);
		Varien_Profiler.start("mage::app::dispatch::send_response");
		app.getResponse().sendResponse();
		Varien_Profiler.stop("mage::app::dispatch::send_response");
		eventDispatcher.dispatchEvent("controller_front_send_response_after", eventParams);
	}

	/**
	 * Returns request rewrite instance.
	 * Class name alias is declared in the configuration
	 */
	protected Mage_Core_Model_Url_Rewrite_Request _getRequestRewriteController() {
		String className = app.getConfig().getNode("global/request_rewrite/model").getText();

		return new Mage_Core_Model_Url_Rewrite_Request(getRouters());
		//		
		//	
		//	        return Mage::getSingleton("core/factory")->getModel($className, array(
		//	            "routers" => $this->getRouters(),
		//	        ));
	}

	/**
	 * Auto-redirect to base url (without SID) if the requested url doesn"t match it.
	 * By default this feature is enabled in configuration.
	 *
	 * @param Zend_Controller_Request_Http $request
	 * @throws IOException 
	 */
	protected void _checkBaseUrl(Mage_Core_Controller_Request_Http request) throws IOException {
		if (!app.getConfig().isInstalled() || request.isPost()) {
			return;
		}

		String redirectCodeString = app.getStoreConfigAsString("web/url/redirect_to_base");

		if (StringUtils.isBlank(redirectCodeString)) {
			return;
		}

		int redirectCode = Integer.parseInt(redirectCodeString);
		if (301 != redirectCode) {
			redirectCode = 302;
		}

		if (_isAdminFrontNameMatched(request)) {
			return;
		}

		String baseUrl = app.getStore().getHelper().getBaseUrl(UrlType.WEB, app.getStore().getHelper().isCurrentlySecure());
		if (baseUrl == null) {
			return;
		}

		URI uri;
		try {
			uri = new URI(baseUrl);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		String requestUri = request.getRequestUri();
		if (StringUtils.isEmpty(requestUri)) {
			requestUri = "/";
		}

		if (!StringUtils.equals(uri.getScheme(), request.getScheme())
			|| !StringUtils.equals(uri.getHost(), request.getHttpHost())
			|| (uri.getPath() != null && requestUri.indexOf(uri.getPath()) < 0)) {
			Mage_Core_Controller_Response_Http response = app.getResponse().setRedirect(baseUrl, redirectCode);
			response.sendResponse();
			throw new RedirectAndExitException();
		}
	}

	/**
	 * Check if requested path starts with one of the admin front names
	 */
	protected boolean _isAdminFrontNameMatched(Mage_Core_Controller_Request_Http request) {
		boolean useCustomAdminPath = StringUtils.isNotBlank(app.getConfig().getNode(Mage_Adminhtml_Helper_Data.XML_PATH_USE_CUSTOM_ADMIN_PATH).getText());
		String customAdminPath = app.getConfig().getNode(Mage_Adminhtml_Helper_Data.XML_PATH_CUSTOM_ADMIN_PATH).getText();
		String adminPath = useCustomAdminPath ? customAdminPath : null;

		if (adminPath == null) {
			adminPath = app.getConfig().getNode(Mage_Adminhtml_Helper_Data.XML_PATH_ADMINHTML_ROUTER_FRONTNAME).getText();
		}

		List<String> adminFrontNames = Arrays.asList(adminPath);

		// Check for other modules that can use admin router (a lot of Magento extensions do that)
		List<Simplexml_Element> adminFrontNameNodes = app.getConfig().getNode("admin/routers").selectDescendants("*[not(self::adminhtml) and use = \"admin\"]/args/frontName");

		for (Simplexml_Element frontNameNode : adminFrontNameNodes) {
			adminFrontNames.add(frontNameNode.getText());
		}

		String pathPrefix = Standard.ltrim(request.getPathInfo(), "/");
		int urlDelimiterPos = pathPrefix.indexOf("/");
		if (urlDelimiterPos > 0) {
			pathPrefix = pathPrefix.substring(0, urlDelimiterPos);
		}

		return adminFrontNameNodes.contains(pathPrefix);
	}

	private Mage_Core_Controller_Varien_Router_Abstract createRouter(String className) {
		try {
			return (Mage_Core_Controller_Varien_Router_Abstract)Class.forName("com.naver.mage4j.core.controller.varien.router." + className).newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Fail to create router.(" + className + ")", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Fail to create router.(" + className + ")", e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Fail to create router.(" + className + ")", e);
		}
	}

	public void addRouter(String name, Mage_Core_Controller_Varien_Router_Abstract router) {
		router.setFront(this);
		_routers.put(name, router);
	}

	/**
	 * Retrieve routers collection
	 * @return 
	 *
	 * @return array
	 */
	public Map<String, Mage_Core_Controller_Varien_Router_Abstract> getRouters() {
		return _routers;
	}

	/**
	 * Retrieve router by name
	 * @return 
	 */
	public Mage_Core_Controller_Varien_Router_Abstract getRouter(String name) {
		return _routers.get(name);
	}

	public Mage_Core_Controller_Varien_Router_Abstract getRouterByRoute(String routeName) {
		// empty route supplied - return base url
		Mage_Core_Controller_Varien_Router_Abstract router;
		if (StringUtils.isEmpty(routeName)) {
			router = getRouter("standard");
		} else if (getRouter("admin").getFrontNameByRoute(routeName) != null) {
			router = getRouter("admin");
		} else if (getRouter("standard").getFrontNameByRoute(routeName) != null) {
			// try standard router url assembly
			router = getRouter("standard");
		} else if ((router = getRouter(routeName)) != null) {
			// try custom router url assembly
		} else {
			// get default router url
			router = getRouter("default");
		}

		return router;
	}

	public Mage_Core_Controller_Varien_Router_Abstract getRouterByFrontName(String frontName) {
		Mage_Core_Controller_Varien_Router_Abstract router;
		if (StringUtils.isEmpty(frontName)) {
			router = getRouter("standard");
		} else if (getRouter("admin").getRouteByFrontName(frontName) != null) {
			router = getRouter("admin");
		} else if (getRouter("standard").getRouteByFrontName(frontName) != null) {
			// try standard router url assembly
			router = getRouter("standard");
		} else if ((router = getRouter(frontName)) != null) {
			// try custom router url assembly
		} else {
			// get default router url
			router = getRouter("default");
		}

		return router;
	}

	public Mage_Core_Controller_Varien_Action getAction() {
		return ___action;
	}

	public void setAction(Mage_Core_Controller_Varien_Action action) {
		this.___action = action;
	}

	public boolean getNoRender() {
		return ___noRender;
	}
}
