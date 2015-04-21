package com.naver.mage4j.php.code;

public class PhpExpressionNegation implements PhpExpression {
	private PhpExpression expression;
	private String operation;

	public PhpExpressionNegation(PhpExpression expression, String operation) {
		super();
		this.expression = expression;
		this.operation = operation;
	}

	public PhpExpression getExpression() {
		return expression;
	}

	public String getOperation() {
		return operation;
	}
}
