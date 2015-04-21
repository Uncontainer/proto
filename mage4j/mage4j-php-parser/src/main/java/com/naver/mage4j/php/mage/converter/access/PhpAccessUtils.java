//package com.naver.mage4j.php.mage.converter.access;
//
//import com.naver.mage4j.php.code.PhpAccessChain;
//import com.naver.mage4j.php.code.PhpAccessElement;
//import com.naver.mage4j.php.code.PhpAccessExpression;
//import com.naver.mage4j.php.code.PhpExpression;
//
//public class PhpAccessUtils {
//	public static boolean isLiteral(PhpAccessElement element) {
//		return (element.getAccessTypes() & PhpAccessElement.LITERAL) > 0;
//	}
//
//	public static boolean isNotLiteral(PhpAccessElement element) {
//		return !isLiteral(element);
//	}
//
//	public static boolean isFunction(PhpAccessElement element) {
//		return (element.getAccessTypes() & PhpAccessElement.FUNCTION) > 0;
//	}
//
//	public static boolean isNotFunction(PhpAccessElement element) {
//		return !isFunction(element);
//	}
//
//	public static PhpAccessElement toAccessElement(PhpExpression expr) {
//		if (expr == null) {
//			return null;
//		}
//
//		if (expr instanceof PhpAccessElement) {
//			return (PhpAccessElement)expr;
//		}
//
//		if (expr instanceof PhpAccessChain) {
//			return ((PhpAccessChain)expr).getConvertedResult();
//		}
//
//		return new PhpAccessExpression(expr);
//	}
//}
