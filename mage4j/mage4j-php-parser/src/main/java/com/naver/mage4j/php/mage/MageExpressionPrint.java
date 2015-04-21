package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpExpressionPrint;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageExpressionPrint implements MageExpression, MageStatementInitializable<PhpExpressionPrint> {
	private MageExpression value;

	MageExpressionPrint() {
	}

	@Override
	public void init(MageContext context, PhpExpressionPrint statement) {
		this.value = context.buildExpression(statement.getValue());
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		// TODO Auto-generated method stub
		context.appendCode("/* [NOT-IMPLEMENT] PhpExpressionPrint */");
	}

	@Override
	public PhpType getType() {
		return PhpTypeFactory.VOID;
	}

	@Override
	public void setType(PhpType type) {
		throw new UnsupportedOperationException();
	}
}
