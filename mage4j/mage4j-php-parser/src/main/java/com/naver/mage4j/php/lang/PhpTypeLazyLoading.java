package com.naver.mage4j.php.lang;

public class PhpTypeLazyLoading implements PhpType {
	private String name;
	private PhpType type;

	public PhpTypeLazyLoading(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return getType().getName();
	}

	@Override
	public boolean isAssignableFrom(PhpType child) {
		return getType().isAssignableFrom(child);
	}

	private PhpType getType() {
		if (type == null) {
			type = PhpTypeFactory.get(name);
		}

		if (type == null) {
			throw new IllegalStateException("Type has not loaded yet.(" + name + ")");
		}

		return type;
	}

	@Override
	public Class<?> getJavaClass() {
		return getType().getJavaClass();
	}

}
