package com.naver.fog.ui.renderer;

import java.util.Map;

import com.naver.fog.ui.template.Template;
import com.naver.fog.web.ViewMode;

public class FogRenderer implements Renderer {
	private final Template template;
	private final ViewMode mode;

	public FogRenderer(Template template, ViewMode mode) {
		super();
		this.template = template;
		this.mode = mode;
	}

	public String render(Map<Long, Object> data) {
		StringBuilder builder = new StringBuilder();

		String templateContent = template.getTemplateContent(mode);
		char[] charArray = templateContent.toCharArray();
		int index = 0;
		scan: // 
		while (index < charArray.length) {
			if (charArray[index] == '$') {
				if (index + 1 < charArray.length && charArray[index + 1] == '{') {
					int beginIndex = index + 2;
					for (int endIndex = beginIndex; endIndex < charArray.length; endIndex++) {
						if (charArray[endIndex] == '}') {
							String varExpr = new String(charArray, beginIndex, endIndex - beginIndex);
							builder.append(toString(varExpr, data));

							index = endIndex + 1;
							continue scan;
						}
					}
				}
			}
			builder.append(charArray[index]);
			index++;
		}

		return builder.toString();
	}

	private String toString(String variableExpression, Map<Long, Object> data) {
		long key;
		int sepIndex = variableExpression.indexOf('!');
		if (sepIndex > 0) {
			key = Long.parseLong(variableExpression.substring(0, sepIndex));
		} else {
			key = Long.parseLong(variableExpression);
		}
		Object value = data.get(key);
		if (value == null) {
			return "";
		}

		return value.toString();
	}
}
