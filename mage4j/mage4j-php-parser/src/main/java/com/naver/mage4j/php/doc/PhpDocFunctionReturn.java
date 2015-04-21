package com.naver.mage4j.php.doc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.php.lang.PhpType;

public class PhpDocFunctionReturn extends PhpDocElement {
	protected List<String> types = new ArrayList<String>();

	public PhpDocFunctionReturn(String line) {
		line = line.trim().replaceAll("(\\s)*\\|(\\s)*", "\\|");
		String[] tokens = line.split("\\s");

		types = Arrays.asList(StringUtils.split(tokens[0], "|"));
		if (tokens.length > 1) {
			description = line.substring(tokens[0].length());
		}
	}

	@Override
	public boolean isBlank() {
		return CollectionUtils.isEmpty(types) && super.isBlank();
	}

	public PhpType getType() {
		return getType(types);
	}

	@Override
	public String toString() {
		return StringUtils.join(types, "|") + (description != null ? " " + description : "");
	}
}
