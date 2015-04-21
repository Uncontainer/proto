package com.naver.mage4j.php.code;

public class PhpNew implements PhpExpression {
	private PhpExpression source;

	public PhpNew(PhpExpression source) {
		super();
		this.source = source;
	}

	public PhpExpression getSource() {
		return source;
	}
}
