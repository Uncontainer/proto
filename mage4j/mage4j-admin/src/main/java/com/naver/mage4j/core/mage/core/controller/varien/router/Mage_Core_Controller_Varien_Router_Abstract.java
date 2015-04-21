package com.naver.mage4j.core.mage.core.controller.varien.router;

import com.naver.mage4j.core.mage.core.controller.request.Mage_Core_Controller_Request_Http;
import com.naver.mage4j.core.mage.core.controller.varien.Mage_Core_Controller_Varien_Front;

public abstract class Mage_Core_Controller_Varien_Router_Abstract {
	private Mage_Core_Controller_Varien_Front _front;

	public Mage_Core_Controller_Varien_Front getFront() {
		return _front;
	}

	public void setFront(Mage_Core_Controller_Varien_Front front) {
		this._front = front;
	}

	public abstract void collectRoutes(String configArea, String useRouterName);

	abstract public boolean match(Mage_Core_Controller_Request_Http request);

	public String getFrontNameByRoute(String routeName) {
		return routeName;
	}

	public String getRouteByFrontName(String frontName) {
		return frontName;
	}
}
