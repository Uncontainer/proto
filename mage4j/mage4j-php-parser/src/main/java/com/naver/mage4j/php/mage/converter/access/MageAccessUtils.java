package com.naver.mage4j.php.mage.converter.access;

import com.naver.mage4j.php.mage.MageAccessElement;
import com.naver.mage4j.php.mage.MageAccessFunction;
import com.naver.mage4j.php.mage.MageAtom;
import com.naver.mage4j.php.mage.MageExpression;

public class MageAccessUtils {
	public static boolean isLiteral(MageAccessElement element) {
		return element instanceof MageAtom;
		//		return (element.getAccessTypes() & MageAccessElement.LITERAL) > 0;
	}

	public static boolean isNotLiteral(MageAccessElement element) {
		return !isLiteral(element);
	}

	public static boolean isFunction(MageExpression element) {
		return element instanceof MageAccessFunction;

		//			return (element.getAccessTypes() & MageAccessElement.FUNCTION) > 0;
	}

	public static boolean isNotFunction(MageExpression element) {
		return !isFunction(element);
	}
}
