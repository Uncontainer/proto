package com.naver.mage4j.php;

import java.util.List;

import com.naver.mage4j.php.code.PhpProgram;
import com.naver.mage4j.php.mage.MageContext;
import com.naver.mage4j.php.mage.MageStatement;
import com.naver.mage4j.php.mage.MageStatementInitializable;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageProgram implements MageStatement, MageStatementInitializable<PhpProgram> {
	private List<MageStatement> statements;

	MageProgram() {
		super();
	}

	@Override
	public void init(MageContext context, PhpProgram program) {
		statements = context.buildStatements(program.getStatements());
	}

	public List<MageStatement> getStatements() {
		return statements;
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		for (MageStatement statement : statements) {
			context.visit(statement).appendNewLine().appendNewLine();
		}
	}
}
