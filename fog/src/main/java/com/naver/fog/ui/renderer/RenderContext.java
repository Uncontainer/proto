package com.naver.fog.ui.renderer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.fog.content.Content;
import com.naver.fog.ui.template.Template;
import com.naver.fog.ui.template.TemplateContext;
import com.naver.fog.web.ViewMode;

@Component
public class RenderContext {
	@Autowired
	private TemplateContext templateContext;

	public String render(Content content, ViewMode viewMode) {
		// TODO customizing된 template를 가져오도록 수정.
		Template template = content.getFrame().getDefaultTemplate();
		Renderer renderer = new FogRenderer(template, viewMode);

		return renderer.render(content.getFieldValueMap());
	}
}
