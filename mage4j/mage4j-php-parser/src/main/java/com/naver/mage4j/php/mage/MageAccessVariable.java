package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.mage.MageContext.VariableScopeType;
import com.naver.mage4j.php.mage.converter.PhpTypeUtils;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageAccessVariable extends MageExpressionAbstract implements MageAccessElement {
	private boolean literal;
	protected String name;
	protected VariableScopeType scope;

	public MageAccessVariable(String name) {
		this(name, null);
	}

	public MageAccessVariable(String name, PhpType type) {
		this.literal = false;
		this.name = name;
		if (type != null) {
			setType(PhpTypeUtils.select(getType(), type));
		}
	}

	public String getName() {
		return name;
	}

	public VariableScopeType getScope() {
		return scope;
	}

	public void setScope(VariableScopeType scope) {
		if (this.scope != null && this.scope != VariableScopeType.FOR) {
			throw new IllegalStateException("Variable scope has already set.(" + name + ":" + this.scope + "=>" + scope + ")");
		}

		this.scope = scope;
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.addImport(getType());
		// TODO 참조의 경우 해당 값으로 설정해야 함. 사용하는 곳에서 필요에 맞게 처리 필요
		context.appendCode(name);
	}

	@Override
	public String toString() {
		return (literal ? "" : "$") + name;
	}

	public boolean isLiteral() {
		return literal;
	}
}
