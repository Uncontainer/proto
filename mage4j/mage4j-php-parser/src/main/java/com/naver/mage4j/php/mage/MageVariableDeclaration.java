package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpVariableDeclaration;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.converter.PhpTypeUtils;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageVariableDeclaration implements MageStatement, MageStatementInitializable<PhpVariableDeclaration> {
	private MageAccessVariable name;
	private MageExpression defaultValue;

	public MageVariableDeclaration(MageAccessVariable variable) {
		super();
		this.name = variable;
	}

	MageVariableDeclaration() {
	}

	@Override
	public void init(MageContext context, PhpVariableDeclaration decl) {
		name = context.registVariable(decl.getName(), decl.getType(), null);
		if (decl.getDefaultValue() != null) {
			if (PhpTypeUtils.isUndecided(name.getType())) {
				defaultValue = context.buildExpression(decl.getDefaultValue());
				setType(defaultValue.getType());
			} else {
				defaultValue = context.buildExpression(decl.getDefaultValue(), name.getType());
			}
		}
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.visit(getType()).appendCode(" ").visit(name);
		if (defaultValue != null) {
			context.appendCode("/* = ").visit(defaultValue).appendCode(" */");
		}
	}

	public void setType(PhpType type) {
		if (type == null) {
			return;
		}

		name.setType(type);
		if (defaultValue != null) {
			try {
				defaultValue.setType(type);
			} catch (IllegalArgumentException e) {
				throw e;
			}
		}
	}

	public PhpType getType() {
		PhpType type = name.getType();
		return type != null ? type : PhpTypeFactory.UNDECIDED;
	}

	public MageAccessVariable getName() {
		return name;
	}

	public MageExpression getDefaultValue() {
		return defaultValue;
	}

	@Override
	public String toString() {
		return getType() + " " + name + " = " + defaultValue;
	}
}
