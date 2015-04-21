package com.yeon.infra.web.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Component
public class ScriptDirective implements TemplateDirectiveModel {
	@Override
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		boolean flush = TemplateModelUtil.getBooleanParam(params, BufferedTemplateWriter.PARAM_FLUSH, false);
		boolean onTheFly = TemplateModelUtil.getBooleanParam(params, BufferedTemplateWriter.PARAM_ON_THE_FLY, false);

		CharSequence content;
		if (body == null) {
			content = "";
		} else {
			StringWriter writer = new StringWriter();
			body.render(writer);
			content = writer.getBuffer();
		}

		BufferedTemplateWriter.fromEnvironment(env).appendScriptBody(content, onTheFly, flush);
	}
}