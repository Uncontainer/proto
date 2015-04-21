package com.naver.mage4j.php.mage;

import java.util.ArrayList;
import java.util.List;

import com.naver.mage4j.php.code.PhpControlSwitch;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.mage.converter.PhpTypeUtils;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageControlSwitch implements MageControlStatement, MageStatementInitializable<PhpControlSwitch> {
	private MageExpression condition;
	private List<Case> cases;

	MageControlSwitch() {
	}

	@Override
	public void init(MageContext context, PhpControlSwitch statement) {
		this.condition = context.buildExpression(statement.getCondition());
		List<PhpControlSwitch.Case> phpCases = statement.getCases();
		this.cases = new ArrayList<Case>(phpCases.size());
		for (PhpControlSwitch.Case c : phpCases) {
			Case item;
			if (c instanceof PhpControlSwitch.DefaultCase) {
				item = new DefaultCase(context, (PhpControlSwitch.DefaultCase)c);
			} else {
				item = new Case(context, c);
				PhpType type = PhpTypeUtils.select(condition.getType(), item.label.getType());
				condition.setType(type);
				item.label.setType(type);
			}
			cases.add(item);
		}
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.appendCode("switch(");
		condition.visitJava(context);
		context.appendCode(") {").appendNewLine().increaseIndent();
		for (Case caseBlock : cases) {
			context.visit(caseBlock).appendNewLine();
		}
		context.decreaseIndent().appendCode("}");
	}

	public static class Case implements MageStatement {
		private MageExpression label;
		protected MageStatementBlock body;

		public Case(MageContext context, PhpControlSwitch.Case phpCase) {
			super();
			this.label = context.buildExpression(phpCase.getLabel());
			this.body = context.buildStatement(phpCase.getBody());
		}

		@Override
		public void visitJava(JavaCodeGenerateContext context) {
			context.appendCode("case ").visit(label).appendCode(":").appendCode(" {").appendNewLine()
				.increaseIndent().visit(body).appendNewLine()
				.decreaseIndent().appendCode("}");
		}
	}

	public static class DefaultCase extends Case {
		public DefaultCase(MageContext context, PhpControlSwitch.DefaultCase phpDefault) {
			super(context, phpDefault);
		}

		@Override
		public void visitJava(JavaCodeGenerateContext context) {
			context.appendCode("default:").appendCode(" {").appendNewLine()
				.increaseIndent().visit(body)
				.appendNewLine().decreaseIndent().appendCode("}");
		}
	}
}
