package com.naver.mage4j.core.mage.core.controller.varien;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.core.mage.adminhtml.controller.Mage_Adminhtml_Controller_Action;
import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.Mage_Core_Model_App;
import com.naver.mage4j.core.mage.core.block.Mage_Core_Block_Abstract;
import com.naver.mage4j.core.mage.core.controller.request.Mage_Core_Controller_Request_Http;
import com.naver.mage4j.core.mage.core.controller.response.Mage_Core_Controller_Response_Http;
import com.naver.mage4j.core.mage.core.controller.varien.Mage_Core_Controller_Varien_Exception.ActionFlag;
import com.naver.mage4j.core.mage.core.controller.varien.Mage_Core_Controller_Varien_Exception.CallbackParam;
import com.naver.mage4j.core.mage.core.controller.varien.Mage_Core_Controller_Varien_Exception.ForwardCallbackParam;
import com.naver.mage4j.core.mage.core.controller.varien.Mage_Core_Controller_Varien_Exception.RedirectCallbackParam;
import com.naver.mage4j.core.mage.core.layout.Mage_Core_Model_Layout_Update;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Cookie;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Layout;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Session;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Url.RouteParams;
import com.naver.mage4j.core.mage.core.model.config.Mage_Core_Model_Config_Element;
import com.naver.mage4j.core.mage.core.model.design.Mage_Core_Model_Design_Package;
import com.naver.mage4j.core.mage.core.model.design.ThemeType;
import com.naver.mage4j.core.mage.core.model.resource.store.StoreHelper;
import com.naver.mage4j.core.mage.core.model.resource.store.UrlType;
import com.naver.mage4j.core.mage.core.model.store.Mage_Core_Model_Store_Exception;
import com.naver.mage4j.core.util.ExceptionUtils;
import com.naver.mage4j.external.varien.Varien_Profiler;
import com.naver.mage4j.external.varien.simplexml.SimplexmlUtils;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;
import com.naver.mage4j.php.mage.MageAtomArray;

/**
 * Custom Zend_Controller_Action class (formally)Allows dispatching before and after events for each controller action
 */
public abstract class Mage_Core_Controller_Varien_Action {
	/**
	 * Custom Zend_Controller_Action class (formally)Allows dispatching before and after events for each controller action
	 */
	public static final String FLAG_NO_CHECK_INSTALLATION = "no-install-check";

	/**
	 * Custom Zend_Controller_Action class (formally)Allows dispatching before and after events for each controller action
	 */
	public static final String FLAG_NO_DISPATCH = "no-dispatch";

	/**
	 * Custom Zend_Controller_Action class (formally)Allows dispatching before and after events for each controller action
	 */
	public static final String FLAG_NO_PRE_DISPATCH = "no-preDispatch";

	/**
	 * Custom Zend_Controller_Action class (formally)Allows dispatching before and after events for each controller action
	 */
	public static final String FLAG_NO_POST_DISPATCH = "no-postDispatch";

	/**
	 * Custom Zend_Controller_Action class (formally)Allows dispatching before and after events for each controller action
	 */
	public static final String FLAG_NO_START_SESSION = "no-startSession";

	/**
	 * Custom Zend_Controller_Action class (formally)Allows dispatching before and after events for each controller action
	 */
	public static final String FLAG_NO_DISPATCH_BLOCK_EVENT = "no-beforeGenerateLayoutBlocksDispatch";

	/**
	 * Custom Zend_Controller_Action class (formally)Allows dispatching before and after events for each controller action
	 */
	public static final String FLAG_NO_COOKIES_REDIRECT = "no-cookies-redirect";

	/**
	 * Custom Zend_Controller_Action class (formally)Allows dispatching before and after events for each controller action
	 */
	public static final String PARAM_NAME_SUCCESS_URL = "success_url";

	/**
	 * Custom Zend_Controller_Action class (formally)Allows dispatching before and after events for each controller action
	 */
	public static final String PARAM_NAME_ERROR_URL = "error_url";

	/**
	 * Custom Zend_Controller_Action class (formally)Allows dispatching before and after events for each controller action
	 */
	public static final String PARAM_NAME_REFERER_URL = "referer_url";

	/**
	 * Custom Zend_Controller_Action class (formally)Allows dispatching before and after events for each controller action
	 */
	public static final String PARAM_NAME_BASE64_URL = "r64";

	/**
	 * Custom Zend_Controller_Action class (formally)Allows dispatching before and after events for each controller action
	 */
	public static final String PARAM_NAME_URL_ENCODED = "uenc";

	/**
	 * Custom Zend_Controller_Action class (formally)Allows dispatching before and after events for each controller action
	 */
	public static final String PROFILER_KEY = "mage::dispatch::controller::action";

	/**
	 * Real module name (like 'Mage_Module')
	 */
	protected String _realModuleName;

