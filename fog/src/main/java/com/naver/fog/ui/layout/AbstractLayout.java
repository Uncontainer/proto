package com.naver.fog.ui.layout;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import com.naver.fog.Resource;
import com.naver.fog.frame.Frame;
import com.naver.fog.frame.FrameContext;
import com.naver.fog.ui.layout.HtmlBuilder.Attribute;
import com.naver.fog.web.ViewMode;

public abstract class AbstractLayout implements Layout {
	protected static final String ATTR_TARGET_RESOURCE_ID = "data-id";
	protected static final String ATTR_ALIAS = "data-alias";

	protected static final String ATTR_ID = "id";
	protected static final String ATTR_CLASS = "class";

	protected long targetResourceId;
	byte viewModeMask = 7;

	protected AbstractLayout(long targetResourceId) {
		this.targetResourceId = targetResourceId;
	}

	public long getTargetResourceId() {
		return targetResourceId;
	}

	public boolean isEnabled(ViewMode mode) {
		return (viewModeMask & getMask(mode)) != 0;
	}

	public void setEnabled(ViewMode mode, boolean enabled) {
		if (enabled) {
			viewModeMask |= getMask(mode);
		} else {
			viewModeMask &= (~getMask(mode));
		}
	}

	private byte getMask(ViewMode mode) {
		switch (mode) {
			case ADD:
				return (byte)1;
			case EDIT:
				return (byte)2;
			case READ:
				return (byte)4;
			default:
				throw new IllegalArgumentException();
		}
	}

	public void add(Layout layout) {
		throw new UnsupportedOperationException();
	}

	private static final HtmlCleaner cleaner = new HtmlCleaner();

	public static Layout fromLayoutHtml(String html) {
		return fromLayoutHtml(html, Resource.NULL_ID);
	}

	public static Layout fromLayoutHtml(String html, long targetResourceId) {
		if (StringUtils.isBlank(html)) {
			return null;
		}

		TagNode rootLayoutTag = getLayoutRoot(html);

		Map<String, String> attributes = rootLayoutTag.getAttributes();
		if (targetResourceId != Resource.NULL_ID) {
			long prevTargetResourceId = Long.parseLong(attributes.get(AbstractLayout.ATTR_TARGET_RESOURCE_ID));
			if (prevTargetResourceId != targetResourceId) {
				FrameContext frameContext = FrameContext.getContext();
				Frame frame = frameContext.get(targetResourceId);
				if (frame == null) {
					throw new IllegalArgumentException("Layout target-resource-id can change for frame.");
				}

				if (!frame.isChildOf(prevTargetResourceId)) {
					throw new IllegalArgumentException("Chagned Layout target-resource-id must be a child frame.");
				}

				attributes.put(AbstractLayout.ATTR_TARGET_RESOURCE_ID, Long.toString(targetResourceId));
			}
		}

		return parseLayoutTag(rootLayoutTag);
	}

	private static TagNode getLayoutRoot(String html) {
		TagNode htmlTag = cleaner.clean(html);
		TagNode bodyTag = htmlTag.getElementsByName("body", false)[0];

		return getUniqueChild(bodyTag);
	}

	protected static TagNode getUniqueChild(TagNode tagNode) {
		List<TagNode> childTagList = tagNode.getChildTagList();
		assert (childTagList.size() == 1);
		return childTagList.get(0);
	}

	protected static AbstractLayout parseLayoutTag(TagNode layoutTag) {
		Map<String, String> attributes = layoutTag.getAttributes();
		long targetResourceId = Long.parseLong(attributes.get(AbstractLayout.ATTR_TARGET_RESOURCE_ID));

		AbstractLayout layout = LayoutFactory.newInstance(attributes.get(AbstractLayout.ATTR_ALIAS), targetResourceId);
		layout.fromLayoutTag(layoutTag);

		return layout;
	}

	protected static void openLayoutTag(HtmlBuilder builder, Layout layout, String name, Attribute... attributes) {
		Attribute[] fullAttributes = new Attribute[2 + attributes.length];
		fullAttributes[0] = new Attribute(ATTR_TARGET_RESOURCE_ID, layout.getTargetResourceId());
		fullAttributes[1] = new Attribute(ATTR_ALIAS, layout.getAlias());
		for (int i = 0; i < attributes.length; i++) {
			fullAttributes[i + 2] = attributes[i];
		}

		builder.openTag(name, fullAttributes);
	}

	protected static void closeLayoutTag(HtmlBuilder builder) {
		builder.closeTag();
	}

	public static HtmlBuilder toLayoutHtml(Layout layout) {
		HtmlBuilder builder = new HtmlBuilder();
		layout.toLayoutHtml(builder);

		return builder;
	}

	protected static int getExpectedWidth(List<? extends Layout> layouts, Map<Long, Object> valueMap) {
		int expectedWidth = 10;
		for (Layout layout : layouts) {
			int childExpectedWidth = layout.getExpectedWidth(valueMap);
			if (childExpectedWidth > expectedWidth) {
				expectedWidth = childExpectedWidth;
			}
		}

		return expectedWidth;
	}

}
