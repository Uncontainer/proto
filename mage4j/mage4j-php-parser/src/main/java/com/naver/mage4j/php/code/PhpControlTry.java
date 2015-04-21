package com.naver.mage4j.php.code;

import java.util.ArrayList;
import java.util.List;

import com.naver.mage4j.php.lang.PhpType;

public class PhpControlTry implements PhpControlStatement {
	private PhpStatementBlock main;
	private List<Catch> catches = new ArrayList<Catch>();

	public PhpControlTry(PhpStatementBlock main) {
		super();
		this.main = main;
	}

	public void addCatch(Catch c) {
		catches.add(c);
	}

	public PhpStatementBlock getMain() {
		return main;
	}

	public List<Catch> getCatches() {
		return catches;
	}

	public static class Catch implements PhpCode {
		private PhpType type;
		private PhpAccessVariable variable;
		private PhpStatementBlock body;

		public Catch(PhpType type, PhpAccessVariable variable, PhpStatementBlock body) {
			super();
			this.type = type;
			this.variable = variable;
			this.body = body;
		}

		public PhpType getType() {
			return type;
		}

		public PhpAccessVariable getVariable() {
			return variable;
		}

		public PhpStatementBlock getBody() {
			return body;
		}
	}
}
