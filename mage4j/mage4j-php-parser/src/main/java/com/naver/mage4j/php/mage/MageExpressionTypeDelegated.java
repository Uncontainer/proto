package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public abstract class MageExpressionTypeDelegated implements MageExpression {
	protected abstract MageExpression getDeletateElement();

	@Override
	public final PhpType getType() {
		return getDeletateElement().getType();
	}

	@Override
	public void setType(PhpType type) {
		getDeletateElement().setType(type);
	}

	protected static class MageExpressionList implements MageExpression {
		MageExpression[] expressions;

		public MageExpressionList(MageExpression... expressions) {
			super();
			this.expressions = expressions;
		}

		@Override
		public void visitJava(JavaCodeGenerateContext context) {
			throw new UnsupportedOperationException();
		}

		@Override
		public PhpType getType() {
			return expressions[0].getType();
		}

		@Override
		public void setType(PhpType type) {
			for (MageExpression expression : expressions) {
				expression.setType(type);
			}
		}
	}
}
