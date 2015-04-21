package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpPair;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MagePair implements MageStatement, MageStatementInitializable<PhpPair> {
	private MageExpression key;
	private MageExpression value;

	MagePair() {
	}

	@Override
	public void init(MageContext context, PhpPair statement) {
		this.value = context.buildExpression(statement.getValue());
		this.key = context.buildExpression(statement.getKey(), value.getType());
		// TODO type 설정
		//		key.setType(value.getType());
	}

	public MageExpression getKey() {
		return key;
	}

	public MageExpression getValue() {
		return value;
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		// TODO Auto-generated method stub
		context.appendCode("/* [NOT-IMPLEMENT] PhpNameList */");
	}
}
