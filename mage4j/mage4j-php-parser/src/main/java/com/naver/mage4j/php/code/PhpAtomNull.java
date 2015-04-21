package com.naver.mage4j.php.code;


public class PhpAtomNull implements PhpAtom {
	public static final PhpAtomNull INSTANCE = new PhpAtomNull();

	private PhpAtomNull() {
	}

	@Override
	public String toString() {
		return "ATOM_NULL: " + null;
	}
}
