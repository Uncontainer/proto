package com.naver.fog.ui.template;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.fog.CachedResourceContext;
import com.naver.fog.ResourceType;

@Component
public class TemplateContext extends CachedResourceContext<Template> implements InitializingBean {
	public TemplateContext() {
		super(ResourceType.TEMPLATE, 1000);
	}

	private static TemplateContext CONTEXT;

	public static TemplateContext getContext() {
		return CONTEXT;
	}

	@Autowired
	private TemplateService templateService;

	@Autowired
	public void setTemplateService(TemplateService templateService) {
		super.resourceService = templateService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		CONTEXT = this;
	}
}
