package com.naver.mage4j.php.mage.converter.access;

import com.naver.mage4j.php.mage.MageExpression;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageAccessMap extends MageAccessComposite {
	public MageAccessMap(MageExpression name, MageExpression index) {
		super(name, index);
	}

	@Override
	public String toString() {
		return name + ".get(" + index + ")";
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.visit(name).appendCode(".get(").visit(index).appendCode(")");
	}

	//	@Override
	//	public int getAccessTypes() {
	//		return super.getAccessTypes() | MAP;
	//	}
}
