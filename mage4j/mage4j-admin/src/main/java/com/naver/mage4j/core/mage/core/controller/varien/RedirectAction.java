package com.naver.mage4j.core.mage.core.controller.varien;

import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Url.RouteParams;
import com.naver.mage4j.external.varien.Varien_Object;

public class RedirectAction extends Varien_Object {
	public String getRedirectUrl() {
		return (String)getData("rediret_url");
	}

	public String getPath() {
		return (String)getData("path");
	}

	public RouteParams getArguments() {
		return (RouteParams)getData("arguments");
	}

	public boolean getRedirect() {
		Boolean redirect = (Boolean)getData("redirect");
		return redirect != null ? redirect : false;
	}
}
