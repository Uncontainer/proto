package com.naver.mage4j.php.mage;

import java.util.List;

import com.naver.mage4j.php.code.PhpStatementGlobal;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageStatementGlobal implements MageStatement, MageStatementInitializable<PhpStatementGlobal> {
	private List<MageAccessElement> names;

	MageStatementGlobal() {
	}

	@Override
	public void init(MageContext context, PhpStatementGlobal statement) {
		this.names = context.buildStatements(statement.getNames());
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		throw new UnsupportedOperationException();
	}
}
