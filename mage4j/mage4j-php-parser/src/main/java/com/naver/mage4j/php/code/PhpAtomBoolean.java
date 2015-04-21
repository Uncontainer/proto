package com.naver.mage4j.php.code;

public class PhpAtomBoolean implements PhpAtom {
	boolean value;

	public PhpAtomBoolean(String value) {
		super();
		this.value = Boolean.valueOf(value);
	}

	public boolean getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "ATOM_BOOL: " + value;
	}
}
