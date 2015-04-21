package com.naver.mage4j.php.code;

public abstract class PhpAccess implements PhpExpression {
	private boolean reference = false;

	public boolean isReference() {
		return reference;
	}

	public void setReference(boolean reference) {
		this.reference = reference;
	}
}
