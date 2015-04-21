package com.naver.mage4j.php.mage.converter.access;

import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.MageAccessConstant;
import com.naver.mage4j.php.mage.MageExpression;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageAccessClass extends MageAccessComposite {
	public MageAccessClass(MageExpression name, MageExpression index) {
		super(name, index);
	}

	@Override
	public String toString() {
		return name + "." + index;
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		if (name instanceof MageAccessConstant) {
			String className = ((MageAccessConstant)name).getValue();
			if (!"self".equals(className)) {
				context.addImport(PhpTypeFactory.get(className));
				context.visit(name).appendCode(".");
			}
		} else {
			context.visit(name).appendCode(".");
		}

		context.visit(index);
	}

	@Override
	public PhpType getType() {
		return index.getType();
	}

	@Override
	public void setType(PhpType type) {
		index.setType(type);
	}
	//
	//	@Override
	//	public int getAccessTypes() {
	//		return super.getAccessTypes() | CLASS;
	//	}
}
