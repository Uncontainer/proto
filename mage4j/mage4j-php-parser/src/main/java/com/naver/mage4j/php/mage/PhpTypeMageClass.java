package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpAccessFunction;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeClass;
import com.naver.mage4j.php.mage.converter.PhpAccessUtils;

public class PhpTypeMageClass implements PhpTypeClass {
	private final MageClass mageClass;

	public PhpTypeMageClass(MageClass mageClass) {
		super();
		this.mageClass = mageClass;
	}

	@Override
	public String getName() {
		return mageClass.getName();
	}

	@Override
	public boolean isAssignableFrom(PhpType type) {
		return false;
//		throw new UnsupportedOperationException();
	}

	@Override
	public Class<?> getJavaClass() {
		//		throw new UnsupportedOperationException();
		return null;
	}

	public MageClass getMageClass() {
		return mageClass;
	}

	@Override
	public MageFunctionSignature getSignature(PhpAccessFunction phpFunctionCall) {
		String name = PhpAccessUtils.getLiteralFunctionName(phpFunctionCall);
		if (name != null) {
			return mageClass.getFunction(phpFunctionCall);
		}

		return null;
	}
}
