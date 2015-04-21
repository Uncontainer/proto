package com.naver.mage4j.php.code;

import java.util.List;

public class PhpAccessBuilder {
	private PhpAccess expression;

	public PhpAccessBuilder() {
	}

	public void addHeader(String className, PhpAccess item) {
		expression = className == null ? item : new PhpAccessClass(new PhpAccessVariable(className, 0), item);
	}

	public void add(PhpExpression item) {
		if (expression == null) {
			throw new IllegalStateException("Call 'addHeader' method if it is first addition.");
		}

		expression = new PhpAccessClass(expression, item);
	}

	public void addArrayAccess(PhpExpression item) {
		if (expression == null) {
			throw new IllegalStateException("Array access must require accessor.");
		}

		expression = new PhpAccessClass(expression, item);
	}

	public void addFunctionCall(List<PhpExpression> args) {
		if (expression == null) {
			throw new IllegalStateException("There is no function name.");
		}

		if (expression instanceof PhpAccessClass) {
			PhpAccessClass c = (PhpAccessClass)expression;
			expression = new PhpAccessClass(c.getName(), new PhpAccessFunction(c.getIndex(), args));
		} else if (expression instanceof PhpAccessVariable) {
			expression = new PhpAccessFunction(expression, args);
		} else {
			throw new IllegalStateException("Invalid function name type.(" + expression.getClass().getSimpleName() + ")");
		}
	}

	public PhpAccess getExpression() {
		return expression;
	}
}
