package com.naver.mage4j.php.mage;

import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.php.code.PhpBodyString;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageBodyString implements MageStatement, MageStatementInitializable<PhpBodyString> {
	private String body;

	MageBodyString() {
		super();
	}

	@Override
	public void init(MageContext context, PhpBodyString statement) {
		this.body = statement.getBody();
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		if (StringUtils.isBlank(body)) {
			return;
		}

		// TODO Auto-generated method stub
		context.appendCode("/* [NOT-IMPLEMENT] PhpBodyString */");
	}
}
