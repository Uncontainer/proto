package com.naver.mage4j.php.code;

public class PhpStatementThrow implements PhpStatement {
	private PhpExpression exception;

	public PhpStatementThrow(PhpExpression exception) {
		super();
		this.exception = exception;
	}

	public PhpExpression getException() {
		return exception;
	}
}
