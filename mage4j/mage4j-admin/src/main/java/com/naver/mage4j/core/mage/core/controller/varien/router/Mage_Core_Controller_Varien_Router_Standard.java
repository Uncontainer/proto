package com.naver.mage4j.core.mage.core.controller.varien.router;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.Mage_Core_Model_App;
import com.naver.mage4j.core.mage.core.RedirectAndExitException;
import com.naver.mage4j.core.mage.core.controller.request.Mage_Core_Controller_Request_Http;
import com.naver.mage4j.core.mage.core.controller.response.Mage_Core_Controller_Response_Http;
import com.naver.mage4j.core.mage.core.controller.varien.Mage_Core_Controller_Varien_Action;
import com.naver.mage4j.core.mage.core.controller.varien.Mage_Core_Controller_Varien_Front;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Url;
import com.naver.mage4j.core.mage.core.model.ModelLoader;
import com.naver.mage4j.core.mage.core.model.resource.store.StoreHelper;
import com.naver.mage4j.core.mage.core.model.resource.store.UrlType;
import com.naver.mage4j.core.mage.core.model.resource.url.UrlRewriteHelper;
import com.naver.mage4j.external.php.Functions;
import com.naver.mage4j.external.php.Standard;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;

public class Mage_Core_Controller_Varien_Router_Standard extends Mage_Core_Controller_Varien_Router_Abstract {
	protected Map<String, List<String>> _modules = new HashMap<String, List<String>>();
	protected Map<String, String> _routes = new HashMap<String, String>();

	@Autowired
	private ModelLoader modelLoader;

	@Autowired
	private Mage_Core_Model_Url coreUrl;

	@Override
	public void collectRoutes(String configArea, String useRouterName) {
		Mage_Core_Model_App app = AppContext.getCurrent();

		Simplexml_Element routersConfigNode = app.getConfig().getNode(configArea + "/routers");
		if (routersConfigNode == null) {
			return;
		}

		List<Simplexml_Element> routerConfigs = routersConfigNode.children();
		for (Simplexml_Element routerConfig : routerConfigs) {
			String routerName = routerConfig.getName();
			String use = routerConfig.getString("use");
			if (useRouterName.equals(use)) {
				List<String> modules = new ArrayList<String>();
				Simplexml_Element args = routerConfig.selectSingle("args");
				modules.add(args.getString("module"));
				for (Simplexml_Element customModule : args.selectSingle("modules").children()) {
					String text = customModule.getText();
					if (StringUtils.isNotBlank(text)) {
						String before = customModule.getAttribute("before");
						if (StringUtils.isNotBlank(before)) {
							int position = modules.indexOf(before);
							if (position < 0) {
								position = 0;
							}
							modules.add(0, text);
						} else {
							String after = customModule.getAttribute("after");
							if (StringUtils.isNotBlank(after)) {
								int position = modules.indexOf(before);
								if (position < 0) {
									modules.add(text);
								} else {
									modules.add(position + 1, text);
								}
							} else {
								modules.add(text);
							}
						}
					}
				}
				String frontName = args.getString("frontName");
				addModule(frontName, modules, routerName);
			}
		}
	}

	public void addModule(String frontName, List<String> moduleNames, String routeName) {
		_modules.put(frontName, moduleNames);
		_routes.put(routeName, frontName);
	}

	public List<String> getModuleByFrontName(String frontName) {
		List<String> modules = _modules.get(frontName);
		if (modules == null) {
			return Collections.emptyList();
		}

		return modules;
	}

	@Override
	public String getFrontNameByRoute(String routeName) {
		return _routes.get(routeName);
	}

	@Override
	public String getRouteByFrontName(String frontName) {
		return _routes.get(frontName);
	}

