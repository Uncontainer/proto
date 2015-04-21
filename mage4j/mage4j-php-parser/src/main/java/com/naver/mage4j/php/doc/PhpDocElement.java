package com.naver.mage4j.php.doc;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;

public abstract class PhpDocElement {
	protected String description;

	protected static PhpType getType(List<String> types) {
		if (CollectionUtils.isEmpty(types)) {
			return null;
		} else {
			return PhpTypeFactory.get(types.get(0));
		}
	}

	public boolean isBlank() {
		return StringUtils.isBlank(description);
	}
}
