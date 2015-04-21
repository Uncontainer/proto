package com.naver.mage4j.external.zend.controller.response;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.naver.mage4j.external.php.Standard;

public class Zend_Controller_Response_Abstract {
	private final Logger log = LoggerFactory.getLogger(Zend_Controller_Response_Abstract.class);

	protected final HttpServletResponse servletResponse;

	/**
	 * Array of raw headers. Each header is a single string, the entire header to emit
	 */
	//	private List<String> _headersRaw = new ArrayList<String>();

	/**
	 * Array of headers. Each header is an array with keys 'name' and 'value'
	 */
	private List<Header> _headers = new ArrayList<Header>();

	/**
	* Flag; is this response a redirect?
	*/
	protected boolean _isRedirect = false;

	static class Header {
		String name;
		String value;
		boolean replace;

		public Header(String name, String value, boolean replace) {
			super();
			this.name = name;
			this.value = value;
			this.replace = replace;
		}
	}

	/**
	* Body content
	* @var array
	*/
	private Map<String, StringBuilder> _body = new LinkedHashMap<String, StringBuilder>();

	/**
	* Exception stack
	* @var Exception
	*/
	private List<Exception> _exceptions = new ArrayList<Exception>();

	/**
	 * Whether or not to render exceptions; off by default
	 * @var boolean
	 */
	private boolean _renderExceptions = false;

	/**
	 * HTTP response code to use in headers
	 * @var int
	 */
	private int _httpResponseCode = HttpStatus.OK.value();

	/**
	 * Flag; if true, when header operations are called after headers have been
	 * sent, an exception will be raised; otherwise, processing will continue
	 * as normal. Defaults to true.
	 *
	 * @see canSendHeaders()
	 * @var boolean
	 */
	private boolean headersSentThrowsException = true;

	public Zend_Controller_Response_Abstract(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	/**
	 * Send the response, including all headers, rendering exceptions if so
	 * requested.
	 * @throws IOException 
	 */
	public void sendResponse() throws IOException {
		sendHeaders();

		if (!_exceptions.isEmpty() && _renderExceptions) {
			StringBuffer exceptions = new StringBuffer();
			for (Exception exception : _exceptions) {
				exceptions.append(exception.getMessage()).append("\n");
			}

			log.error(exceptions.toString());

			return;
		}

		outputBody();
	}

	/**
	* Append content to the body content
	*
	* @param string $content
	* @param null|string $name
	* @return Zend_Controller_Response_Abstract
	*/
	public Zend_Controller_Response_Abstract appendBody(String content, String name) {
		if (name == null) {
			name = "default";
		}

		StringBuilder savedContent = this._body.get(name);
		if (savedContent == null) {
			savedContent = new StringBuilder(content);
			this._body.put(name, savedContent);
		} else {
			savedContent.append(content);
		}

		return this;
	}

	public Zend_Controller_Response_Abstract appendBody(String content) {
		return appendBody(content, null);
	}

	/**
	* Echo the body segments
	*
	* @return void
	 * @throws IOException 
	*/
	public void outputBody() throws IOException {
		PrintWriter writer = servletResponse.getWriter();
		for (StringBuilder content : _body.values()) {
			writer.print(content.toString());
		}
	}

	boolean __headSent = false;

	/**
	* Send all headers
	*
	* Sends any headers specified. If an {@link setHttpResponseCode() HTTP response code}
	* has been specified, it is sent with the first header.
	*
	* @return Zend_Controller_Response_Abstract
	*/
	public Zend_Controller_Response_Abstract sendHeaders() {
		// Only check if we can send headers if we have headers to send
		if (/*!_headersRaw.isEmpty() || */!_headers.isEmpty() || HttpStatus.OK.value() != _httpResponseCode) {
			canSendHeaders(true);
		} else if (HttpStatus.OK.value() == this._httpResponseCode) {
			// Haven't changed the response code, and we have no headers
			return this;
		}

		boolean httpCodeSent = false;
		//		for (String headerValue : _headersRaw) {
		//			if (!httpCodeSent && _httpResponseCode > 0) {
		//				header(headerValue, true, _httpResponseCode);
		//				httpCodeSent = true;
		//			} else {
		//				header(headerValue, null, null);
		//			}
		//		}

		for (Header header : _headers) {
			if (!httpCodeSent) {
				servletResponse.setStatus(_httpResponseCode);
			}

			servletResponse.setHeader(header.name, header.value);

			//			header(header.name + ": " + header.value, header.replace, null);
			httpCodeSent = true;
		}

		if (!httpCodeSent) {
			//			header("HTTP/1.1 " + _httpResponseCode, null, null);
			servletResponse.setStatus(_httpResponseCode);
			httpCodeSent = true;
		}

		__headSent = httpCodeSent;

		return this;
	}

	//	private void header(String headerValue, Boolean replace, Integer http_response_code) {
	//		throw new UnsupportedOperationException();
	//	}

	/**
	 * Set a header
	 *
	 * If $replace is true, replaces any headers already defined with that
	 * $name.
	 *
	 * @param string $name
	 * @param string $value
	 * @param boolean $replace
	 * @return Zend_Controller_Response_Abstract
	 */
	public Zend_Controller_Response_Abstract setHeader(String name, String value, boolean replace) {
		canSendHeaders(true);
		name = _normalizeHeader(name);

		if (replace) {
			Iterator<Header> iter = _headers.iterator();
			while (iter.hasNext()) {
				Header header = iter.next();
				if (header.name.equals(name)) {
					iter.remove();
				}
			}
		}

		_headers.add(new Header(name, value, replace));

		return this;
	}

	public void setHeader(String name, String value) {
		setHeader(name, value, false);
	}

	/**
	 * Set raw HTTP header
	 *
	 * Allows setting non key => value headers, such as status codes
	 */
	//	public void setRawHeader(String value) {
	//		if (value == null) {
	//			return;
	//		}
	//
	//		canSendHeaders(true);
	//		if (value.startsWith("Location")) {
	//			_isRedirect = true;
	//		}
	//
	//		_headersRaw.add(value);
	//	}

	/**
	 * Normalize a header name
	 *
	 * Normalizes a header name to X-Capitalized-Names
	 *
	 * @param  string $name
	 * @return string
	 */
	protected String _normalizeHeader(String name) {
		String filtered = name.replaceAll("-|_", " ");
		filtered = Standard.ucwords(filtered.toLowerCase());
		return filtered.replace(' ', '-');
	}

	/**
	 * Can we send headers?
	 *
	 * @param boolean $throw Whether or not to throw an exception if headers have been sent; defaults to false
	 * @return boolean
	 * @throws Zend_Controller_Response_Exception
	 */
	public boolean canSendHeaders(boolean throwException) {
		boolean ok = __headSent; //headers_sent($file, $line);
		if (ok && throwException && headersSentThrowsException) {
			throw new Zend_Controller_Response_Exception("Cannot send headers; headers already sent");
		}

		return !ok;
	}

	/**
	 * Set HTTP response code to use with headers
	 *
	 * @param int code
	 * @return Zend_Controller_Response_Abstract
	 */
	public Zend_Controller_Response_Abstract setHttpResponseCode(int code) {
		if (100 > code || 599 < code) {
			throw new Zend_Controller_Response_Exception("Invalid HTTP response code");
		}

		if ((300 <= code) && (307 >= code)) {
			_isRedirect = true;
		} else {
			_isRedirect = false;
		}

		_httpResponseCode = code;

		return this;
	}
}
