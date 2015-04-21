package com.naver.mage4j.core.mage.core.controller.varien;

import java.util.ArrayList;
import java.util.List;

import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Url.RouteParams;

/**
 * Controller exception that can fork different actions, cause forward or redirect
 */
public class Mage_Core_Controller_Varien_Exception extends Exception {
	/**
	 * Controller exception that can fork different actions, cause forward or redirect
	 */
	public static final String RESULT_FORWARD = "_forward";

	/**
	 * Controller exception that can fork different actions, cause forward or redirect
	 */
	public static final String RESULT_REDIRECT = "_redirect";

	/**
	 * Controller exception that can fork different actions, cause forward or redirect
	 */
	protected String _resultCallback = null;

	/**
	 * Controller exception that can fork different actions, cause forward or redirect
	 */
	protected CallbackParam _resultCallbackParams;

	/**
	 * Controller exception that can fork different actions, cause forward or redirect
	 */
	protected String _defaultActionName = "noroute";

	/**
	 * Controller exception that can fork different actions, cause forward or redirect
	 */
	protected List<ActionFlag> _flags = new ArrayList<ActionFlag>();

	/**
	 * Prepare data for forwarding action
	 * 
	 * @param actionName
	 * @param controllerName
	 * @param moduleName
	 * @param params
	 * @return Mage_Core_Controller_Varien_Exception
	 */
	public Mage_Core_Controller_Varien_Exception prepareForward(String actionName/* = null */, String controllerName/* = null */, String moduleName/* = null */, RouteParams params/* = Collections.emptyMap() */) {
		this._resultCallback = RESULT_FORWARD;
		if (null == actionName) {
			actionName = this._defaultActionName;
		}

		_resultCallbackParams = new ForwardCallbackParam(actionName, controllerName, moduleName, params);
		return this;
	}

	/**
	 * Prepare data for redirecting
	 * 
	 * @param path
	 * @param arguments
	 * @return Mage_Core_Controller_Varien_Exception
	 */
	public Mage_Core_Controller_Varien_Exception prepareRedirect(String path, RouteParams arguments/* = Collections.emptyMap() */) {
		this._resultCallback = RESULT_REDIRECT;
		this._resultCallbackParams = new RedirectCallbackParam(path, arguments);
		return this;
	}

	/**
	 * Prepare data for running a custom action
	 * 
	 * @param actionName
	 * @return Mage_Core_Controller_Varien_Exception
	 */
	public Mage_Core_Controller_Varien_Exception prepareFork(String actionName/* = null */) {
		if (null == actionName) {
			actionName = this._defaultActionName;
		}

		this._resultCallback = actionName;
		return this;
	}

	/**
	 * Prepare a flag data
	 * 
	 * @param action
	 * @param flag
	 * @param value
	 * @return Mage_Core_Controller_Varien_Exception
	 */
	public Mage_Core_Controller_Varien_Exception prepareFlag(String action, String flag, boolean value) {
		this._flags.add(new ActionFlag(action, flag, value));
		return this;
	}

	public static class ActionFlag {
		String action;
		String flag;
		boolean value;

		public ActionFlag(String action, String flag, boolean value) {
			super();
			this.action = action;
			this.flag = flag;
			this.value = value;
		}

		public String getAction() {
			return action;
		}

		public String getFlag() {
			return flag;
		}

		public boolean getValue() {
			return value;
		}
	}

	/**
	 * Return all set flags
	 * 
	 * @return array
	 */
	public List<ActionFlag> getResultFlags() {
		return this._flags;
	}

	/**
	 * Return results as callback for a controller
	 */
	public String getResultCallbackName() {
		if (null == this._resultCallback) {
			this.prepareFork(null);
		}

		return _resultCallback;
	}

	/**
	 * Return results as callback for a controller
	 */
	public CallbackParam getResultCallbackParams() {
		if (null == this._resultCallback) {
			this.prepareFork(null);
		}

		return _resultCallbackParams;
	}

	public interface CallbackParam {

	}

	public static class ForwardCallbackParam implements CallbackParam {
		String actionName;
		String controllerName;
		String moduleName;
		RouteParams params;

		public ForwardCallbackParam(String actionName, String controllerName, String moduleName, RouteParams params) {
			super();
			this.actionName = actionName;
			this.controllerName = controllerName;
			this.moduleName = moduleName;
			this.params = params;
		}

		public String getActionName() {
			return actionName;
		}

		public String getControllerName() {
			return controllerName;
		}

		public String getModuleName() {
			return moduleName;
		}

		public RouteParams getParams() {
			return params;
		}
	}

	public static class RedirectCallbackParam implements CallbackParam {
		String path;
		RouteParams arguments;

		public RedirectCallbackParam(String path, RouteParams arguments) {
			super();
			this.path = path;
			this.arguments = arguments;
		}

		public String getPath() {
			return path;
		}

		public RouteParams getArguments() {
			return arguments;
		}
	}
}
