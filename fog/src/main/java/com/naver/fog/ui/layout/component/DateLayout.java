package com.naver.fog.ui.layout.component;

import java.util.Map;

import org.htmlcleaner.TagNode;

import com.naver.fog.Resource;
import com.naver.fog.ResourceUtils;
import com.naver.fog.ui.layout.AbstractLayout;
import com.naver.fog.ui.layout.HtmlBuilder;
import com.naver.fog.ui.layout.HtmlBuilder.Attribute;
import com.naver.fog.web.ViewMode;

public class DateLayout extends AbstractLayout {
	public static final String ALIAS = "date";

	public DateLayout(long targetResourceId) {
		super(targetResourceId);
	}

	public String getAlias() {
		return ALIAS;
	}

	public void toTemplateHtml(HtmlBuilder builder, ViewMode mode) {
		switch (mode) {
			case ADD:
			case EDIT:
				builder.appendTag("input"
					, new Attribute("type", "text")
					, new Attribute("name", getTargetResourceId())
					, new Attribute("value", "${" + getTargetResourceId() + "!''}"));
				break;
			case READ:
				builder.appendHtml("${" + getTargetResourceId() + "!''}");
				break;
			default:
				throw new IllegalArgumentException("Unsupported mode: " + mode);
		}
	}

	public void toLayoutHtml(HtmlBuilder builder) {
		Resource resource = ResourceUtils.getResource(getTargetResourceId());
		openLayoutTag(builder, this, "span");
		builder.appendHtml(resource.getType() + ":" + resource.getName());
		closeLayoutTag(builder);
	}

	public void fromLayoutTag(TagNode parentTag) {
		// do nothing
	}

	public int getExpectedWidth(Map<Long, Object> valueMap) {
		return 150;
	}
}
