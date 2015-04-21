package com.naver.mage4j.php.mage;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.php.code.PhpExpressionCast;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.mage.converter.PhpTypeUtils;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageExpressionCast implements MageExpression, MageStatementInitializable<PhpExpressionCast> {
	private MageExpression target;
	private List<PhpType> types;

	MageExpressionCast() {
		super();
	}

	@Override
	public void init(MageContext context, PhpExpressionCast statement) {
		target = context.buildExpression(statement.getTarget());
		types = statement.getTypes();

		PhpType type = target.getType();
		if (PhpTypeUtils.isUndecided(type)) {
			return;
		}

		for (int i = types.size() - 1; i >= 0; i--) {
			if (PhpTypeUtils.equals(types.get(i), type)) {
				types.remove(i);
			} else {
				type = types.get(i);
			}
		}
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		for (PhpType type : types) {
			context.appendCode("(").visit(type).appendCode(")");
		}
		context.visit(target);
	}

	@Override
	public PhpType getType() {
		if (types.isEmpty()) {
			return target.getType();
		} else {
			return types.get(0);
		}
	}

	@Override
	public void setType(PhpType type) {
//		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return StringUtils.join(types.stream().map(s -> "(" + s.getName() + ")").collect(Collectors.toList()), "") + target;
	}
}
