package com.naver.mage4j.php.code;


public class PhpPair implements PhpStatement {
	private PhpExpression key;
	private PhpExpression value;

	public PhpPair(PhpExpression key, PhpExpression value) {
		super();
		this.key = key;
		this.value = value;
	}

	public PhpExpression getKey() {
		return key;
	}

	public void setKey(PhpExpression key) {
		this.key = key;
	}

	public PhpExpression getValue() {
		return value;
	}

	public void setValue(PhpExpression value) {
		this.value = value;
	}
}
