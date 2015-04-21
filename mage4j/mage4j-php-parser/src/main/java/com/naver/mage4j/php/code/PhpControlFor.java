package com.naver.mage4j.php.code;

import java.util.List;

public class PhpControlFor implements PhpControlStatement {
	private List<PhpExpression> init;
	private List<PhpExpression> condition;
	private List<PhpExpression> update;
	private PhpStatement body;

	public PhpControlFor(List<PhpExpression> init, List<PhpExpression> condition, List<PhpExpression> update, PhpStatement body) {
		super();
		this.init = init;
		this.condition = condition;
		this.update = update;
		this.body = body;
	}

	public List<PhpExpression> getInit() {
		return init;
	}

	public List<PhpExpression> getCondition() {
		return condition;
	}

	public List<PhpExpression> getUpdate() {
		return update;
	}

	public PhpStatement getBody() {
		return body;
	}
}
