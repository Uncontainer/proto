package com.naver.mage4j.php.code;

public class PhpStatementReturn implements PhpStatement {
	private PhpExpression value;

	public PhpStatementReturn(PhpExpression value) {
		super();
		this.value = value;
	}

	public PhpExpression getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "return " + value;
	}
}
