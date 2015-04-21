package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpStatementBreak;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageStatementBreak implements MageStatement, MageStatementInitializable<PhpStatementBreak> {
	private String label;

	MageStatementBreak() {
		super();
	}

	@Override
	public void init(MageContext context, PhpStatementBreak statement) {
		this.label = statement.getLabel();
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.appendCode("break").appendCode(label != null ? " " + label : "");
	}
}