	/**
	 * Action flagsfor example used to disable rendering default layout
	 */
	protected Map<String, Map<String, Boolean>> _flags = new HashMap<String, Map<String, Boolean>>();

	/**
	 * Action list where need check enabled cookie
	 */
	protected List<String> _cookieCheckActions = Collections.emptyList();

	/**
	 * Currently used area
	 */
	protected String _currentArea;

	/**
	 * Namespace for session.Should be defined for proper working session.
	 */
	protected String _sessionNamespace;

	/**
	 * Whether layout is loaded
	 */
	protected boolean _isLayoutLoaded = false;

	/**
	 * Title parts to be rendered in the page head title
	 */
	protected List<String> _titles = new ArrayList<String>();

	/**
	 * Whether the default title should be removed
	 */
	protected boolean _removeDefaultTitle = false;

	/**
	 * Constructor
	 * 
	 * @param request
	 * @param response
	 * @param invokeArgs
	 */
	public Mage_Core_Controller_Varien_Action(/*array invokeArgs = Collections.emptyMap() */) {
		AppContext.getCurrent().getFrontController().setAction(this);
	}

	/**
	 * Constructor
	 * 
	 * @param request
	 * @param response
	 * @param invokeArgs
	 */
	public boolean hasAction(String action) {
		return method_exists(this.getClass(), this.getActionMethodName(action));
	}

	private boolean method_exists(Class<?> clazz, String methodName) {
		// TODO 캐싱 이후 사용
		try {
			this.getClass().getMethod(methodName);
			return true;
		} catch (NoSuchMethodException e) {
			return false;
		}
	}

	/**
	 * Retrieve request object
	 * 
	 * @return Mage_Core_Controller_Request_Http
	 */
	public Mage_Core_Controller_Request_Http getRequest() {
		return AppContext.getCurrent().getRequest();
	}

	/**
	 * Retrieve response object
	 * 
	 * @return Mage_Core_Controller_Response_Http
	 */
	public Mage_Core_Controller_Response_Http getResponse() {
		return AppContext.getCurrent().getResponse();
	}

	/**
	 * Retrieve flag value
	 * 
	 * @param action
	 * @param flag
	 * @return bool
	 */
	public boolean getFlag(String action, String flag/* = "" */) {
		if (StringUtils.isEmpty(action)) {
			action = this.getRequest().getActionName();
		}

		if (StringUtils.isEmpty(flag)) {
			throw new IllegalArgumentException("Empty flag. Call getFlagAll() method to get all flags.");
		}

		if (this._flags.get(action).get(flag) != null) {
			return this._flags.get(action).get(flag);
		} else {
			return false;
		}
	}

	public Map<String, Map<String, Boolean>> getFlagAll() {
		return this._flags;
	}

	/**
	 * Setting flag value
	 * 
	 * @param action
	 * @param flag
	 * @param value
	 * @return Mage_Core_Controller_Varien_Action
	 */
	public Mage_Core_Controller_Varien_Action setFlag(String action, String flag, boolean value) {
		if (StringUtils.isEmpty(action)) {
			action = getRequest().getActionName();
		}

		Map<String, Boolean> actionFlagMap = this._flags.get(action);
		if (actionFlagMap == null) {
			actionFlagMap = new HashMap<String, Boolean>();
			_flags.put(action, actionFlagMap);
		}

		actionFlagMap.put(flag, value);

		return this;
	}

	/**
	 * Retrieve full bane of current action current controller andcurrent module
	 * 
	 * @param delimiter
	 * @return string
	 */
	public String getFullActionName(String delimiter/* = "_" */) {
		if (delimiter == null) {
			delimiter = "_";
		}

		Mage_Core_Controller_Request_Http request = getRequest();

		return request.getRequestedRouteName() + delimiter +
			request.getRequestedControllerName() + delimiter +
			request.getRequestedActionName();
	}

	/**
	 * Retrieve current layout object
	 * 
	 * @return Mage_Core_Model_Layout
	 */
	public Mage_Core_Model_Layout getLayout() {
		return AppContext.getCurrent().getLayout();
	}

	/**
	 * Load layout by handles(s)
	 * 
	 * @param handles
	 * @param generateBlocks
	 * @param generateXml
	 * @return Mage_Core_Controller_Varien_Action
	 */
	public Mage_Core_Controller_Varien_Action loadLayout(List<String> handles/* = null */, boolean generateBlocks/* = true */, boolean generateXml/* = true */) {
		//		if (!"".equals(handles)) {
		//			getLayout().getUpdate().addHandle(handles != null ? handles : "default");
		//		}
		if (handles.isEmpty()) {
			getLayout().getUpdate().addHandle("default");
		} else {
			getLayout().getUpdate().addHandles(handles);
		}

		this.addActionLayoutHandles();
		this.loadLayoutUpdates();
		if (!(generateXml)) {
			return this;
		}

		this.generateLayoutXml();
		if (!(generateBlocks)) {
			return this;
		}

		this.generateLayoutBlocks();
		this._isLayoutLoaded = true;
		return this;
	}

