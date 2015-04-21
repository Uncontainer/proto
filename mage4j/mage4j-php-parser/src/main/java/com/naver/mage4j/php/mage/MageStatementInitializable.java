package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpStatement;

public interface MageStatementInitializable<T extends PhpStatement> {
	void init(MageContext context, T statement);
}
