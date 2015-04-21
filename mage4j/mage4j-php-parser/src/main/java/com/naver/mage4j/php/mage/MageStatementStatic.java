package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpStatementStatic;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageStatementStatic implements MageStatement, MageStatementInitializable<PhpStatementStatic> {
	private MageAccessVariable name;
	private MageAtom value;

	MageStatementStatic() {
		super();
	}

	@Override
	public void init(MageContext context, PhpStatementStatic statement) {
		this.name = context.buildExpression(statement.getName());
		this.value = context.buildStatement(statement.getValue());
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.appendCode("static ").visit(name);
		if (value != null) {
			context.appendCode(" = ").visit(value);
		}

//		throw new UnsupportedOperationException("전역변수 처리 필요");
	}
}
