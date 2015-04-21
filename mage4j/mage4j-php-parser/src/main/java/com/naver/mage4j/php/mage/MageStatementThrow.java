package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpStatementThrow;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageStatementThrow implements MageStatement, MageStatementInitializable<PhpStatementThrow> {
	private MageExpression exception;

	public MageStatementThrow() {
		super();
	}

	@Override
	public void init(MageContext context, PhpStatementThrow statement) {
		this.exception = context.buildExpression(statement.getException());
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.addImport(exception.getType());
		context.appendCode("throw ").visit(exception);
	}
}
