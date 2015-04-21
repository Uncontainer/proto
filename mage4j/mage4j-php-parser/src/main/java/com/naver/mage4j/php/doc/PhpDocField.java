package com.naver.mage4j.php.doc;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.php.lang.PhpType;

public class PhpDocField extends PhpDocElementAbstract {
	private List<String> types;

	public PhpDocField(String str) {
		super(str);
	}

	@Override
	protected void parse(String type, String body) {
		switch (type) {
			case "var":
				body = body.trim().replaceAll("(\\s)*\\|(\\s)*", "\\|");
				String[] tokens = body.split("\\s");
				types = Arrays.asList(StringUtils.split(tokens[0], "|"));
				break;
		}
	}

	@Override
	public boolean isBlank() {
		return CollectionUtils.isEmpty(types) && super.isBlank();
	}

	public PhpType getType() {
		return getType(types);
	}

	public static void main(String[] args) {
		String doc = "/**\n" +
			" * Helper module name\n" +
			" *\n" +
			" * @var string\n" +
			" */\n";

		PhpDocField field = new PhpDocField(doc);
		System.out.println(field);
	}
}
