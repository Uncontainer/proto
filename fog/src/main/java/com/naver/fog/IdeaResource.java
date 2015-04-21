package com.naver.fog;

import com.naver.fog.ui.template.Template;
import com.naver.fog.ui.template.TemplateContext;

public abstract class IdeaResource extends Resource {
	protected long defaultTemplateId;
	private transient Template defaultTemplate;

	public long getDefaultTemplateId() {
		return defaultTemplateId;
	}

	public void setDefaultTemplateId(long defaultTemplateId) {
		this.defaultTemplateId = defaultTemplateId;
	}

	public synchronized Template getDefaultTemplate() {
		if (defaultTemplate == null) {
			defaultTemplate = TemplateContext.getContext().get(defaultTemplateId);
		}

		return defaultTemplate;
	}

	@Override
	public void replaceWith(Resource other) {
		if (!(other instanceof IdeaResource)) {
			throw new IllegalArgumentException();
		}

		IdeaResource otherIdeaResource = (IdeaResource)other;

		super.replaceWith(otherIdeaResource);
		this.defaultTemplateId = otherIdeaResource.defaultTemplateId;
		this.defaultTemplate = null;
	}
}