	/**
	 * Load layout by handles(s)
	 * 
	 * @param handles
	 * @param generateBlocks
	 * @param generateXml
	 * @return Mage_Core_Controller_Varien_Action
	 */
	public Mage_Core_Controller_Varien_Action addActionLayoutHandles() {
		Mage_Core_Model_Design_Package _package = AppContext.getCurrent().getDesignPackage();
		Mage_Core_Model_Layout_Update update = this.getLayout().getUpdate();
		update.addHandle("STORE_" + AppContext.getCurrent().getStore().getCode());
		update.addHandle("THEME_" + _package.getArea() + "_" + _package.getPackageName() + "_" + _package.getTheme(ThemeType.LAYOUT));
		update.addHandle(this.getFullActionName("_").toLowerCase());
		return this;
	}

	/**
	 * Load layout by handles(s)
	 * 
	 * @param handles
	 * @param generateBlocks
	 * @param generateXml
	 * @return Mage_Core_Controller_Varien_Action
	 */
	public Mage_Core_Controller_Varien_Action loadLayoutUpdates() {
		String _profilerKey = (PROFILER_KEY + "::" + this.getFullActionName("_"));
		AppContext.getCurrent().getEventDispatcher().dispatchEvent("controller_action_layout_load_before", MageAtomArray.createMap(new Object[] {"action", this}, new Object[] {"layout", this.getLayout()}));
		Varien_Profiler.start(_profilerKey + "::layout_load");
		this.getLayout().getUpdate().load(null);
		Varien_Profiler.stop(_profilerKey + "::layout_load");
		return this;
	}

	/**
	 * Load layout by handles(s)
	 * 
	 * @param handles
	 * @param generateBlocks
	 * @param generateXml
	 * @return Mage_Core_Controller_Varien_Action
	 */
	public Mage_Core_Controller_Varien_Action generateLayoutXml() {
		String _profilerKey;
		_profilerKey = PROFILER_KEY + "::" + this.getFullActionName("_");
		if (!(this.getFlag("", FLAG_NO_DISPATCH_BLOCK_EVENT))) {
			AppContext.getCurrent().getEventDispatcher().dispatchEvent("controller_action_layout_generate_xml_before", MageAtomArray.createMap(new Object[] {"action", this}, new Object[] {"layout", this.getLayout()}));
		}

		Varien_Profiler.start(_profilerKey + "::layout_generate_xml");
		this.getLayout().generateXml();
		Varien_Profiler.stop(_profilerKey + "::layout_generate_xml");
		return this;
	}

	/**
	 * Load layout by handles(s)
	 * 
	 * @param handles
	 * @param generateBlocks
	 * @param generateXml
	 * @return Mage_Core_Controller_Varien_Action
	 */
	public Mage_Core_Controller_Varien_Action generateLayoutBlocks() {
		String _profilerKey = PROFILER_KEY + "::" + this.getFullActionName("_");
		if (!(this.getFlag("", FLAG_NO_DISPATCH_BLOCK_EVENT))) {
			AppContext.getCurrent().getEventDispatcher().dispatchEvent("controller_action_layout_generate_blocks_before", MageAtomArray.createMap(new Object[] {"action", this}, new Object[] {"layout", this.getLayout()}));
		}

		Varien_Profiler.start(_profilerKey + "::layout_generate_blocks");
		this.getLayout().generateBlocks(null);
		Varien_Profiler.stop(_profilerKey + "::layout_generate_blocks");
		if (!(this.getFlag("", FLAG_NO_DISPATCH_BLOCK_EVENT))) {
			AppContext.getCurrent().getEventDispatcher().dispatchEvent("controller_action_layout_generate_blocks_after", MageAtomArray.createMap(new Object[] {"action", this}, new Object[] {"layout", this.getLayout()}));
		}

		return this;
	}

	/**
	 * Rendering layout
	 * 
	 * @param output
	 * @return Mage_Core_Controller_Varien_Action
	 */
	public Mage_Core_Controller_Varien_Action renderLayout(String output/* = "" */) {
		String _profilerKey = (PROFILER_KEY + "::" + this.getFullActionName("_"));
		if (this.getFlag("", "no-renderLayout")) {
			return this;
		}

		Mage_Core_Model_App app = AppContext.getCurrent();
		if (app.getFrontController().getNoRender()) {
			return this;
		}

		this._renderTitles();
		Varien_Profiler.start(_profilerKey + "layout_render");
		if ("" != output) {
			this.getLayout().addOutputBlock(output, "toHtml");
		}

		app.getEventDispatcher().dispatchEvent("controller_action_layout_render_before", null);
		app.getEventDispatcher().dispatchEvent("controller_action_layout_render_before_" + this.getFullActionName("_"), null);
		this.getLayout().setDirectOutput(false);
		output = app.getTranslateInline().processResponseBody(this.getLayout().getOutput());
		this.getResponse().appendBody(output);
		Varien_Profiler.stop(_profilerKey + "::layout_render");
		return this;
	}

