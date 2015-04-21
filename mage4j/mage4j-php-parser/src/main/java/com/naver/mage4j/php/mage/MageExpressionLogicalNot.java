package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpExpressionLogicalNot;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageExpressionLogicalNot implements MageExpression, MageStatementInitializable<PhpExpressionLogicalNot> {
	private MageExpression expression;

	MageExpressionLogicalNot() {
		super();
	}

	@Override
	public void init(MageContext context, PhpExpressionLogicalNot statement) {
		this.expression = context.buildExpression(statement.getExpression());
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.appendCode("!").visit(expression, "(", ")");
	}

	@Override
	public PhpType getType() {
		return PhpTypeFactory.BOOLEAN;
	}

	@Override
	public void setType(PhpType type) {
//		throw new UnsupportedOperationException();
	}
}
