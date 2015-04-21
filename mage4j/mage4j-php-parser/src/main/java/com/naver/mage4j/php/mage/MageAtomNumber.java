package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpAtomNumber;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageAtomNumber extends MageExpressionAbstract implements MageAtom, MageStatementInitializable<PhpAtomNumber> {
	private String value;

	MageAtomNumber() {
	}

	public MageAtomNumber(int value) {
		this.value = Integer.toString(value);
		setType(PhpTypeFactory.INTEGER);
	}

	@Override
	public void init(MageContext context, PhpAtomNumber statement) {
		this.value = statement.getValue();
		setType(PhpTypeFactory.INTEGER);
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.appendCode(value);
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "ATOM_NUMBER: " + value;
	}
}
