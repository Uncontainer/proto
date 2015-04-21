package com.naver.mage4j.php.code;

public class PhpStatementBreak implements PhpStatement {
	private String label;

	public PhpStatementBreak(String label) {
		super();
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
