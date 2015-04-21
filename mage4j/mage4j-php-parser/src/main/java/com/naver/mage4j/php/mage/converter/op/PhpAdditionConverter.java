package com.naver.mage4j.php.mage.converter.op;

import java.lang.reflect.Method;
import java.util.Map;

import com.naver.mage4j.php.mage.MageExpression;
import com.naver.mage4j.php.mage.converter.JavaClassUtils;
import com.naver.mage4j.php.mage.converter.access.MageAccessJavaMethod;
import com.naver.mage4j.php.mage.converter.access.MageAccessMap;

public class PhpAdditionConverter implements BinaryOperationConverter {
	@Override
	public void convert(BinaryOperationConvertContext context) {
		MageExpression left = context.getLeft();
		MageExpression right = context.getRight();

		if (left instanceof MageAccessMap && right instanceof MageAccessMap) {
			Method method = JavaClassUtils.getMethod(Map.class, "putAll", Map.class);
			context.addMerged(new MageAccessJavaMethod(left, method, right));
			return;
		}

		context.pass();
	}
}