	/**
	 * Rendering layout
	 * 
	 * @param output
	 * @return Mage_Core_Controller_Varien_Action
	 */
	public void dispatch(String action) {
		try {
			String actionMethodName = this.getActionMethodName(action);
			if (!method_exists(getClass(), actionMethodName)) {
				actionMethodName = "norouteAction";
			}

			Varien_Profiler.start(PROFILER_KEY + "::predispatch");
			this.preDispatch();
			Varien_Profiler.stop(PROFILER_KEY + "::predispatch");
			if (this.getRequest().isDispatched()) {
				if (!(this.getFlag("", FLAG_NO_DISPATCH))) {
					String _profilerKey = (PROFILER_KEY + "::" + this.getFullActionName("_"));
					Varien_Profiler.start(_profilerKey);
					try {
						getClass().getMethod(actionMethodName).invoke(this);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
						throw new RuntimeException(e);
					}
					Varien_Profiler.stop(_profilerKey);
					Varien_Profiler.start(PROFILER_KEY + "::postdispatch");
					this.postDispatch();
					Varien_Profiler.stop(PROFILER_KEY + "::postdispatch");
				}
			}
		} catch (Throwable t) {
			while (t instanceof InvocationTargetException) {
				t = t.getCause();
			}

			Mage_Core_Controller_Varien_Exception exception;
			if (t instanceof Mage_Core_Controller_Varien_Exception) {
				exception = (Mage_Core_Controller_Varien_Exception)t;
			} else {
				ExceptionUtils.launderThrowable(t);
				return;
			}

			for (ActionFlag flagData : exception.getResultFlags()) {
				this.setFlag(flagData.getAction(), flagData.getFlag(), flagData.getValue());
			}

			String method = exception.getResultCallbackName();
			CallbackParam parameters = exception.getResultCallbackParams();
			switch (method) {
				case Mage_Core_Controller_Varien_Exception.RESULT_REDIRECT: {
					RedirectCallbackParam r = (RedirectCallbackParam)parameters;
					this._redirect(r.getPath(), r.getArguments());
					break;
				}
				case Mage_Core_Controller_Varien_Exception.RESULT_FORWARD: {
					ForwardCallbackParam f = (ForwardCallbackParam)parameters;
					this._forward(f.getActionName(), f.getControllerName(), f.getModuleName(), f.getParams());
					break;
				}
				default: {
					String actionMethodName = this.getActionMethodName(method);
					this.getRequest().setActionName(method);
					try {
						getClass().getMethod(actionMethodName, String.class).invoke(this, method);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
						throw new RuntimeException(e1);
					}
					break;
				}
			}
		}
	}

	/**
	 * Retrieve action method name
	 * 
	 * @param action
	 * @return string
	 */
	public String getActionMethodName(String action) {
		return action + "Action";
	}

	/**
	 * Dispatch event before action
	 * 
	 * @return void
	 */
	public void preDispatch() {
		Mage_Core_Model_App app = AppContext.getCurrent();
		boolean installed = app.getConfig().isInstalled();
		if (!(this.getFlag("", FLAG_NO_CHECK_INSTALLATION))) {
			if (!(installed)) {
				this.setFlag("", FLAG_NO_DISPATCH, true);
				this._redirect("install", null);
				return;
			}

		}

		if (installed && (!(app.getStore().getIsActive()))) {
			throw new Mage_Core_Model_Store_Exception();
		}

		if (this._rewrite()) {
			return;
		}

		if (!this.getFlag("", FLAG_NO_START_SESSION)) {
			boolean checkCookie = this._cookieCheckActions.contains(this.getRequest().getActionName()) && StringUtils.isNotEmpty(this.getRequest().getParam("nocookie"));
			Mage_Core_Model_Cookie cookies = app.getCookie();
			Mage_Core_Model_Session session = (Mage_Core_Model_Session)app.getSession(Collections.singletonMap("name", this._sessionNamespace)).start(null);
			if (cookies == null) {
				if (session.getCookieShouldBeReceived()) {
					this.setFlag("", FLAG_NO_COOKIES_REDIRECT, true);
					session.unsCookieShouldBeReceived();
					session.setSkipSessionIdFlag(true);
				} else if (checkCookie) {
					if (this.getRequest().getRequestParam(session.getSessionIdQueryParam()) != null
						&& app.getUseSessionInUrl()
						&& !StringUtils.equals(this._sessionNamespace, Mage_Adminhtml_Controller_Action.SESSION_NAMESPACE)) {
						session.setCookieShouldBeReceived(true);
					} else {
						this.setFlag("", FLAG_NO_COOKIES_REDIRECT, true);
					}
				}
			}
		}

		app.loadArea(this.getLayout().getArea());
		if (this.getFlag("", FLAG_NO_COOKIES_REDIRECT)
			&& app.getStoreConfigAsBoolean("web/browser_capabilities/cookies")) {
			this._forward("noCookies", "index", "core", null);
			return;
		}

		if (this.getFlag("", FLAG_NO_PRE_DISPATCH)) {
			return;
		}

		//		Varien_Autoload.registerScope(this.getRequest().getRouteName());
		Map<String, Object> eventArgs = Collections.singletonMap("controller_action", this);
		app.getEventDispatcher().dispatchEvent("controller_action_predispatch", eventArgs);
		app.getEventDispatcher().dispatchEvent("controller_action_predispatch_" + this.getRequest().getRouteName(), eventArgs);
		app.getEventDispatcher().dispatchEvent("controller_action_predispatch_" + this.getFullActionName("_"), eventArgs);
	}

