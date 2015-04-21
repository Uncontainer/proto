package com.naver.mage4j.php.code;

public class PhpControlWhile implements PhpControlStatement {
	private PhpExpression condition;
	private PhpStatement body;
	private boolean doWhile;

	public PhpControlWhile(PhpExpression condition, PhpStatement body, boolean doWhile) {
		super();
		this.condition = condition;
		this.body = body;
		this.doWhile = doWhile;
	}

	public PhpExpression getCondition() {
		return condition;
	}

	public PhpStatement getBody() {
		return body;
	}

	public boolean isDoWhile() {
		return doWhile;
	}
}
