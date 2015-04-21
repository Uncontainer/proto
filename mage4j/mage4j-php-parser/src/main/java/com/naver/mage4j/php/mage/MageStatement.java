package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public interface MageStatement {
	void visitJava(JavaCodeGenerateContext context);
}
