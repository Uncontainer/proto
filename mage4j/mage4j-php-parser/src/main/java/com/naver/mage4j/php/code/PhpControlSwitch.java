package com.naver.mage4j.php.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PhpControlSwitch implements PhpControlStatement {
	private PhpExpression condition;
	private List<Case> cases;

	public PhpControlSwitch(PhpExpression condition) {
		super();
		this.condition = condition;
	}

	public void addCase(Case caseStatement) {
		if (cases == null) {
			cases = new ArrayList<Case>();
		}

		cases.add(caseStatement);
	}

	public PhpExpression getCondition() {
		return condition;
	}

	public List<Case> getCases() {
		return cases != null ? cases : Collections.emptyList();
	}

	public static class Case implements PhpCode {
		private PhpExpression label;
		protected PhpStatementBlock body;

		public Case(PhpExpression label) {
			super();
			this.label = label;
		}

		public PhpExpression getLabel() {
			return label;
		}

		public void setLabel(PhpExpression label) {
			this.label = label;
		}

		public PhpStatementBlock getBody() {
			return body;
		}

		public void setBody(PhpStatementBlock body) {
			this.body = body;
		}
	}

	public static class DefaultCase extends Case {
		public DefaultCase() {
			super(null);
		}
	}
}
