package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageAtomNull implements MageAtom {
	public static final MageAtomNull INSTANCE = new MageAtomNull();

	private MageAtomNull() {
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.appendCode("null");
	}

	@Override
	public PhpType getType() {
		return PhpTypeFactory.OBJECT;
	}

	@Override
	public void setType(PhpType type) {
//		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return "ATOM_NULL: " + null;
	}
}
