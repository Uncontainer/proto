package com.yeon.infra.web.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class ResourceDirective implements TemplateDirectiveModel {
	@Override
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		boolean flush;
		boolean onTheFly;

		String resourceUrl = TemplateModelUtil.getStringParam(params, BufferedTemplateWriter.PARAM_RESOURCE_URL, "");
		if (resourceUrl.endsWith(".js")) {
			flush = TemplateModelUtil.getBooleanParam(params, BufferedTemplateWriter.PARAM_FLUSH, false);
			onTheFly = TemplateModelUtil.getBooleanParam(params, BufferedTemplateWriter.PARAM_ON_THE_FLY, false);
		} else {
			flush = TemplateModelUtil.getBooleanParam(params, BufferedTemplateWriter.PARAM_FLUSH, true);
			onTheFly = TemplateModelUtil.getBooleanParam(params, BufferedTemplateWriter.PARAM_ON_THE_FLY, true);
		}

		BufferedTemplateWriter.fromEnvironment(env).appendResourceUrl(resourceUrl, onTheFly, flush);
	}
}