package com.naver.mage4j.php.code;

public class PhpClone implements PhpExpression {
	private PhpAccess source;

	public PhpClone(PhpAccess source) {
		super();
		this.source = source;
	}

	public PhpAccess getSource() {
		return source;
	}
}
