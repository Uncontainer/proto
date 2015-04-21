package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpStatementReturn;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageStatementReturn implements MageStatement, MageStatementInitializable<PhpStatementReturn> {
	private MageExpression value;

	MageStatementReturn() {
		super();
	}

	@Override
	public void init(MageContext context, PhpStatementReturn statement) {
		this.value = context.buildExpression(statement.getValue());
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.appendCode("return ").visit(value);
	}

	public PhpType getReturnType() {
		return value != null ? value.getType() : PhpTypeFactory.VOID;
	}

	@Override
	public String toString() {
		return "return " + value;
	}
}
