package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpExpressionInstanceOf;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageExpressionInstanceOf implements MageExpression, MageStatementInitializable<PhpExpressionInstanceOf> {
	private MageExpression child;
	private MageExpression parent;

	public MageExpressionInstanceOf() {
		super();
	}

	@Override
	public void init(MageContext context, PhpExpressionInstanceOf statement) {
		child = context.buildExpression(statement.getChild());
		parent = context.buildExpression(statement.getParent());
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.visit(child).appendCode(" instanceof ").visit(parent);
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
