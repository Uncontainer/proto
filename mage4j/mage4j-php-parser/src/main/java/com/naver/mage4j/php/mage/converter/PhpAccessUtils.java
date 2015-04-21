package com.naver.mage4j.php.mage.converter;

import com.naver.mage4j.php.code.PhpAccessFunction;
import com.naver.mage4j.php.code.PhpAccessVariable;
import com.naver.mage4j.php.code.PhpExpression;

public class PhpAccessUtils {
	public static String getLiteralFunctionName(PhpAccessFunction element) {
		PhpExpression name = element.getName();
		if (name instanceof PhpAccessVariable) {
			PhpAccessVariable v = (PhpAccessVariable)name;
			if (v.isLiteral()) {
				return v.getName();
			}
		}

		return null;
	}
}
