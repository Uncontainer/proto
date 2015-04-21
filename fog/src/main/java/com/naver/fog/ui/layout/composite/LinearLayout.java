package com.naver.fog.ui.layout.composite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.htmlcleaner.TagNode;

import com.naver.fog.ui.layout.AbstractLayout;
import com.naver.fog.ui.layout.HtmlBuilder;
import com.naver.fog.ui.layout.HtmlBuilder.Attribute;
import com.naver.fog.ui.layout.Layout;
import com.naver.fog.ui.layout.TagIdGenerator;
import com.naver.fog.web.ViewMode;

public class LinearLayout extends AbstractLayout {
	public static final String ALIAS = "linear";

	protected List<Layout> children = new ArrayList<Layout>();

	public LinearLayout(long targetResourceId) {
		super(targetResourceId);
	}

	public String getAlias() {
		return ALIAS;
	}

	public void toTemplateHtml(HtmlBuilder builder, ViewMode mode) {
		builder.openTag("div");
		for (Layout child : children) {
			if (!child.isEnabled(mode)) {
				continue;
			}

			builder.openTag("div");
			child.toTemplateHtml(builder, mode);
			builder.closeTag();
		}
		builder.closeTag();
	}

	public void toLayoutHtml(HtmlBuilder builder) {
		String id = TagIdGenerator.nextId(ALIAS);
		String el = "$(\"#" + id + "\")";
		builder.appendScript(el).appendScript(".sortable();");
		builder.appendScript(el).appendScript(".disableSelection();");

		openLayoutTag(builder, this, "ol", new Attribute(ATTR_ID, id), new Attribute(ATTR_CLASS, "linear_layout"));
		for (Layout child : children) {
			builder.openTag("li", new Attribute(ATTR_CLASS, "ui-widget-content"));
			child.toLayoutHtml(builder);
			builder.closeTag();
		}
		closeLayoutTag(builder);
	}

	public void fromLayoutTag(TagNode parentTag) {
		List<TagNode> liTagList = parentTag.getChildTagList();
		for (TagNode liTag : liTagList) {
			add(parseLayoutTag(getUniqueChild(liTag)));
		}
	}

	@Override
	public void add(Layout layout) {
		children.add(layout);
	}

	public int getExpectedWidth(Map<Long, Object> valueMap) {
		return getExpectedWidth(children, valueMap);
	}
}
