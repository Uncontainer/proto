package com.naver.fog.ui.layout.composite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.htmlcleaner.TagNode;

import com.naver.fog.Resource;
import com.naver.fog.ResourceUtils;
import com.naver.fog.ui.layout.AbstractLayout;
import com.naver.fog.ui.layout.HtmlBuilder;
import com.naver.fog.ui.layout.HtmlBuilder.Attribute;
import com.naver.fog.ui.layout.Layout;
import com.naver.fog.ui.layout.TagIdGenerator;
import com.naver.fog.ui.layout.component.LabelLayout;
import com.naver.fog.web.ViewMode;

public class LabeledLinearLayout extends AbstractLayout {
	public static final String ALIAS = "labededLinear";

	private final List<LabelLayout> labels = new ArrayList<LabelLayout>();
	private final List<Layout> elements = new ArrayList<Layout>();

	public LabeledLinearLayout(long targetResourceId) {
		super(targetResourceId);
	}

	public String getAlias() {
		return ALIAS;
	}

	public void toTemplateHtml(HtmlBuilder builder, ViewMode mode) {
		builder.openTag("table", new Attribute("style", "width:100%"));

		{
			builder.openTag("colgroup");
			builder.appendTag("col", new Attribute("style", "width:120px"));
			builder.appendTag("col", new Attribute("style", "width:500px"));
			builder.closeTag();
		}

		for (int i = 0; i < labels.size(); i++) {
			LabelLayout label = labels.get(i);
			Layout layout = elements.get(i);
			if (!layout.isEnabled(mode)) {
				continue;
			}

			builder.openTag("tr");
			{
				builder.openTag("th");
				label.toTemplateHtml(builder, mode);
				builder.closeTag();
				builder.openTag("td");
				layout.toTemplateHtml(builder, mode);
				builder.closeTag();
			}
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
		for (int i = 0; i < labels.size(); i++) {
			LabelLayout label = labels.get(i);
			Layout layout = elements.get(i);
			builder.openTag("li", new Attribute(ATTR_CLASS, "ui-widget-content"));
			{
				builder.openTag("span", new Attribute("style", "width:50px"));
				label.toLayoutHtml(builder);
				builder.closeTag();

				builder.openTag("span");
				layout.toLayoutHtml(builder);
				builder.closeTag();
			}
			builder.closeTag();
		}

		closeLayoutTag(builder);
	}

	public void fromLayoutTag(TagNode parentTag) {
		List<TagNode> liTagList = parentTag.getChildTagList();
		for (TagNode liTag : liTagList) {
			List<TagNode> spanTagList = liTag.getChildTagList();
			assert (spanTagList.size() == 2);

			LabelLayout label = (LabelLayout)parseLayoutTag(getUniqueChild(spanTagList.get(0)));
			Layout element = parseLayoutTag(getUniqueChild(spanTagList.get(1)));

			labels.add(label);
			elements.add(element);
		}
	}

	@Override
	public void add(Layout layout) {
		Resource targetResource = ResourceUtils.getResourceSafely(layout.getTargetResourceId());
		labels.add(new LabelLayout(targetResource));
		elements.add(layout);
	}

	public int getExpectedWidth(Map<Long, Object> valueMap) {
		return getLabelExpectedWidth(valueMap) + getElementExpectedWidth(valueMap);
	}

	private int getLabelExpectedWidth(Map<Long, Object> valueMap) {
		return getExpectedWidth(labels, valueMap);
	}

	private int getElementExpectedWidth(Map<Long, Object> valueMap) {
		return getExpectedWidth(elements, valueMap);
	}
}
