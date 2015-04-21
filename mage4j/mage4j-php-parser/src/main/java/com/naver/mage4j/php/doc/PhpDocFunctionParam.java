package com.naver.mage4j.php.doc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.php.lang.PhpType;

public class PhpDocFunctionParam extends PhpDocElement {
	protected String name;
	protected List<String> types = new ArrayList<String>();

	public PhpDocFunctionParam(String line) {
		line = line.trim().replaceAll("(\\s)*\\|(\\s)*", "\\|");
		String[] tokens = line.split("\\s");

		types = Arrays.asList(StringUtils.split(tokens[0], "|"));
		if (tokens.length > 1) {
			name = tokens[1];
			name = name.replaceAll("^[$&]+", "");
		}
		if (tokens.length > 2) {
			for (int i = 2; i < tokens.length; i++) {
				description = tokens[i] + " ";
			}
		}
	}

	@Override
	public boolean isBlank() {
		return StringUtils.isBlank(name) && CollectionUtils.isEmpty(types) && super.isBlank();
	}

	public PhpType getType() {
		return getType(types);
	}

	@Override
	public String toString() {
		return name + (description != null ? " " + description : "");
	}
}
