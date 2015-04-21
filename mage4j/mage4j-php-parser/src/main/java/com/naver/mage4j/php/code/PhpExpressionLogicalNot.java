package com.naver.mage4j.php.code;

public class PhpExpressionLogicalNot implements PhpExpression {
	private PhpExpression expression;

	public PhpExpressionLogicalNot(PhpExpression expression) {
		super();
		this.expression = expression;
	}

	public PhpExpression getExpression() {
		return expression;
	}
}
