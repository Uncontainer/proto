package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpExpressionNegation;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageExpressionNegation extends MageExpressionTypeDelegated implements MageExpression, MageStatementInitializable<PhpExpressionNegation> {
	private MageExpression expression;
	private String operation;

	public MageExpressionNegation(MageExpression expression, String operation) {
		super();
		this.expression = expression;
		this.operation = operation;
	}

	MageExpressionNegation() {
		super();
	}

	@Override
	public void init(MageContext context, PhpExpressionNegation statement) {
		this.operation = statement.getOperation();
		this.expression = context.buildExpression(statement.getExpression());
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.appendCode(operation).visit(expression);
	}

	@Override
	protected MageExpression getDeletateElement() {
		return expression;
	}
}
