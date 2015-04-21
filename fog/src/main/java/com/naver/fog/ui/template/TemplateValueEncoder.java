package com.naver.fog.ui.template;

import org.apache.commons.lang3.StringUtils;

import com.naver.fog.ui.layout.AbstractLayout;
import com.naver.fog.ui.layout.Layout;

public class TemplateValueEncoder {
	public static final char VERSION = 'a';
	private static final String TYPE_VIEW = "view";
	private static final String TYPE_EDIT = "edit";

	public static String encode(Template template) {
		StringBuilder builder = new StringBuilder();
		builder.append(VERSION);
		encodeLayout(builder, TYPE_VIEW, template.getViewLayout());
		encodeLayout(builder, TYPE_EDIT, template.getEditLayout());

		return builder.toString();
	}

	private static void encodeLayout(StringBuilder builder, String type, Layout layout) {
		String layoutHtml = AbstractLayout.toLayoutHtml(layout).getMarkup();

		builder.append("|");
		builder.append(type).append(":");
		builder.append(layoutHtml.length()).append('.').append(layoutHtml);
	}

	public static void decode(Template template, String encodedValue) {
		if (StringUtils.isBlank(encodedValue)) {
			return;
		}

		DecodeContext decodeContext = new DecodeContext(encodedValue);

		while (!decodeContext.isEof()) {
			String type = decodeContext.nextViewTypw();
			String layoutHtml = decodeContext.nextString();
			if (TYPE_VIEW.equals(type)) {
				template.setViewLayout(AbstractLayout.fromLayoutHtml(layoutHtml));
			} else if (TYPE_EDIT.equals(type)) {
				template.setEditLayout(AbstractLayout.fromLayoutHtml(layoutHtml));
			} else {
				throw new IllegalArgumentException("Unsupported layout type :" + type);
			}
		}
	}

	static class DecodeContext {
		final String encodedValue;
		int index;

		public DecodeContext(String encodedValue) {
			super();
			this.encodedValue = encodedValue;
			char version = encodedValue.charAt(0);
			if (version != VERSION) {
				throw new IllegalArgumentException("Unsupported content encoding version.(" + version + ")");
			}
			this.index = 1;
		}

		String nextViewTypw() {
			index++; // skip field seperator '|'
			int colonIndex = encodedValue.indexOf(':', index);
			String result = encodedValue.substring(index, colonIndex);
			index = colonIndex + 1;

			return result;
		}

		String nextString() {
			int dotIndex = encodedValue.indexOf('.', index);
			int length = Integer.valueOf(encodedValue.substring(index, dotIndex));
			index = dotIndex + 1;

			String value = encodedValue.substring(index, index + length);
			index += length;

			return value;
		}

		boolean isEof() {
			return index >= encodedValue.length();
		}
	}

}
