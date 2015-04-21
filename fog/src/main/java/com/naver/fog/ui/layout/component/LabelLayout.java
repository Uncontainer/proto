package com.naver.fog.ui.layout.component;

import java.util.Map;

import org.htmlcleaner.TagNode;

import com.naver.fog.Resource;
import com.naver.fog.ui.layout.AbstractLayout;
import com.naver.fog.ui.layout.HtmlBuilder;
import com.naver.fog.ui.layout.HtmlBuilder.Attribute;
import com.naver.fog.ui.layout.TagIdGenerator;
import com.naver.fog.web.ViewMode;

public class LabelLayout extends AbstractLayout {
	public static final String ALIAS = "label";
	private String label;

	public LabelLayout(long targetResourceId) {
		this(targetResourceId, null);
	}

	public LabelLayout(Resource resource) {
		this(resource.getId(), resource.getName());
	}

	public LabelLayout(long targetResourceId, String label) {
		super(targetResourceId);
		this.label = label;
	}

	public String getAlias() {
		return ALIAS;
	}

	public void toTemplateHtml(HtmlBuilder builder, ViewMode mode) {
		builder.appendHtml(label);
	}

	public void toLayoutHtml(HtmlBuilder builder) {
		String id = TagIdGenerator.nextId("__label");
		builder.appendScript("$('#" + id + "').bind('keyup', function(ev){"
			+ "$(ev.target).attr('data-value', ev.target.value);"
			+ "});");

		openLayoutTag(builder, this, "span");
		builder.appendTag("input", new Attribute("type", "text"), new Attribute("value", label), new Attribute(ATTR_ID, id), new Attribute("data-value", label));
		closeLayoutTag(builder);
	}

	public void fromLayoutTag(TagNode parentTag) {
		TagNode inputTag = getUniqueChild(parentTag);
		label = inputTag.getAttributeByName("data-value");
	}

	public int getExpectedWidth(Map<Long, Object> valueMap) {
		return label.length() * 10;
	}
}
