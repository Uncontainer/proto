package com.naver.mage4j.php.code;

import java.util.List;

public class PhpStatementGlobal implements PhpStatement {
	private List<PhpAccess> names;

	public PhpStatementGlobal(List<PhpAccess> names) {
		this.names = names;
	}

	public List<PhpAccess> getNames() {
		return names;
	}
}
