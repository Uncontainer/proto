package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpClone;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageClone extends MageExpressionTypeDelegated implements MageStatementInitializable<PhpClone> {
	private MageAccessElement source;

	MageClone() {
		super();
	}

	@Override
	public void init(MageContext context, PhpClone statement) {
		source = context.buildExpression(statement.getSource());
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		// TODO 복사 생성자 호출추가. "new " + name.getType() + "(" + name + ")";
		context.appendCode("/* [NOT-IMPLEMENT] Clone */");
	}

	@Override
	protected MageAccessElement getDeletateElement() {
		return source;
	}
}
