package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.lang.PhpType;

public interface MageExpression extends MageStatement {
	PhpType getType();

	void setType(PhpType type);
}