	/**
	 * Match the request
	 *
	 * @param Zend_Controller_Request_Http $request
	 * @return boolean
	 */
	@Override
	public boolean match(Mage_Core_Controller_Request_Http request) {
		//checking before even try to find out that current module
		//should use this router
		if (!_beforeModuleMatch()) {
			return false;
		}

		fetchDefault();

		Mage_Core_Controller_Varien_Front front = getFront();
		String path = Standard.trim(request.getPathInfo(), "/");

		String[] p;
		if (path != null) {
			p = StringUtils.split(path, "/");
		} else {
			p = StringUtils.split(_getDefaultPath(), "/");
			if (p == null) {
				p = ArrayUtils.EMPTY_STRING_ARRAY;
			}
		}

		// get module name
		String module = request.getModuleName();
		if (module == null) {
			if (p.length > 0) {
				module = p[0];
			} else {
				module = getFront().getDefault("module");
				request.setAlias(UrlRewriteHelper.REWRITE_REQUEST_PATH_ALIAS, "");
			}
		}
		if (module == null) {
			if (AppContext.getCurrent().getStore().getHelper().isAdmin()) {
				module = "admin";
			} else {
				return false;
			}
		}

		/**
		 * Searching router args by module name from route using it as key
		 */
		List<String> modules = getModuleByFrontName(module);

		if (modules.isEmpty()) {
			return false;
		}

		// checks after we found out that this router should be used for current module
		if (!_afterModuleMatch()) {
			return false;
		}

		/**
		 * Going through modules to find appropriate controller
		 */
		String foundRealModule = null;
		String controller = null;
		String action = null;
		Mage_Core_Controller_Varien_Action controllerInstance = null;
		for (String realModule : modules) {
			request.setRouteName(getRouteByFrontName(module));

			// get controller name
			controller = request.getControllerName();
			if (controller == null) {
				if (p.length >= 2) {
					controller = p[1];
				} else {
					controller = front.getDefault("controller");
					request.setAlias(UrlRewriteHelper.REWRITE_REQUEST_PATH_ALIAS, Standard.ltrim(request.getOriginalPathInfo(), "/"));
				}
			}

			// get action name
			action = request.getActionName();
			if (action == null) {
				action = p.length >= 3 ? p[2] : front.getDefault("action");
			}

			//checking if this place should be secure
			_checkShouldBeSecure(request, "/" + module + "/" + controller + "/" + action);

			String controllerClassName = _validateControllerClassName(realModule, controller);
			if (controllerClassName == null) {
				continue;
			}

			// instantiate controller class
			controllerInstance = modelLoader.getControllerInstance(controllerClassName, request, AppContext.getCurrent().getResponse());

			if (!controllerInstance.hasAction(action)) {
				continue;
			}

			foundRealModule = realModule;
			break;
		}

		/**
		 * if we did not found any suitable
		 */
		if (foundRealModule == null) {
			if (_noRouteShouldBeApplied()) {
				controller = "index";
				action = "noroute";

				String controllerClassName = _validateControllerClassName(foundRealModule, controller);
				if (controllerClassName == null) {
					return false;
				}

				// instantiate controller class
				controllerInstance = modelLoader.getControllerInstance(controllerClassName, request, AppContext.getCurrent().getResponse());

				if (!controllerInstance.hasAction(action)) {
					return false;
				}
			} else {
				return false;
			}
		}

		// set values only after all the checks are done
		request.setModuleName(module);
		request.setControllerName(controller);
		request.setActionName(action);
		request.setControllerModule(foundRealModule);

		// set parameters from pathinfo
		for (int i = 3; i < p.length; i += 2) {
			// TODO path에서 왜 parameter를 뽑아내는지 스펙 확인 필요.
			//			request.setParam(p[i], isset(p[i + 1]) ? urldecode(p[i + 1]) : "");
			throw new UnsupportedOperationException();
		}

		// dispatch action
		request.setDispatched(true);
		controllerInstance.dispatch(action);

		return true;
	}

	/**
	 * Allow to control if we need to enable no route functionality in current router
	 */
	protected boolean _noRouteShouldBeApplied() {
		return false;
	}

