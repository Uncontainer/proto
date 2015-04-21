package com.naver.mage4j.php.doc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public abstract class PhpDocElementAbstract extends PhpDocElement {
	public PhpDocElementAbstract(String str) {
		str = str.trim();
		if (!(str.startsWith("/**") && str.endsWith("*/"))) {
			throw new IllegalArgumentException();
		}

		str = str.substring(3, str.length() - 3);

		String[] lines = StringUtils.split(str, "\n");
		String type = "description";
		String body = "";
		for (String line : lines) {
			line = line.trim();
			if ("/**".equals(line)) {
				line = line.substring(3).trim();
				if (!line.isEmpty()) {
					body += line + "\n";
				}
				continue;
			} else if ("*/".equals(line)) {
				break;
			}

			if (line.startsWith("*")) {
				line = line.substring(1).trim();
			} else {
				if (body.isEmpty()) {
					body += "\n";
				}
				body += line + "\n";
				continue;
			}

			if (line.isEmpty()) {
				body += "";
				continue;
			}

			Pattern pattern = Pattern.compile("^(\\s)*@([^\\s]+)(.*)");
			Matcher matcher = pattern.matcher(line);
			if (matcher.matches()) {
				_parse(type, body);

				type = matcher.group(2);
				if (matcher.groupCount() >= 3) {
					body = matcher.group(3);
				} else {
					body = "";
				}
			} else {
				if (body.isEmpty()) {
					body += "\n";
				}
				body += line + "\n";
			}
		}

		_parse(type, body);
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append("/**").append("\n");
		for (String line : StringUtils.split(description, "\n")) {
			out.append(" * ").append(line).append("\n");
		}
		out.append(" */");

		return out.toString();
	}

	private void _parse(String type, String body) {
		if ("description".equals(type)) {
			description = body;
		} else {
			parse(type, body);
		}
	}

	protected abstract void parse(String type, String body);
}
