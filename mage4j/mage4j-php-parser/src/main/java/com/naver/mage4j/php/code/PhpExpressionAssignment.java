package com.naver.mage4j.php.code;

public class PhpExpressionAssignment extends PhpExpressionBinary {
	public PhpExpressionAssignment(PhpAccess left, String op, PhpExpression right) {
		super(left, PhpBinaryOperator.fromPhpOperator(op), right);
	}
}
