package com.naver.mage4j.php.mage.converter.access;

import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.mage.MageExpression;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageAccessArray extends MageAccessComposite {
	public MageAccessArray(MageExpression name, MageExpression index, PhpType type) {
		super(name, index);
		setType(type);
	}

	@Override
	public String toString() {
		return name + "[" + index + "]";
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.visit(name).appendCode("[").visit(index).appendCode("]");
	}

	//	@Override
	//	public int getAccessTypes() {
	//		return super.getAccessTypes() | ARRAY;
	//	}
	//
	//	@Override
	//	public String getNameString() {
	//		return name.getNameString();
	//	}
}