	/**
	 * Dispatches event after action
	 * 
	 */
	public void postDispatch() {
		if (this.getFlag("", FLAG_NO_POST_DISPATCH)) {
			return;
		}

		AppContext.getCurrent().getEventDispatcher().dispatchEvent("controller_action_postdispatch_" + this.getFullActionName("_"), Collections.singletonMap("controller_action", this));
		AppContext.getCurrent().getEventDispatcher().dispatchEvent("controller_action_postdispatch_" + this.getRequest().getRouteName(), Collections.singletonMap("controller_action", this));
		AppContext.getCurrent().getEventDispatcher().dispatchEvent("controller_action_postdispatch", Collections.singletonMap("controller_action", this));
	}

	/**
	 * Dispatches event after action
	 * 
	 */
	//	public void norouteAction(Object coreRoute/* = null */) {
	//		NoType status = ((this.getRequest().getParam("__status__")) ? (this.getRequest().getParam("__status__")) : (new Varien_Object()));
	//		AppContext.getCurrent().getEventDispatcher().dispatchEvent("controller_action_noroute", MageAtomArray.createMap(new Object[] {"action", this}, new Object[] {"status", status}));
	//		if ((status.getLoaded() != true) || (status.getForwarded() == true) || (!(coreRoute == null))) {
	//			this.loadLayout(Arrays.asList("default", "noRoute"), true, true);
	//			this.renderLayout("");
	//		} else {
	//			status.setForwarded(true);
	//			this._forward(status.getForwardAction(), status.getForwardController(), status.getForwardModule(), Collections.singletonMap("__status__", status));
	//		}
	//	}

	/**
	 * Dispatches event after action
	 * 
	 */
	public void noCookiesAction() {
		RedirectAction redirect = new RedirectAction();
		AppContext.getCurrent().getEventDispatcher().dispatchEvent("controller_action_nocookies", MageAtomArray.createMap(new Object[] {"action", this}, new Object[] {"redirect", redirect}));
		if (redirect.getRedirectUrl() != null) {
			this._redirectUrl(redirect.getRedirectUrl());
		} else if (redirect.getRedirect()) {
			this._redirect(redirect.getPath(), redirect.getArguments());
		} else {
			this.loadLayout(Arrays.asList("default", "noCookie"), true, true);
			this.renderLayout("");
		}

		this.getRequest().setDispatched(true);
	}

	/**
	 * Throw control to different action (control and module if was specified).
	 * 
	 * @param action
	 * @param controller
	 * @param module
	 * @param params
	 */
	protected void _forward(String action, String controller/* = null */, String module/* = null */, RouteParams params/* = null */) {
		Mage_Core_Controller_Request_Http request = this.getRequest();
		request.initForward();
		if (params != null) {
			request.setParams(params);
		}

		if (controller != null) {
			request.setControllerName(controller);
			if (module != null) {
				request.setModuleName(module);
			}

		}

		request.setActionName(action).setDispatched(false);
	}

	/**
	 * Initializing layout messages by message storage(s), loading and adding messages to layout messages block
	 * 
	 * @param messagesStorage
	 * @return Mage_Core_Controller_Varien_Action
	 */
	//	protected Mage_Core_Controller_Varien_Action _initLayoutMessages(List<String> messagesStorage) {
	//		for (String storageName : messagesStorage) {
	//			NoType storage = Mage.getSingleton(storageName);
	//			if (storage) {
	//				Mage_Core_Block_Messages block = this.getLayout().getMessagesBlock();
	//				block.addMessages(storage.getMessages(true));
	//				block.setEscapeMessageFlag(storage.getEscapeMessages(true));
	//				block.addStorageType(storageName);
	//			} else {
	//				String message = AppContext.getCurrent().getHelperData().__("Invalid messages storage \"%s\" for layout messages initialization", storageName);
	//				throw new Mage_Core_Exception(message);
	//			}
	//		}
	//
	//		return this;
	//	}

