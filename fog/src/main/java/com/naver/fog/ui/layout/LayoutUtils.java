package com.naver.fog.ui.layout;

import com.naver.fog.web.ViewMode;

public class LayoutUtils {
	public static String toTemplateHtml(Layout layout, ViewMode mode) {
		HtmlBuilder builder = new HtmlBuilder();
		layout.toTemplateHtml(builder, mode);
		return builder.getMarkup();
	}
}
