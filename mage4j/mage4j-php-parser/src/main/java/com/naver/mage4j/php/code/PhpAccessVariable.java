package com.naver.mage4j.php.code;


public class PhpAccessVariable extends PhpAccess {
	private final String name;
	private final boolean suppressWarnings;
	private final int dollarCount;

	public PhpAccessVariable(String name) {
		this(name, 0);
	}

	public PhpAccessVariable(String name, int dollarCount) {
		this(name, dollarCount, false);
	}

	public PhpAccessVariable(String name, int dollarCount, boolean suppressWarnings) {
		super();
		this.name = name;
		this.dollarCount = dollarCount;
		this.suppressWarnings = suppressWarnings;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return (dollarCount > 0 ? "$" : "") + name;
	}

	public boolean isLiteral() {
		return dollarCount == 0;
	}
}
