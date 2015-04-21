package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageAccessConstant extends MageExpressionAbstract implements MageAccessElement {
	protected String value;

	public MageAccessConstant(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.appendCode(value);
	}

	@Override
	public String toString() {
		return value;
	}

//	@Override
//	public int getAccessTypes() {
//		return LITERAL;
//	}
//
//	@Override
//	public String getNameString() {
//		return getValue();
//	}
}
