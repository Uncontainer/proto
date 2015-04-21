package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpAtomString;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageAtomString implements MageAtom, MageStatementInitializable<PhpAtomString> {
	String value;

	public MageAtomString(String value) {
		this.value = value;
	}

	MageAtomString() {
	}

	@Override
	public void init(MageContext context, PhpAtomString statement) {
		this.value = statement.getValue();
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.appendCode("\"").appendCode(value).appendCode("\"");
	}

	@Override
	public PhpType getType() {
		return PhpTypeFactory.STRING;
	}

	@Override
	public String toString() {
		return "ATOM_STRING: " + value;
	}

	@Override
	public void setType(PhpType type) {
		//		throw new UnsupportedOperationException();
	}

	public String getValue() {
		return value;
	}
}
