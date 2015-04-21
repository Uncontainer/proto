package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpStatementRequire;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageStatementRequire implements MageStatement, MageStatementInitializable<PhpStatementRequire> {
	private MageExpression value;

	MageStatementRequire() {
		super();
	}

	@Override
	public void init(MageContext context, PhpStatementRequire statement) {
		this.value = context.buildExpression(statement.getValue());
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		// TODO import문 추가.
		context.appendCode("require ").visit(value);
		//		throw new UnsupportedOperationException();
	}
}
