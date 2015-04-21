package com.naver.mage4j.core.mage.core.model;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.model.url.Mage_Core_Model_Url_Rewrite;

/**
 * Factory class
 */
public class Mage_Core_Model_Factory {
	/**
	 * Xml path to url rewrite model class alias
	 */
	public static final String XML_PATH_URL_REWRITE_MODEL = "global/url_rewrite/model";

	/**
	 * Xml path to url rewrite model class alias
	 */
	public static final String XML_PATH_INDEX_INDEX_MODEL = "global/index/index_model";

	/**
	 * Config instance
	 */
	protected Mage_Core_Model_Config _config;

	@Autowired
	private ModelLoader modelLoader;

	/**
	 * Initialize factory
	 * 
	 * @param arguments
	 */
	public Mage_Core_Model_Factory(/*Map arguments = Collections.emptyMap() */) {
		//		this._config = ((!(arguments.get("config") == null)) ? (arguments.get("config")) : (AppContext.getCurrent().getConfig()));
		this._config = AppContext.getCurrent().getConfig();
	}

//	/**
//	 * Retrieve model object
//	 * 
//	 * @param modelClass
//	 * @param arguments
//	 * @return bool|Mage_Core_Model_Abstract
//	 */
//	public Mage_Core_Model_Abstract getModel(String modelClass/* = "" */, Map<String, Object> arguments/* = Collections.emptyMap() */) {
//		return modelLoader.getModel(modelClass, arguments);
//	}
//
//	/**
//	 * Retrieve model object singleton
//	 * 
//	 * @param modelClass
//	 * @param arguments
//	 * @return Mage_Core_Model_Abstract
//	 */
//	public Mage_Core_Model_Abstract getSingleton(String modelClass/* = "" */, Map<String, Object> arguments/* = Collections.emptyMap() */) {
//		return modelLoader.getSingleton(modelClass, arguments);
//	}
//
//	/**
//	 * Retrieve object of resource model
//	 * 
//	 * @param modelClass
//	 * @param arguments
//	 * @return Object
//	 */
//	public Object getResourceModel(String modelClass, Map<String, Object> arguments/* = Collections.emptyMap() */) {
//		return modelLoader.getResourceModel(modelClass, arguments);
//	}
//
//	/**
//	 * Retrieve helper instance
//	 * 
//	 * @param helperClass
//	 * @return Mage_Core_Helper_Abstract
//	 */
//	public Mage_Core_Helper_Abstract getHelper(String helperClass) {
//		return Mage.helper(helperClass);
//	}

	/**
	 * Get config instance
	 * 
	 * @return Mage_Core_Model_Config
	 */
	public Mage_Core_Model_Config getConfig() {
		return this._config;
	}

	/**
	 * Retrieve url_rewrite instance
	 * 
	 * @return Mage_Core_Model_Url_Rewrite
	 */
	public Mage_Core_Model_Url_Rewrite getUrlRewriteInstance() {
		return (Mage_Core_Model_Url_Rewrite)modelLoader.getModel(this.getUrlRewriteClassAlias(), Collections.emptyMap());
	}

	/**
	 * Retrieve alias for url_rewrite model
	 * 
	 * @return string
	 */
	public String getUrlRewriteClassAlias() {
		return this._config.getNode(XML_PATH_URL_REWRITE_MODEL).getText();
	}

	/**
	 * @return string
	 */
	public String getIndexClassAlias() {
		return this._config.getNode(XML_PATH_INDEX_INDEX_MODEL).getText();
	}

}