package com.naver.mage4j.php.lang;

import com.naver.mage4j.php.code.PhpAccessFunction;
import com.naver.mage4j.php.mage.MageFunctionSignature;

public interface PhpTypeClass extends PhpType {
	MageFunctionSignature getSignature(PhpAccessFunction phpIndex);
}
