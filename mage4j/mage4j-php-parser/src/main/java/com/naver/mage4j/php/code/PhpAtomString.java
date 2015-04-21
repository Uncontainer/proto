package com.naver.mage4j.php.code;

public class PhpAtomString implements PhpAtom {
	String value;

	public PhpAtomString(String value, boolean quotationRemoved) {
		super();
		if (!quotationRemoved) {
			value = value.substring(1, value.length() - 1);
		}
		value = value.replace("\\", "\\\\");
		value = value.replace("\"", "\\\"");

		this.value = value;
	}

	@Override
	public String toString() {
		return "ATOM_STRING: " + value;
	}

	public String getValue() {
		return value;
	}
}
