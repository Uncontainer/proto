package com.naver.fog.ui.layout;

import java.util.EmptyStackException;
import java.util.Stack;

public class HtmlBuilder {
	private final Stack<String> tagNameStack = new Stack<String>();
	StringBuilder markupBuilder = new StringBuilder();
	StringBuilder scriptBuilder = new StringBuilder();

	public HtmlBuilder appendScript(String script) {
		scriptBuilder.append(script);

		return this;
	}

	public HtmlBuilder openTag(String name, Attribute... attributes) {
		return appendTag(name, false, attributes);
	}

	public HtmlBuilder appendTag(String name, Attribute... attributes) {
		return appendTag(name, true, attributes);
	}

	private HtmlBuilder appendTag(String name, boolean close, Attribute... attributes) {
		markupBuilder.append("<").append(name);
		for (Attribute attribute : attributes) {
			markupBuilder.append(" ").append(attribute.key).append("=\"").append(attribute.value).append("\"");
		}

		if (close) {
			markupBuilder.append("/>");
		} else {
			markupBuilder.append(">");
			tagNameStack.push(name);
		}

		return this;
	}

	public int getCurrentDepth() {
		return tagNameStack.size() + 1;
	}

	public HtmlBuilder closeTag() {
		try {
			String name = tagNameStack.pop();
			markupBuilder.append("</").append(name).append(">");
		} catch (EmptyStackException e) {
			throw new IllegalStateException("Tag unbalanced.");
		}

		return this;
	}

	public HtmlBuilder appendHtml(String html) {
		markupBuilder.append(html);

		return this;
	}

	public String getMarkup() {
		if (!tagNameStack.isEmpty()) {
			throw new IllegalStateException("Some tags are not closed.");
		}

		return markupBuilder.toString();
	}

	public String getScript() {
		if (!tagNameStack.isEmpty()) {
			throw new IllegalStateException("Some tags are not closed.");
		}

		return scriptBuilder.toString();
	}

	public static class Attribute {
		final String key;
		final Object value;

		public Attribute(String key, Object value) {
			super();
			this.key = key;
			this.value = value;
		}
	}
}
