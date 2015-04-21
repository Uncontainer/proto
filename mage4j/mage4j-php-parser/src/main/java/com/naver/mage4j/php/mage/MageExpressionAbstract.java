package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.lang.PhpType;

public abstract class MageExpressionAbstract implements MageExpression {
	private PhpType type;
	protected int typeChangeCount;

	@Override
	public PhpType getType() {
		return type;
	}

	@Override
	public void setType(PhpType type) {
		typeChangeCount++;
		this.type = type;
	}

	protected int getTypeChangeCount() {
		return typeChangeCount;
	}
}
