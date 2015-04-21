package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpStatementContinue;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageStatementContinue implements MageStatement, MageStatementInitializable<PhpStatementContinue> {
	private String label;

	MageStatementContinue() {
		super();
	}

	@Override
	public void init(MageContext context, PhpStatementContinue statement) {
		this.label = statement.getLabel();
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.appendCode("continue").appendCode(label != null ? " " + label : "");
	}
}
