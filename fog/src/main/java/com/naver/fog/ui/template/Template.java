package com.naver.fog.ui.template;

import com.naver.fog.Resource;
import com.naver.fog.ResourceType;
import com.naver.fog.ResourceUtils;
import com.naver.fog.ui.layout.Layout;
import com.naver.fog.ui.layout.LayoutUtils;
import com.naver.fog.web.ViewMode;

public class Template extends Resource {
	private long targetResourceId;
	private Layout viewLayout;
	private Layout editLayout;

	public Template() {
	}

	@Override
	public ResourceType getType() {
		return ResourceType.TEMPLATE;
	}

	public long getTargetResourceId() {
		return targetResourceId;
	}

	public void setTargetResourceId(long targetResourceId) {
		this.targetResourceId = targetResourceId;
	}

	public Resource getTargetResource() {
		return ResourceUtils.getResource(targetResourceId);
	}

	@Override
	public void replaceWith(Resource other) {
		if (!(other instanceof Template)) {
			throw new IllegalArgumentException();
		}

		Template otherTemplate = (Template)other;
		super.replaceWith(other);
		this.targetResourceId = otherTemplate.targetResourceId;
		this.viewLayout = otherTemplate.viewLayout;
		this.editLayout = otherTemplate.editLayout;
	}

	public String getTemplateContent(ViewMode mode) {
		Layout layout = mode.isModifiable() ? editLayout : viewLayout;
		return LayoutUtils.toTemplateHtml(layout, mode);
	}

	public Layout getViewLayout() {
		return viewLayout;
	}

	public void setViewLayout(Layout viewLayout) {
		this.viewLayout = viewLayout;
	}

	public Layout getEditLayout() {
		return editLayout;
	}

	public void setEditLayout(Layout editLayout) {
		this.editLayout = editLayout;
	}
}
