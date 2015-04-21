package com.naver.mage4j.php.mage;

import java.util.List;

import com.naver.mage4j.php.code.PhpStatementBlock;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageStatementBlock implements MageStatement, MageStatementInitializable<PhpStatementBlock> {
	private List<MageStatement> statements;

	MageStatementBlock() {
	}

	@Override
	public void init(MageContext context, PhpStatementBlock statement) {
		this.statements = context.buildStatements(statement.getStatements());
	}

	public void addAll(List<MageStatement> items) {
	}

	public void addAll(int position, List<? extends MageStatement> items) {
		this.statements.addAll(position, items);
	}

	public List<MageStatement> getStatements() {
		return statements;
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		if (statements != null) {
			for (int i = 0; i < statements.size(); i++) {
				MageStatement statement = statements.get(i);
				if (i > 0) {
					context.appendNewLine();
				}
				context.visit(statement);
				if (!(statement instanceof MageControlStatement)) {
					context.appendCode(";");
				} else {
					context.appendNewLine();
				}
			}
		}
	}
}
