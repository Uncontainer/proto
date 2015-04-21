package com.naver.mage4j.php.mage;

import java.util.List;

import com.naver.mage4j.php.code.PhpControlFor;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageControlFor implements MageControlStatement, MageStatementInitializable<PhpControlFor> {
	private List<MageExpression> init;
	private List<MageExpression> condition;
	private List<MageExpression> update;
	private MageStatement body;

	MageControlFor() {
		super();
	}

	@Override
	public void init(MageContext context, PhpControlFor statement) {
		this.init = context.buildStatements(statement.getInit());
		this.condition = context.buildStatements(statement.getCondition());
		this.update = context.buildStatements(statement.getUpdate());
		this.body = context.buildStatement(statement.getBody());
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.appendCode("for(").visit(init, null, null, ",").appendCode("; ")
			.visit(condition, null, null, ",").appendCode("; ")
			.visit(update, null, null, ",").appendCode(") {").appendNewLine()
			.increaseIndent().visit(body)
			.appendNewLine().decreaseIndent().appendCode("}");
	}
}
