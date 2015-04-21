package com.naver.fog.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.fog.AbstractResourceContext;
import com.naver.fog.ResourceType;

@Component
public class ContentContext extends AbstractResourceContext<Content> {
	public ContentContext() {
		super(ResourceType.CONTENT);
	}

	private static ContentContext INSTANCE;

	public static ContentContext getContext() {
		return INSTANCE;
	}

	@Autowired
	public void setContentService(ContentService contentService) {
		super.resourceService = contentService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		INSTANCE = this;
	}
}
