package com.naver.mage4j.php.mage;

import java.util.List;

import com.naver.mage4j.php.lang.PhpType;

public interface MageFunctionSignature {
	PhpType getReturnType();

	String getName();

	List<MageVariableDeclaration> getParameters();
}
