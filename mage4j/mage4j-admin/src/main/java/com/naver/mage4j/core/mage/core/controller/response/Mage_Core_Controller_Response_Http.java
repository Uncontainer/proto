package com.naver.mage4j.core.mage.core.controller.response;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.http.HttpServletResponse;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.model.event.EventDispatcher;
import com.naver.mage4j.external.zend.controller.response.Zend_Controller_Response_Abstract;

public class Mage_Core_Controller_Response_Http extends Zend_Controller_Response_Abstract {
	public boolean headersSentThrowsException = true;

	public Mage_Core_Controller_Response_Http(HttpServletResponse servletResponse) {
		super(servletResponse);
	}

	@Override
	public void sendResponse() throws IOException {
		EventDispatcher eventDispatcher = AppContext.getCurrent().getEventDispatcher();
		eventDispatcher.dispatchEvent("http_response_send_before", Collections.singletonMap("response", (Object)this));
		super.sendResponse();
	}

	/**
	 * Set redirect URL
	 *
	 * Sets Location header and response code. Forces replacement of any prior redirects.
	 */
	public Mage_Core_Controller_Response_Http setRedirect(String url, int code) {
		canSendHeaders(true);
		setHeader("Location", url, true).setHttpResponseCode(code);

		return this;
	}

	public Mage_Core_Controller_Response_Http setRedirect(String url) {
		return setRedirect(url, 302);
	}
}
