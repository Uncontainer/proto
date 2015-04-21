package com.naver.mage4j.php.code;

public class PhpExpressionTernary implements PhpExpression {
	private PhpExpression condition;
	private PhpExpression trueExpression;
	private PhpExpression falseExpression;

	public PhpExpressionTernary(PhpExpression condition, PhpExpression trueExpression, PhpExpression falseExpression) {
		super();
		this.condition = condition;
		this.trueExpression = trueExpression;
		this.falseExpression = falseExpression;
	}

	public PhpExpression getCondition() {
		return condition;
	}

	public PhpExpression getTrueExpression() {
		return trueExpression;
	}

	public PhpExpression getFalseExpression() {
		return falseExpression;
	}
}
