package com.naver.mage4j.php.code;


public class PhpExpressionInstanceOf implements PhpExpression {
	private PhpExpression child;
	private PhpExpression parent;

	public PhpExpressionInstanceOf(PhpExpression child, PhpExpression parent) {
		super();
		this.child = child;
		this.parent = parent;
	}

	public PhpExpression getChild() {
		return child;
	}

	public PhpExpression getParent() {
		return parent;
	}
}
