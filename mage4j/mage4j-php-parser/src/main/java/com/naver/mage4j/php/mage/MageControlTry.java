package com.naver.mage4j.php.mage;

import java.util.ArrayList;
import java.util.List;

import com.naver.mage4j.php.code.PhpControlTry;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageControlTry implements MageControlStatement, MageStatementInitializable<PhpControlTry> {
	private MageStatementBlock main;
	private List<Catch> catches;

	MageControlTry() {
		super();
	}

	@Override
	public void init(MageContext context, PhpControlTry statement) {
		main = context.buildStatement(statement.getMain());
		List<PhpControlTry.Catch> phpCatches = statement.getCatches();
		catches = new ArrayList<MageControlTry.Catch>(phpCatches.size());
		for (PhpControlTry.Catch phpCatch : phpCatches) {
			catches.add(new Catch(context, phpCatch));
		}
	}

	public void addCatch(Catch c) {
		catches.add(c);
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.appendCode("try {").appendNewLine()
			.increaseIndent().visit(main).appendNewLine()
			.decreaseIndent().appendCode("} ");
		for (Catch catchBlock : catches) {
			context.visit(catchBlock).appendNewLine();
		}
	}

	private static class Catch implements MageStatement {
		private PhpType type;
		private MageAccessVariable variable;
		private MageStatementBlock body;

		public Catch(MageContext context, PhpControlTry.Catch phpCatch) {
			super();
			this.type = phpCatch.getType();
			this.variable = context.buildExpression(phpCatch.getVariable(), type);
			this.body = context.buildStatement(phpCatch.getBody());
		}

		@Override
		public void visitJava(JavaCodeGenerateContext context) {
			context.appendCode(" catch (").visit(type).appendCode(" ").visit(variable).appendCode(") {").appendNewLine()
				.increaseIndent().visit(body).appendNewLine()
				.decreaseIndent().appendCode("}");
		}
	}
}
