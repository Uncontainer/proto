package com.naver.mage4j.core.mage.core.controller.varien.router;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.naver.mage4j.core.mage.adminhtml.Mage_Adminhtml_Helper_Data;
import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.Mage_Core_Model_App;
import com.naver.mage4j.core.mage.core.RedirectAndExitException;
import com.naver.mage4j.core.mage.core.controller.response.Mage_Core_Controller_Response_Http;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Config;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Url;
import com.naver.mage4j.core.mage.core.model.resource.store.StoreContext;
import com.naver.mage4j.core.mage.core.model.resource.store.StoreHelper;

public class Mage_Core_Controller_Varien_Router_Admin extends Mage_Core_Controller_Varien_Router_Standard {
	@Autowired
	private Mage_Core_Model_Url coreUrl;

	@Autowired
	private StoreContext storeContext;

	@Override
	public void collectRoutes(String configArea, String useRouterName) {
		Mage_Core_Model_Config config = AppContext.getCurrent().getConfig();
		String useCustomAdminPath = config.getNode(Mage_Adminhtml_Helper_Data.XML_PATH_USE_CUSTOM_ADMIN_PATH).getText();
		if (StringUtils.isNotBlank(useCustomAdminPath)) {
			String customUrl = config.getNode(Mage_Adminhtml_Helper_Data.XML_PATH_CUSTOM_ADMIN_PATH).getText();
			String xmlPath = Mage_Adminhtml_Helper_Data.XML_PATH_ADMINHTML_ROUTER_FRONTNAME;
			if (!StringUtils.equals(customUrl, config.getNode(xmlPath).getText())) {
				config.setNode(xmlPath, customUrl, true);
			}
		}

		super.collectRoutes(configArea, useRouterName);
	}

	/**
	 * checking if we installed or not and doing redirect
	 *
	 * @return bool
	 */
	@Override
	protected boolean _afterModuleMatch() {
		Mage_Core_Model_App app = AppContext.getCurrent();
		if (!app.getConfig().isInstalled()) {
			Mage_Core_Controller_Response_Http response = app.getResponse();
			response.setRedirect(coreUrl.getUrl("install", null));
			try {
				response.sendResponse();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			throw new RedirectAndExitException();
		}
		return true;
	}

	/**
	 * We need to have noroute action in this router
	 * not to pass dispatching to next routers
	 *
	 * @return bool
	 */
	@Override
	protected boolean _noRouteShouldBeApplied() {
		return true;
	}

	/**
	 * Check whether URL for corresponding path should use https protocol
	 */
	@Override
	protected boolean _shouldBeSecure(String path) {
		Mage_Core_Model_App app = AppContext.getCurrent();
		StoreHelper helper = storeContext.getAdminStore().getHelper();
		return app.getStoreConfigAsString("default/web/unsecure/base_url", "").startsWith("https")
			|| helper.getConfigFlag("web/secure/use_in_adminhtml")
			&& app.getStoreConfigAsString("default/web/secure/base_url", "").startsWith("https");
	}
}
