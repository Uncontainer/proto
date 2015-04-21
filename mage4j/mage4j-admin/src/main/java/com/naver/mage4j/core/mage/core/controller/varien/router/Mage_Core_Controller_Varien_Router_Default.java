package com.naver.mage4j.core.mage.core.controller.varien.router;

import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.Mage_Core_Model_App;
import com.naver.mage4j.core.mage.core.controller.request.Mage_Core_Controller_Request_Http;

public class Mage_Core_Controller_Varien_Router_Default extends Mage_Core_Controller_Varien_Router_Abstract {
	@Override
	public void collectRoutes(String area, String routerCode) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean match(Mage_Core_Controller_Request_Http request) {
		Mage_Core_Model_App app = AppContext.getCurrent();
		String[] noRoute = StringUtils.split(app.getStoreConfigAsString("web/default/no_route", ""), "/");

		String moduleName = (noRoute.length >= 3) ? noRoute[2] : "core";
		String controllerName = (noRoute.length >= 2) ? noRoute[1] : "index";
		String actionName = (noRoute.length >= 1) ? noRoute[0] : "index";

		if (app.getStore().getHelper().isAdmin()) {
			String adminFrontName = app.getConfig().getNode("admin/routers/adminhtml/args/frontName").getText();

			if (adminFrontName != moduleName) {
				moduleName = "core";
				controllerName = "index";
				actionName = "noRoute";
				app.setCurrentStore(app.getDefaultStoreView());
			}
		}

		request.setModuleName(moduleName);
		request.setControllerName(controllerName);
		request.setActionName(actionName);

		return true;
	}
}
