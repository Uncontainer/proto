package com.naver.fog.ui.renderer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

import com.naver.fog.web.ViewMode;

import freemarker.cache.StringTemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class FreemarkerRenderer implements Renderer {
	private static final Configuration config;
	private static final BeansWrapper wrapper;
	private static final StringTemplateLoader templateLoader = new StringTemplateLoader();

	static {
		config = new Configuration();

		wrapper = BeansWrapper.getDefaultInstance();
		config.setObjectWrapper(wrapper);

		config.setTemplateLoader(templateLoader);

		config.setDefaultEncoding("UTF-8");
		config.setTemplateUpdateDelay(60);
		config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		config.setTemplateUpdateDelay(0);
		config.setDateFormat("yyyy-MM-dd");
		config.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");

		config.setLocale(Locale.KOREA);
	}

	private final com.naver.fog.ui.template.Template template;
	private final ViewMode mode;

	public FreemarkerRenderer(com.naver.fog.ui.template.Template template, ViewMode mode) {
		super();
		this.template = template;
		this.mode = mode;
	}

	public String render(Map<Long, Object> data) {
		long templateId = template.getId();
		String templateContent = template.getTemplateContent(mode);
		templateLoader.putTemplate(Long.toString(templateId), templateContent);
		Template template = null;
		try {
			template = config.getTemplate(Long.toString(templateId), config.getLocale());
		} catch (IOException e) {
			throw new RuntimeException("Fail to load template.", e);
		}

		StringWriter writer = new StringWriter(templateContent.length());

		try {
			template.process(data, writer);
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return writer.toString();
	}
}
