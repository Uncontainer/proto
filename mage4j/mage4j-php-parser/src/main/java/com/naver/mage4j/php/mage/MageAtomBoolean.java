package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpAtomBoolean;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageAtomBoolean implements MageAtom, MageStatementInitializable<PhpAtomBoolean> {
	boolean value;

	MageAtomBoolean() {
		super();
	}

	@Override
	public void init(MageContext context, PhpAtomBoolean statement) {
		this.value = statement.getValue();
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.appendCode(Boolean.toString(value));
	}

	@Override
	public PhpType getType() {
		return PhpTypeFactory.BOOLEAN;
	}

	@Override
	public void setType(PhpType type) {
//		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return "ATOM_BOOL: " + value;
	}
}
