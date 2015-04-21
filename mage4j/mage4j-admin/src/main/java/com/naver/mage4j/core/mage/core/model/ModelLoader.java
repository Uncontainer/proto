package com.naver.mage4j.core.mage.core.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.mage4j.core.mage.MageInstanceLoader;
import com.naver.mage4j.core.mage.core.Mage_Core_Exception;
import com.naver.mage4j.core.mage.core.controller.request.Mage_Core_Controller_Request_Http;
import com.naver.mage4j.core.mage.core.controller.response.Mage_Core_Controller_Response_Http;
import com.naver.mage4j.core.mage.core.controller.varien.Mage_Core_Controller_Varien_Action;
import com.naver.mage4j.core.mage.core.helper.Mage_Core_Helper_Abstract;

@Component
public class ModelLoader implements InitializingBean {
	@Autowired
	private ModelInstanceLoader modelInstanceLoader;

	private static ModelLoader LOADER = null;

	public static ModelLoader get() {
		if (LOADER == null) {
			throw new IllegalStateException();
		}

		return LOADER;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LOADER = this;
	}

	/**
	 * Retrieve model object
	*
	* @link    Mage_Core_Model_Config::getModelInstance
	* @return  Mage_Core_Model_Abstract|false
	*/
	public Object getModel(String modelClass, Map<String, Object> arguments) {
		return modelInstanceLoader.getModelInstance(modelClass, arguments);
	}

	public Mage_Core_Controller_Varien_Action getControllerInstance(String controllerClassName, Mage_Core_Controller_Request_Http request, Mage_Core_Controller_Response_Http response) {
		// TODO [MVC] 지원 여부 확인 후 구현 필요
		throw new UnsupportedOperationException();
	}

	/**
	 * Retrieve model object singleton
	 *
	 * @param   string $modelClass
	 * @param   array $arguments
	 * @return  Mage_Core_Model_Abstract
	 */
	public Object getSingleton(String modelClass, Map<String, Object> arguments) {
		String registryKey = "_singleton/" + modelClass;
		if (registry(registryKey) == null) {
			register(registryKey, getModel(modelClass, arguments));
		}

		return registry(registryKey);
	}

	//	public Object getResourceSingleton(String modelClass) {
	//		return getResourceSingleton(modelClass, Collections.<String, Object> emptyMap());
	//	}

	/**
	 * Retrieve resource vodel object singleton
	 *
	 * @param   string $modelClass
	 * @param   array $arguments
	 * @return  object
	 */
	public Object getResourceSingleton(String modelClass, Map<String, Object> arguments) {
		//		if (arguments == null) {
		//			throw new IllegalArgumentException();
		//		}

		String registryKey = "_resource_singleton/" + modelClass;
		if (registry(registryKey) == null) {
			register(registryKey, getResourceModel(modelClass, arguments));
		}

		return registry(registryKey);
	}

	/**
	 * Retrieve helper object
	 *
	 * @param string $name the helper name
	 * @return Mage_Core_Helper_Abstract
	 */
	public Mage_Core_Helper_Abstract helper(String name) {
		String registryKey = "_helper/" + name;
		Object result = registry(registryKey);
		if (result == null) {
			String helperClass = modelInstanceLoader.getHelperClassName(name);
			result = MageInstanceLoader.get().getInstance(helperClass);
			register(registryKey, result);
		}

		return (Mage_Core_Helper_Abstract)result;
	}

	/**
	 * Retrieve object of resource model
	 *
	 * @param   string $modelClass
	 * @param   array $arguments
	 * @return  Object
	 */
	public Object getResourceModel(String modelClass, Map<String, Object> arguments) {
		return modelInstanceLoader.getResourceModelInstance(modelClass, arguments);
	}

	/**
	 * Registry collection
	 */
	private Map<String, Object> _registry = new ConcurrentHashMap<String, Object>();

	/**
	 * Retrieve a value from registry by a key
	 *
	 * @param string $key
	 * @return mixed
	 */
	private Object registry(String key) {
		return _registry.get(key);
	}

	private void register(String key, Object value) {
		register(key, value, false);
	}

	/**
	 * Register a new variable
	 *
	 * @param string $key
	 * @param mixed $value
	 * @param bool $graceful
	 * @throws Mage_Core_Exception
	 */
	private void register(String key, Object value, boolean graceful) {
		Object savedValue = _registry.get(key);
		if (savedValue != null) {
			if (graceful) {
				return;
			}

			throw new Mage_Core_Exception("Mage registry key '" + key + "' already exists.");
		}

		_registry.put(key, value);
	}
}
