package com.naver.mage4j.php.code;

public class PhpAccessClass extends PhpAccess {
	protected PhpExpression name;
	protected PhpExpression index;

	public PhpAccessClass(PhpExpression name, PhpExpression index) {
		super();
		this.name = name;
		this.index = index;
	}

	public PhpExpression getName() {
		return name;
	}

	public PhpExpression getIndex() {
		return index;
	}

	@Override
	public String toString() {
		return name + "." + index;
	}
}
