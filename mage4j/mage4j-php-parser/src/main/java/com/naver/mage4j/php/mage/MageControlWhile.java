package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpControlWhile;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageControlWhile implements MageControlStatement, MageStatementInitializable<PhpControlWhile> {
	private MageExpression condition;
	private MageStatement body;
	private boolean doWhile;

	MageControlWhile() {
		super();
	}

	@Override
	public void init(MageContext context, PhpControlWhile statement) {
		this.condition = context.buildExpression(statement.getCondition());
		this.body = context.buildStatement(statement.getBody());
		this.doWhile = statement.isDoWhile();
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		if (doWhile) {
			context.appendCode("do {").appendNewLine()
				.increaseIndent().visit(body).appendNewLine()
				.decreaseIndent().appendCode("} while(").visit(condition).appendCode(");");
		} else {
			context.appendCode("while(").visit(condition).appendCode(") {").appendNewLine()
				.increaseIndent().visit(body)
				.decreaseIndent().appendCode("}");
		}
	}
}