	/**
	 * Check that request uses https protocol if it should.
	 * Function redirects user to correct URL if needed.
	 *
	 * @param Mage_Core_Controller_Request_Http $request
	 * @param string $path
	 * @return void
	 */
	protected void _checkShouldBeSecure(Mage_Core_Controller_Request_Http request, String path) {
		Mage_Core_Model_App app = AppContext.getCurrent();
		if (!app.getConfig().isInstalled() || request.isPost()) {
			return;
		}

		if (_shouldBeSecure(path) && !request.isSecure()) {
			String url = _getCurrentSecureUrl(request);
			if (!"adminhtml".equals(request.getRouteName()) && app.getUseSessionInUrl()) {
				url = coreUrl.getRedirectUrl(url);
			}

			Mage_Core_Controller_Response_Http response = app.getResponse();
			response.setRedirect(url);
			try {
				response.sendResponse();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			throw new RedirectAndExitException();
		}
	}

	/**
	 * Check whether URL for corresponding path should use https protocol
	 *
	 * @param string $path
	 * @return bool
	 */
	protected boolean _shouldBeSecure(String path) {
		Mage_Core_Model_App app = AppContext.getCurrent();

		return (app.getStoreConfigAsString("web/unsecure/base_url", "")).startsWith("https")
			|| app.getStoreConfigFlag("web/secure/use_in_frontend")
			&& app.getStoreConfigAsString("web/secure/base_url", "").startsWith("https")
			&& app.shouldUrlBeSecure(path);
	}

	protected String _getCurrentSecureUrl(Mage_Core_Controller_Request_Http request) {
		StoreHelper storeHelper = AppContext.getCurrent().getStore().getHelper();
		String alias = request.getAlias(UrlRewriteHelper.REWRITE_REQUEST_PATH_ALIAS);
		String baseUrl = storeHelper.getBaseUrl(UrlType.LINK, true);
		if (alias != null) {
			return baseUrl + Standard.rtrim(alias, "/");
		} else {
			return baseUrl + Standard.rtrim(request.getPathInfo(), "/");
		}
	}

	/**
	 * Generating and validating class file name,
	 * class and if evrything ok do include if needed and return of class name
	 *
	 * @return mixed
	 */
	protected String _validateControllerClassName(String realModule, String controller)
	{
		String controllerFileName = getControllerFileName(realModule, controller);
		if (!validateControllerFileName(controllerFileName)) {
			return null;
		}

		String controllerClassName = getControllerClassName(realModule, controller);
		if (controllerClassName == null) {
			return null;
		}

		// include controller file if needed
		if (!_includeControllerClass(controllerFileName, controllerClassName)) {
			return null;
		}

		return controllerClassName;
	}

	/**
	 * Include the file containing controller class if this class is not defined yet
	 *
	 * @param string $controllerFileName
	 * @param string $controllerClassName
	 * @return bool
	 */
	protected boolean _includeControllerClass(String controllerFileName, String controllerClassName) {
		// TODO 클래스 로딩 방식 자체에 대한 부분 재검토 필요.
		//        if (!class_exists(controllerClassName, false)) {
		//            if (!file_exists(controllerFileName)) {
		//                return false;
		//            }
		//            include $controllerFileName;
		//
		//            if (!class_exists($controllerClassName, false)) {
		//                throw Mage::exception('Mage_Core', Mage::helper('core')->__('Controller file was loaded but class does not exist'));
		//            }
		//        }
		//        
		//        return true;
		throw new UnsupportedOperationException();
	}

	public String getControllerClassName(String realModule, String controller) {
		return realModule + '_' + Functions.uc_words(controller) + "Controller";
	}

	public boolean validateControllerFileName(String fileName) {
		if (fileName == null) {
			return false;
		}

		File file = new File(fileName);
		return file.exists() && file.canRead();
	}

	private String getControllerFileName(String realModule, String controller) {
		String[] parts = StringUtils.split(realModule, "_");
		realModule = StringUtils.join(Standard.array_splice(parts, 0, 2, null), "_");
		String file = AppContext.getCurrent().getConfig().getModuleDir("controllers", realModule);
		if (parts.length > 0) {
			file += "/" + StringUtils.join(parts, "/");
		}

		file += "/" + Functions.uc_words(controller, "/") + "Controller.php";
		return file;
	}

	/**
	 * Get router default request path
	 */
	protected String _getDefaultPath() {
		return AppContext.getCurrent().getStoreConfigAsString("web/default/front");
	}

	public void fetchDefault() {
		Map<String, String> options = new HashMap<String, String>();
		options.put("module", "core");
		options.put("controller", "index");
		options.put("action", "index");
		getFront().setDefault(options);
	}

	/**
	 * checking if this admin if yes then we don't use this router
	 *
	 * @return bool
	 */
	protected boolean _beforeModuleMatch() {
		return !AppContext.getCurrent().getStore().getHelper().isAdmin();
	}

	/**
	 * dummy call to pass through checking
	 *
	 * @return bool
	 */
	protected boolean _afterModuleMatch() {
		return true;
	}
}
