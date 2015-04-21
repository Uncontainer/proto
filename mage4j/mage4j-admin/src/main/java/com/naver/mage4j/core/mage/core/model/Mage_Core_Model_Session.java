package com.naver.mage4j.core.mage.core.model;

import java.util.Map;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.model.session.Mage_Core_Model_Session_Abstract;

public class Mage_Core_Model_Session extends Mage_Core_Model_Session_Abstract {
	public Mage_Core_Model_Session(Map<String, Object> data) {
		super(data);
		String name = (String)data.get("name");
		init("core", name);
	}

	/**
	 * Retrieve Session Form Key
	 *
	 * @return string A 16 bit unique key for forms
	 */
	public String getFormKey() {
		String result = (String)this.getData("_form_key");
		if (result == null) {
			result = AppContext.getCurrent().getHelperData().getRandomString(16, null);
			this.setData("_form_key", result);
		}

		return result;
	}

	public boolean isAllowed(String acl) {
		return false;
	}

	public Boolean getCookieShouldBeReceived() {
		return (Boolean)this.getData("Cookie_Should_Be_Received");
	}

	public Mage_Core_Model_Session setCookieShouldBeReceived(boolean flag) {
		this.setData("Cookie_Should_Be_Received", flag);
		return this;
	}

	public Mage_Core_Model_Session unsCookieShouldBeReceived() {
		this.unsetData("Cookie_Should_Be_Received");
		return this;
	}
}
