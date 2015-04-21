package com.naver.mage4j.php.mage.converter.op;

import java.lang.reflect.Method;
import java.util.Map;

import com.naver.mage4j.php.mage.MageAccessElement;
import com.naver.mage4j.php.mage.MageExpression;
import com.naver.mage4j.php.mage.MageExpressionAssignment;
import com.naver.mage4j.php.mage.converter.JavaClassUtils;
import com.naver.mage4j.php.mage.converter.access.MageAccessJavaMethod;
import com.naver.mage4j.php.mage.converter.access.MageAccessMap;

public class PhpAssignmentConverter implements BinaryOperationConverter {
	@Override
	public void convert(BinaryOperationConvertContext context) {
		MageExpression left = context.getLeft();
		MageExpression right = context.getRight();

		if (left instanceof MageAccessMap) {
			MageAccessMap accessMap = (MageAccessMap)left;
			Method method = JavaClassUtils.getMethod(Map.class, "put", Object.class, Object.class);
			MageAccessJavaMethod merged = new MageAccessJavaMethod(accessMap.getName(), method, accessMap.getIndex(), right);
			context.addMerged(merged);
		} else {
			left.setType(right.getType());

			MageExpressionAssignment merged = new MageExpressionAssignment((MageAccessElement)left, context.getOperation(), right);
			context.addMerged(merged);
		}
	}
}
