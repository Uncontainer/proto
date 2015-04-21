package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpBinaryOperator;
import com.naver.mage4j.php.mage.converter.PhpTypeUtils;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageExpressionAssignment extends MageExpressionBinary {
	public MageExpressionAssignment(MageAccessElement left, PhpBinaryOperator op, MageExpression right) {
		super(left, op, right);
		getDeletateElement().setType(PhpTypeUtils.select(left.getType(), right.getType()));
	}

	@Override
	public MageExpression getDeletateElement() {
		MageExpression right = getRight();
		if (right == null) {
			return left;
		} else {
			return new MageExpressionList(left, right);
		}
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.visit(left).appendCode(" " + operations.get(0).getJava() + " ").visit(getRight());
	}

	private MageExpression getRight() {
		return rights.isEmpty() ? null : rights.get(0);
	}
}
