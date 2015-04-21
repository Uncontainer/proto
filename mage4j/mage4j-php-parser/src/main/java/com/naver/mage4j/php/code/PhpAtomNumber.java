package com.naver.mage4j.php.code;

import com.naver.mage4j.php.lang.PhpType;

public class PhpAtomNumber extends PhpExpressionAbstract implements PhpAtom {
	private String value;

	public PhpAtomNumber(String sign, String value, PhpType type) {
		if (sign != null && "-".equals(sign)) {
			value = sign + value;
		}

		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "ATOM_NUMBER: " + value;
	}
}
