package com.naver.fog.ui.template;

import com.naver.fog.web.FogHandledException;

public class TemplateNotFoundException extends FogHandledException {
	private static final long serialVersionUID = 8735789530292501613L;

	public TemplateNotFoundException(long templateId) {
		this(templateId, HandleType.ALERT_AND_BACK);
	}

	public TemplateNotFoundException(long templateId, HandleType handleType) {
		super(templateId, null, handleType, null, "Fail to find template.(" + templateId + ")");
	}
}
