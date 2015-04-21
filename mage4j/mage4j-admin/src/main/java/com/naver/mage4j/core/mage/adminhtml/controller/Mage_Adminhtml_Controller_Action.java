package com.naver.mage4j.core.mage.adminhtml.controller;

import com.naver.mage4j.core.mage.core.controller.varien.Mage_Core_Controller_Varien_Action;

/**
 * Base adminhtml controller
 */
public class Mage_Adminhtml_Controller_Action extends Mage_Core_Controller_Varien_Action {
	/**
	 * Name of "is URLs checked" flag
	 */
	public static final String FLAG_IS_URLS_CHECKED = "check_url_settings";

	/**
	 * Session namespace to refer in other places
	 */
	public static final String SESSION_NAMESPACE = "adminhtml";
}