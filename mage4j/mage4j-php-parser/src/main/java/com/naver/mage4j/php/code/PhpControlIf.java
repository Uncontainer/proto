package com.naver.mage4j.php.code;

import java.util.ArrayList;
import java.util.List;

public class PhpControlIf implements PhpControlStatement {
	private PhpExpression condition;
	private PhpStatement trueBlock;
	private List<PhpControlIf> conditionals;
	private PhpStatement elseBlock;

	public PhpControlIf(PhpExpression condition) {
		this(condition, null);
	}

	public PhpControlIf(PhpExpression condition, PhpStatement trueBlock) {
		super();
		this.condition = condition;
		this.trueBlock = trueBlock;
	}

	public PhpExpression getCondition() {
		return condition;
	}

	public List<PhpControlIf> getConditionals() {
		return conditionals;
	}

	public void addConditional(PhpExpression condition, PhpStatement trueBlock) {
		addConditional(new PhpControlIf(condition, trueBlock));
	}

	public void addConditional(PhpControlIf conditional) {
		if (conditionals == null) {
			conditionals = new ArrayList<PhpControlIf>();
		}
		conditionals.add(conditional);
	}

	public PhpStatement getTrue() {
		return trueBlock;
	}

	public void setTrue(PhpStatement trueBlock) {
		this.trueBlock = trueBlock;
	}

	public PhpStatement getElse() {
		return elseBlock;
	}

	public void setElse(PhpStatement elseBlock) {
		this.elseBlock = elseBlock;
	}
}
