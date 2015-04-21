package com.naver.mage4j.php.mage;

import java.util.List;

import com.naver.mage4j.php.code.PhpControlIf;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageControlIf implements MageControlStatement, MageStatementInitializable<PhpControlIf> {
	private MageExpression condition;
	private MageStatement trueBlock;
	private List<MageControlIf> conditionals;
	private MageStatement elseBlock;

	MageControlIf() {
		super();
	}

	@Override
	public void init(MageContext context, PhpControlIf statement) {
		this.condition = context.buildExpression(statement.getCondition());
		this.trueBlock = context.buildStatement(statement.getTrue());
		this.conditionals = context.buildStatements(statement.getConditionals());
		this.elseBlock = context.buildStatement(statement.getElse());
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		visitIf(this, context);
		if (conditionals != null) {
			for (MageControlIf conditional : conditionals) {
				context.appendCode(" else ");
				visitIf(conditional, context);
			}
		}
		if (elseBlock != null) {
			context.appendCode(" else {").appendNewLine()
				.increaseIndent().visit(elseBlock)
				.appendNewLine().decreaseIndent().appendCode("}");
		}
	}

	private static void visitIf(MageControlIf conditional, JavaCodeGenerateContext context) {
		context.appendCode("if (").visit(conditional.condition).appendCode(") {").appendNewLine()
			.increaseIndent().visit(conditional.trueBlock)
			.appendNewLine().decreaseIndent().appendCode("}");
	}
}
