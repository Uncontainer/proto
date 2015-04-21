package com.naver.mage4j.php.code;

public class PhpControlForeach implements PhpControlStatement {
	private PhpExpression target;
	private PhpStatement variable;
	private PhpStatement body;

	public PhpControlForeach(PhpExpression target, PhpStatement variable, PhpStatement body) {
		super();
		this.target = target;
		this.variable = variable;
		this.body = body;
	}

	public PhpExpression getTarget() {
		return target;
	}

	public PhpStatement getVariable() {
		return variable;
	}

	public PhpStatement getBody() {
		return body;
	}

	@Override
	public String toString() {
		return "for(" + variable + " : " + target + ")";
	}
}
