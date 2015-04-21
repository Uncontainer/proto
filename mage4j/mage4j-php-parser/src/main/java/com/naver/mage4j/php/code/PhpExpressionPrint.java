package com.naver.mage4j.php.code;


public class PhpExpressionPrint implements PhpExpression {
	private PhpExpression value;

	public PhpExpressionPrint(PhpExpression value) {
		super();
		this.value = value;
	}

	public PhpExpression getValue() {
		return value;
	}
}
