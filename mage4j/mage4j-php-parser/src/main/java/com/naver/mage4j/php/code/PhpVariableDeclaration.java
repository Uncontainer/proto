package com.naver.mage4j.php.code;

import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;

public class PhpVariableDeclaration implements PhpStatement {
	private PhpType type;
	private PhpAccessVariable name;
	private PhpExpression defaultValue;

	public PhpVariableDeclaration(PhpType type, PhpAccessVariable name) {
		super();
		this.type = type;
		this.name = name;
	}

	public PhpType getType() {
		return type != null ? type : PhpTypeFactory.UNDECIDED;
	}

	public PhpAccessVariable getName() {
		return name;
	}

	public PhpExpression getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(PhpExpression defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public String toString() {
		return type + " " + name + "=" + defaultValue;
	}
}
