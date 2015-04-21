package com.naver.fog.field;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.fog.CachedResourceContext;
import com.naver.fog.ResourceType;
import com.naver.fog.ui.template.TemplateContext;
import com.naver.fog.ui.template.TemplateService;

@Component
public class FieldContext extends CachedResourceContext<Field> implements InitializingBean {
	private static FieldContext INSTANCE;

	public static FieldContext getContext() {
		return INSTANCE;
	}

	@Autowired
	private TemplateService templateService;

	@Autowired
	private TemplateContext templateContext;

	public FieldContext() {
		super(ResourceType.FIELD, 5000);
		// cache가 expire될 경우 frame의 dangling reference가 문제될 수 있으나 일단 무시한다.
	}

	@Autowired
	public void setFieldService(FieldService fieldService) {
		super.resourceService = fieldService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		INSTANCE = this;
	}
}
