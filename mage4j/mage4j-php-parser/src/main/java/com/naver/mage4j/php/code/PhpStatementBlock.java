package com.naver.mage4j.php.code;

import java.util.ArrayList;
import java.util.List;

public class PhpStatementBlock implements PhpStatement {
	private final List<PhpStatement> statements;

	public PhpStatementBlock() {
		this(new ArrayList<PhpStatement>());
	}

	public PhpStatementBlock(List<PhpStatement> members) {
		statements = new ArrayList<PhpStatement>(members);
	}

	public void add(PhpStatement item) {
		statements.add(item);
	}

	public void addAll(List<PhpStatement> items) {
		this.statements.addAll(items);
	}

	public List<PhpStatement> getStatements() {
		return statements;
	}
}
