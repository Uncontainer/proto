package com.naver.fog.ui.layout.composite;

import java.util.List;
import java.util.Map;

import org.htmlcleaner.TagNode;

import com.naver.fog.ui.layout.AbstractLayout;
import com.naver.fog.ui.layout.HtmlBuilder;
import com.naver.fog.ui.layout.Layout;
import com.naver.fog.ui.layout.component.LabelLayout;
import com.naver.fog.web.ViewMode;

public class LayoutWithLabel extends AbstractLayout {
	public static final String ALIAS = "labeded";

	private LabelLayout label;
	private Layout element;

	public LayoutWithLabel(long targetResourceId) {
		super(targetResourceId);
	}

	public LayoutWithLabel(LabelLayout label, Layout element) {
		super(element.getTargetResourceId());
		this.label = label;
		this.element = element;
	}

	public String getAlias() {
		return ALIAS;
	}

	public LabelLayout getLabel() {
		return label;
	}

	public Layout getElement() {
		return element;
	}

	public void toTemplateHtml(HtmlBuilder builder, ViewMode mode) {
		builder.openTag("div");
		{
			builder.openTag("span");
			label.toTemplateHtml(builder, mode);
			builder.closeTag();

			builder.openTag("span");
			element.toTemplateHtml(builder, mode);
			builder.closeTag();
		}
		builder.closeTag();
	}

	public void toLayoutHtml(HtmlBuilder builder) {
		openLayoutTag(builder, this, "div");
		{
			builder.openTag("span");
			label.toLayoutHtml(builder);
			builder.closeTag();

			builder.openTag("span");
			element.toLayoutHtml(builder);
			builder.closeTag();
		}
		closeLayoutTag(builder);
	}

	public void fromLayoutTag(TagNode parentTag) {
		List<TagNode> spanTagList = parentTag.getChildTagList();
		assert (spanTagList.size() == 2);
		label = (LabelLayout)parseLayoutTag(getUniqueChild(spanTagList.get(0)));
		element = parseLayoutTag(getUniqueChild(spanTagList.get(1)));
	}

	public int getExpectedWidth(Map<Long, Object> valueMap) {
		return label.getExpectedWidth(valueMap) + element.getExpectedWidth(valueMap);
	}
}
