package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpExpressionIncrment;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageExpressionIncrement extends MageExpressionTypeDelegated implements MageStatementInitializable<PhpExpressionIncrment> {
	private PhpExpressionIncrment original;

	private MageExpression expression;

	MageExpressionIncrement() {
		super();
	}

	@Override
	public void init(MageContext context, PhpExpressionIncrment statement) {
		this.original = statement;
		this.expression = context.buildExpression(statement.getExpression());
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		if (original.isPrecede()) {
			context.appendCode(original.getOp()).appendCode("(").visit(expression).appendCode(")");
		} else {
			context.appendCode("(").visit(expression).appendCode(")").appendCode(original.getOp());
		}
	}

	@Override
	public MageExpression getDeletateElement() {
		return expression;
	}
}
