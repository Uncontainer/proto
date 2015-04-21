package com.naver.mage4j.php.code;

public class PhpStatementRequire implements PhpStatement {
	private PhpExpression value;

	public PhpStatementRequire(PhpExpression value) {
		super();
		this.value = value;
	}

	public PhpExpression getValue() {
		return value;
	}
}
