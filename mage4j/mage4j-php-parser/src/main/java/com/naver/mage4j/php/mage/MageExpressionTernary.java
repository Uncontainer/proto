package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpExpressionTernary;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageExpressionTernary implements MageExpression, MageStatementInitializable<PhpExpressionTernary> {
	private MageExpression condition;
	private MageExpression trueExpression;
	private MageExpression falseExpression;

	MageExpressionTernary() {
		super();
	}

	@Override
	public void init(MageContext context, PhpExpressionTernary statement) {
		this.condition = context.buildExpression(statement.getCondition());
		this.trueExpression = context.buildExpression(statement.getTrueExpression());
		this.falseExpression = context.buildExpression(statement.getFalseExpression());
	}

	@Override
	public PhpType getType() {
		if (trueExpression != null) {
			return trueExpression.getType();
		} else if (falseExpression != null) {
			return falseExpression.getType();
		} else {
			return PhpTypeFactory.UNDECIDED;
		}
	}

	@Override
	public void setType(PhpType type) {
		if (trueExpression != null) {
			trueExpression.setType(type);
		} else if (falseExpression != null) {
			falseExpression.setType(type);
		}
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.appendCode("(").visit(condition).appendCode(") ? ");
		if (trueExpression != null) {
			context.appendCode("(").visit(trueExpression).appendCode(")");
		} else {
			context.appendCode("null");
		}
		context.appendCode(" : ");
		if (falseExpression != null) {
			context.appendCode("(").visit(falseExpression).appendCode(")");
		} else {
			context.appendCode("null");
		}
	}
}