	/**
	 * Initializing layout messages by message storage(s), loading and adding messages to layout messages block
	 * 
	 * @param messagesStorage
	 * @return Mage_Core_Controller_Varien_Action
	 */
	//	public Mage_Core_Controller_Varien_Action initLayoutMessages(String messagesStorage) {
	//		return this._initLayoutMessages(Arrays.asList(messagesStorage));
	//	}

	/**
	 * Set redirect url into response
	 * 
	 * @param url
	 * @return Mage_Core_Controller_Varien_Action
	 */
	protected Mage_Core_Controller_Varien_Action _redirectUrl(String url) {
		this.getResponse().setRedirect(url, 302);
		return this;
	}

	/**
	 * Set redirect into response
	 * 
	 * @param path
	 * @param arguments
	 * @return Mage_Core_Controller_Varien_Action
	 */
	protected Mage_Core_Controller_Varien_Action _redirect(String path, RouteParams arguments/* = Collections.emptyMap() */) {
		return this.setRedirectWithCookieCheck(path, arguments);
	}

	/**
	 * Set redirect into response with session id in URL if it is enabled.It allows to distinguish primordial request from browser with cookies disabled.
	 * 
	 * @param path
	 * @param arguments
	 * @return Mage_Core_Controller_Varien_Action
	 */
	public Mage_Core_Controller_Varien_Action setRedirectWithCookieCheck(String path, RouteParams arguments/* = Collections.emptyMap() */) {
		Mage_Core_Model_Session session = AppContext.getCurrent().getSession(Collections.singletonMap("name", this._sessionNamespace));
		if (session.getCookieShouldBeReceived()
			&& AppContext.getCurrent().getUseSessionInUrl()
			&& !StringUtils.equals(this._sessionNamespace, Mage_Adminhtml_Controller_Action.SESSION_NAMESPACE)) {
			arguments.put("_query", Collections.singletonMap(session.getSessionIdQueryParam(), session.getSessionId()));
		}

		String redirectUrl = AppContext.getCurrent().getUrl().getUrl(path, arguments);
		this.getResponse().setRedirect(redirectUrl, 302);

		return this;
	}

	/**
	 * Redirect to success page
	 * 
	 * @param defaultUrl
	 * @return Mage_Core_Controller_Varien_Action
	 */
	protected Mage_Core_Controller_Varien_Action _redirectSuccess(String defaultUrl) {
		String successUrl;
		successUrl = this.getRequest().getParam(PARAM_NAME_SUCCESS_URL);
		if (successUrl == null) {
			successUrl = defaultUrl;
		}

		if (!(this._isUrlInternal(successUrl))) {
			successUrl = AppContext.getCurrent().getStore().getHelper().getBaseUrl();
		}

		this.getResponse().setRedirect(successUrl, 302);
		return this;
	}

	/**
	 * Redirect to error page
	 * 
	 * @param defaultUrl
	 * @return Mage_Core_Controller_Varien_Action
	 */
	protected Mage_Core_Controller_Varien_Action _redirectError(String defaultUrl) {
		String errorUrl;
		errorUrl = this.getRequest().getParam(PARAM_NAME_ERROR_URL);
		if (errorUrl == null) {
			errorUrl = defaultUrl;
		}

		if (!(this._isUrlInternal(errorUrl))) {
			errorUrl = AppContext.getCurrent().getStore().getHelper().getBaseUrl();
		}

		this.getResponse().setRedirect(errorUrl, 302);
		return this;
	}

	/**
	 * Set referer url for redirect in response
	 * 
	 * @param defaultUrl
	 * @return Mage_Core_Controller_Varien_Action
	 */
	protected Mage_Core_Controller_Varien_Action _redirectReferer(String defaultUrl/* = null */) {
		String refererUrl;
		refererUrl = this._getRefererUrl();
		if (refererUrl == null) {
			refererUrl = StringUtils.isEmpty(defaultUrl) ? AppContext.getCurrent().getStore().getHelper().getBaseUrl() : defaultUrl;
		}

		this.getResponse().setRedirect(refererUrl, 302);
		return this;
	}

	/**
	 * Identify referer url via all accepted methods (HTTP_REFERER, regular or base64-encoded request param)
	 * 
	 * @return string
	 */
	protected String _getRefererUrl() {
		String url;
		String refererUrl = this.getRequest().getReferer();
		if ((url = this.getRequest().getParam(PARAM_NAME_REFERER_URL)) != null) {
			refererUrl = url;
		}

		if ((url = this.getRequest().getParam(PARAM_NAME_BASE64_URL)) != null) {
			refererUrl = AppContext.getCurrent().getHelperData().urlDecode(url);
		}

		if ((url = this.getRequest().getParam(PARAM_NAME_URL_ENCODED)) != null) {
			refererUrl = AppContext.getCurrent().getHelperData().urlDecode(url);
		}

		if (!(this._isUrlInternal(refererUrl))) {
			refererUrl = AppContext.getCurrent().getStore().getHelper().getBaseUrl();
		}

		return refererUrl;
	}

