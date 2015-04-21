package com.naver.fog;

import com.naver.fog.content.ContentContext;
import com.naver.fog.field.FieldContext;
import com.naver.fog.frame.FrameContext;
import com.naver.fog.ui.template.TemplateContext;

public class ResourceUtils {
	public static Resource getResource(long id) {
		AbstractResourceContext<?> resourceContext = getResourceContext(id);
		if (resourceContext != null) {
			return resourceContext.get(id);
		}

		return null;
	}

	public static Resource getResourceSafely(long id) {
		AbstractResourceContext<?> resourceContext = getResourceContext(id);
		if (resourceContext != null) {
			return resourceContext.getSafely(id);
		}

		return null;
	}

	private static AbstractResourceContext<?> getResourceContext(long id) {
		switch (ResourceType.fromResourceId(id)) {
			case FRAME:
				return FrameContext.getContext();
			case FIELD:
				return FieldContext.getContext();
			case CONTENT:
				return ContentContext.getContext();
			case TEMPLATE:
				return TemplateContext.getContext();
		}

		return null;
	}
}
