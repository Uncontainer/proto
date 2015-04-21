package com.naver.mage4j.php.lang;

public class PhpTypeAdapter implements PhpType {
	protected final String name;

	public PhpTypeAdapter(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isAssignableFrom(PhpType type) {
		return false;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public Class<?> getJavaClass() {
		return null;
	}
}
