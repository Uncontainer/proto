package com.naver.mage4j.php;

import com.naver.mage4j.php.code.PhpAccessClass;
import com.naver.mage4j.php.code.PhpAccessFunction;
import com.naver.mage4j.php.code.PhpExpression;
import com.naver.mage4j.php.mage.MageAccessElement;
import com.naver.mage4j.php.mage.converter.PhpAccessUtils;
import com.naver.mage4j.php.mage.converter.access.AccessConvertContext;
import com.naver.mage4j.php.mage.converter.access.AccessConverterClass;

public class AppTypeConverter extends AccessConverterClass {
	@Override
	public MageAccessElement convert(AccessConvertContext context, PhpAccessClass access) {
		PhpExpression current = access.getIndex();
		if (current instanceof PhpAccessFunction) {
			PhpAccessFunction f = (PhpAccessFunction)current;
			String name = PhpAccessUtils.getLiteralFunctionName(f);
			if (name != null) {
				if ("getCache".equals(name)) {
					//										element = new PhpAccessJavaMethod(ClassUtils.getMethod(AppContext.class, "getCurrent"), f.getArgs());
				}
			}
		}

		return context.byPassFunction();
	}
}
