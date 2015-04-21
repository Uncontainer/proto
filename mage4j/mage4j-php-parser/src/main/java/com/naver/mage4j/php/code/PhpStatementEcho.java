package com.naver.mage4j.php.code;

import java.util.List;

public class PhpStatementEcho implements PhpStatement {
	private List<PhpStatement> value;

	public PhpStatementEcho(List<PhpStatement> value) {
		super();
		this.value = value;
	}

	public List<PhpStatement> getValue() {
		return value;
	}
}
