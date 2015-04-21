package com.naver.mage4j.php.code;

import java.util.ArrayList;
import java.util.List;

public class PhpVariablesDeclaration extends PhpExpressionAbstract {
	private List<PhpAccess> names = new ArrayList<PhpAccess>();
	private PhpExpression value;

	public void add(PhpAccess name) {
		names.add(name);
	}

	public void setValue(PhpExpression value) {
		this.value = value;
	}

	public List<PhpAccess> getNames() {
		return names;
	}

	public PhpExpression getValue() {
		return value;
	}
}
