package com.naver.mage4j.php.code;

public class PhpStatementContinue implements PhpStatement {
	private String label;

	public PhpStatementContinue(String label) {
		super();
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
