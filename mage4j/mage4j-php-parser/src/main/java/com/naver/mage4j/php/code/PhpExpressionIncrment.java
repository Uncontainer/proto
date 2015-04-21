package com.naver.mage4j.php.code;


public class PhpExpressionIncrment implements PhpExpression {
	private PhpExpression expression;
	private String op;
	private boolean precede;

	public PhpExpressionIncrment(PhpExpression expression, String op, boolean precede) {
		super();
		this.expression = expression;
		this.op = op;
		this.precede = precede;
	}

	public PhpExpression getExpression() {
		return expression;
	}

	public String getOp() {
		return op;
	}

	public boolean isPrecede() {
		return precede;
	}

}
