package com.naver.mage4j.php.mage;

import java.util.List;

import com.naver.mage4j.php.code.PhpStatementEcho;
import com.naver.mage4j.php.echo.EchoBuffer;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageStatementEcho implements MageStatement, MageStatementInitializable<PhpStatementEcho> {
	private List<MageStatement> value;

	MageStatementEcho() {
		super();
	}

	@Override
	public void init(MageContext context, PhpStatementEcho statement) {
		this.value = context.buildStatements(statement.getValue());
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.addImport(EchoBuffer.class);

		for (MageStatement statement : value) {
			context.appendCode("EchoBuffer.append(").visit(statement).appendCode(");").appendNewLine();
		}
	}
}
