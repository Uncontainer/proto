package com.yeon.infra.web.freemarker;

import freemarker.core.Environment;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.template.TemplateModelException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

public class BufferedTemplateWriter {
	public static final String PARAM_RESOURCE_URL = "src";
	public static final String PARAM_FLUSH = "flush";
	public static final String PARAM_ON_THE_FLY = "onTheFly";

	public static final String PARAM_RESPONSE_BUFFER = "responseBuffer";

	static final String SCRIPT_START_TAG = "<script type=\"text/javascript\" charset=\"utf-8\">";
	static final String SCRIPT_END_TAG = "</script>";

	private final StringBuffer buffer;
	private Environment environment;
	private boolean writeScriptStartTag;
	private final Set<String> importScriptUrls;

	private BufferedTemplateWriter(int capacity) {
		this.buffer = new StringBuffer(capacity);
		this.importScriptUrls = new HashSet<String>();
	}

	public void reset() {
		buffer.setLength(0);
		importScriptUrls.clear();
	}

	public synchronized BufferedTemplateWriter appendResourceUrl(String url, boolean onTheFly, boolean flush) throws IOException {
		String tag = resourceUrlToTag(url);
		if (tag.isEmpty()) {
			return this;
		}

		if (onTheFly && !flush) {
			environment.getOut().append(tag).append('\n');
			return this;
		}

		if (writeScriptStartTag) {
			buffer.append(SCRIPT_END_TAG).append('\n');
			writeScriptStartTag = false;
		}

		buffer.append(tag).append('\n');
		if (flush) {
			flush();
		}

		return this;
	}

	public synchronized BufferedTemplateWriter appendScriptBody(CharSequence content, boolean onTheFly, boolean flush) throws IOException {
		if (onTheFly && !flush) {
			environment.getOut().append(SCRIPT_START_TAG).append('\n').append(content).append('\n').append(SCRIPT_END_TAG).append('\n');
			return this;
		}

		if (!writeScriptStartTag) {
			buffer.append(SCRIPT_START_TAG).append('\n');
			writeScriptStartTag = true;
		}

		buffer.append(content);
		if (flush) {
			flush();
		}

		return this;
	}

	private String resourceUrlToTag(String resourceUrl) {
		if (resourceUrl.endsWith(".js")) {
			if (importScriptUrls.contains(resourceUrl)) {
				return "";
			} else {
				importScriptUrls.add(resourceUrl);
			}

			return String.format("<script type=\"text/javascript\" src=\"%s\" charset=\"utf-8\"></script>", resourceUrl);
		} else if (resourceUrl.endsWith(".css")) {
			return String.format("<link rel=\"stylesheet\" type=\"text/css\" href=\"%s\" />", resourceUrl);
		} else {
			return resourceUrl;
		}
	}

	public synchronized BufferedTemplateWriter flush() throws IOException {
		Writer writer = environment.getOut();
		writer.append(buffer).append('\n');
		if (writeScriptStartTag) {
			writer.append(SCRIPT_END_TAG).append('\n');
			writeScriptStartTag = false;
		}

		buffer.setLength(0);

		return this;
	}

	public static BufferedTemplateWriter fromEnvironment(Environment environment) {
		HttpServletRequest request;
		try {
			request = ((HttpRequestHashModel)environment.getDataModel().get("Request")).getRequest();
		} catch (TemplateModelException e) {
			throw new RuntimeException(e);
		}

		BufferedTemplateWriter buffer = (BufferedTemplateWriter)request.getAttribute(PARAM_RESPONSE_BUFFER);
		if (buffer == null) {
			// TODO pooling 고려.
			buffer = new BufferedTemplateWriter(1024 * 10);
			buffer.environment = environment;
			request.setAttribute(PARAM_RESPONSE_BUFFER, buffer);
		} else {
			if (buffer.environment.getOut() != environment.getOut()) {
				throw new IllegalStateException();
			}
		}

		return buffer;
	}
}