	/**
	 * Check url to be used as internal
	 * 
	 * @param url
	 * @return bool
	 */
	protected boolean _isUrlInternal(String url) {
		if (url.contains("http")) {
			StoreHelper storeHelper = AppContext.getCurrent().getStore().getHelper();
			if ((url.indexOf(storeHelper.getBaseUrl()) == 0) || (url.indexOf(storeHelper.getBaseUrl(UrlType.LINK, true)) == 0)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Get real module name (like 'Mage_Module')
	 * 
	 * @return string
	 */
	//	protected String _getRealModuleName() {
	//		String _class;
	//		if (StringUtils.isEmpty(this._realModuleName)) {
	//			_class = get_class(this);
	//			this._realModuleName = _class.substring(0, _class.toLowerCase().indexOf("_" + this.getRequest().getControllerName() + "Controller".toLowerCase()));
	//		}
	//
	//		return this._realModuleName;
	//	}

	/**
	 * Support for controllers rewritesExample of configuration:<global><routers><core_module><rewrite><core_controller><to>new_route/new_controller</to><override_actions>true</override_actions><actions><core_action><to>new_module/new_controller/new_action</core_action></actions><core_controller></rewrite></core_module></routers></global>This will override:1. core_module/core_controller/core_action to new_module/new_controller/new_action2. all other actions of core_module/core_controller to new_module/new_controller
	 * 
	 * @return boolean  true if rewrite happened
	 */
	protected boolean _rewrite() {
		String route = this.getRequest().getRouteName();
		String controller = this.getRequest().getControllerName();
		String action = this.getRequest().getActionName();
		Mage_Core_Model_Config_Element rewrite = (Mage_Core_Model_Config_Element)AppContext.getCurrent().getConfig().getNode("global/routers/" + route + "/rewrite/" + controller);
		if (SimplexmlUtils.isNull(rewrite)) {
			return false;
		}

		String[] t;
		Simplexml_Element rewriteAction = rewrite.selectSingle("actions").selectSingle("action");
		if (SimplexmlUtils.isNull(rewriteAction) || rewrite.is("override_actions")) {
			t = rewrite.getString("to").split("/");
			if ((t.length != 2) || (t[0] == null) || (t[1] == null)) {
				return false;
			}
			t = Arrays.copyOf(t, 3);
			t[2] = action;
		} else {
			t = rewriteAction.getString("to").split("/");
			if ((t.length != 3) || (t[0] == null) || (t[1] == null) || (t[2] == null)) {
				return false;
			}

		}

		this._forward("*".equals(t[2]) ? action : t[2], "*".equals(t[1]) ? controller : t[1], "*".equals(t[0]) ? route : t[0], null);
		return true;
	}

	/**
	 * Validate Form Key
	 * 
	 * @return bool
	 */
	protected boolean _validateFormKey() {
		String formKey;
		if ((formKey = this.getRequest().getParam("form_key")) == null
			|| (formKey != AppContext.getCurrent().getSession(null).getFormKey())) {
			return false;
		}

		return true;
	}

	/**
	 * Add an extra title to the end or one from the end, or remove allUsage examples:$this->_title('foo')->_title('bar');=> bar / foo / <default title>$this->_title()->_title('foo')->_title('bar');=> bar / foo$this->_title('foo')->_title(false)->_title('bar');bar / <default title>
	 * 
	 * @param text
	 * @param resetIfExists
	 * @return Mage_Core_Controller_Varien_Action
	 */
	//	protected Mage_Core_Controller_Varien_Action _title(String text/* = null */, boolean resetIfExists/* = true */) {
	//		if (is_string(text)) {
	//			this._titles = text;
	//		} else if ((-1) == text) {
	//			if (this._titles == null) {
	//				this._removeDefaultTitle = true;
	//			} else {
	//				array_pop(this._titles);
	//			}
	//
	//		} else if ((this._titles == null) || resetIfExists) {
	//			if (false == text) {
	//				this._removeDefaultTitle = false;
	//				this._titles = Collections.emptyMap();
	//			} else if (null == text) {
	//				this._removeDefaultTitle = true;
	//				this._titles = Collections.emptyMap();
	//			}
	//
	//		}
	//
	//		return this;
	//	}

	/**
	 * Prepare titles in the 'head' layout blockSupposed to work only in actions where layout is renderedFalls back to the default logic if there are no titles eventually
	 * 
	 */
	protected void _renderTitles() {
		Mage_Core_Block_Abstract titleBlock;
		if (this._isLayoutLoaded && !this._titles.isEmpty()) {
			titleBlock = this.getLayout().getBlock("head");
			if (titleBlock != null) {
				if (!(this._removeDefaultTitle)) {
					String title = StringUtils.trim(titleBlock.getTitle());
					if (title != null) {
						this._titles.add(title);
					}
				}

				Collections.reverse(this._titles);
				titleBlock.setTitle(StringUtils.join(this._titles, " / "));
			}
		}
	}

	/**
	 * Convert dates in array from localized to internal format
	 * 
	 * @param array
	 * @param dateFields
	 * @return array
	 */
	//	protected Map _filterDates(Map array, List<String> dateFields) {
	//		if (dateFields == null) {
	//			return array;
	//		}
	//
	//		Zend_Filter_LocalizedToNormalized filterInput = (new Zend_Filter_LocalizedToNormalized(Collections.singletonMap("date_format", AppContext.getCurrent().getLocale().getDateFormat(Mage_Core_Model_Locale.FORMAT_TYPE_SHORT))));
	//		Zend_Filter_NormalizedToLocalized filterInternal = (new Zend_Filter_NormalizedToLocalized(Collections.singletonMap("date_format", Varien_Date.DATE_INTERNAL_FORMAT)));
	//		for (String dateField : dateFields) {
	//			if (array.containsKey(dateField) && (!(dateField == null))) {
	//				array.dateField = filterInput.filter(array.dateField);
	//				array.dateField = filterInternal.filter(array.dateField);
	//			}
	//
	//		}
	//
	//		return array;
	//	}

	/**
	 * Convert dates with time in array from localized to internal format
	 * 
	 * @param array
	 * @param dateFields
	 * @return array
	 */
	//	protected Map _filterDateTime(Map array, List<String> dateFields) {
	//		if (dateFields == null) {
	//			return array;
	//		}
	//
	//		Zend_Filter_LocalizedToNormalized filterInput = (new Zend_Filter_LocalizedToNormalized(Collections.singletonMap("date_format", AppContext.getCurrent().getLocale().getDateTimeFormat(Mage_Core_Model_Locale.FORMAT_TYPE_SHORT))));
	//		Zend_Filter_NormalizedToLocalized filterInternal = (new Zend_Filter_NormalizedToLocalized(Collections.singletonMap("date_format", Varien_Date.DATETIME_INTERNAL_FORMAT)));
	//		for (String dateField : dateFields) {
	//			if (array.containsKey(dateField) && (!(dateField == null))) {
	//				array.dateField = filterInput.filter(array.dateField);
	//				array.dateField = filterInternal.filter(array.dateField);
	//			}
	//
	//		}
	//
	//		return array;
	//	}

	/**
	 * Declare headers and content file in response for file download
	 * 
	 * @param fileName
	 * @param content case 
	 * @param contentType
	 * @param contentLength applicable 
	 * @return Mage_Core_Controller_Varien_Action
	 */
	//	protected Mage_Core_Controller_Varien_Action _prepareDownloadResponse(String fileName, String content, String contentType/* = "application/octet-stream" */, Integer contentLength/* = null */){
	//		Varien_Io_File ioAdapter;
	//		String file;
	//		boolean isFile;
	//		NoType session;
	//		boolean buffer;
	//		session = Mage.getSingleton("admin/session");
	//		if (session.isFirstPageAfterLogin()) {
	//			this._redirect(session.getUser().getStartupPageUrl(), Collections.emptyMap());
	//			return this;
	//		}
	//
	//		isFile = false;
	//		file = null;
	//		if (is_array(content)) {
	//			if ((!(content."type" != null)) || (!(content."value" != null))) {
	//				return this;
	//			}
	//
	//			if (content."type" == "filename") {
	//				isFile = true;
	//				file = content."value";
	//				contentLength = filesize(file);
	//			}
	//
	//		}
	//
	//		this.getResponse()
	//		.setHttpResponseCode(200)
	//		.setHeader("Pragma", "public", true)
	//		.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0", true)
	//		.setHeader("Content-type", contentType, true)
	//		.setHeader("Content-Length", Integer.toString(contentLength == null ? content.length() : contentLength), true)
	//		.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"", true)
	//		.setHeader("Last-Modified", date("r"), true);
	//		if (content != null) {
	//			if (isFile) {
	//				this.getResponse().clearBody();
	//				this.getResponse().sendHeaders();
	//				ioAdapter = (new Varien_Io_File());
	//				ioAdapter.open(Collections.singletonMap("path", ioAdapter.dirname(file)));
	//				ioAdapter.streamOpen(file, "r", 0666);
	//				while(buffer = ioAdapter.streamRead(1024)) {
	//					/* [NOT-IMPLEMENT] PhpExpressionPrint */;}
	//
	//				ioAdapter.streamClose();
	//				if (!(StringUtils.isEmpty(content."rm"))) {
	//					ioAdapter.rm(file);
	//				}
	//
	//				exit(0);
	//			} else {
	//				this.getResponse().setBody(content);
	//			}
	//
	//		}
	//
	//		return this;
	//	}
}