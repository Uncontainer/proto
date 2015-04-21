package com.naver.mage4j.php.code;


public class PhpStatementStatic implements PhpStatement {
	private PhpAccessVariable name;
	private PhpAtom value;

	public PhpStatementStatic(PhpAccessVariable name, PhpAtom value) {
		super();
		this.name = name;
		this.value = value;
	}

	public PhpAccessVariable getName() {
		return name;
	}

	public PhpAtom getValue() {
		return value;
	}
}
