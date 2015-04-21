package com.naver.mage4j.php;

import java.lang.reflect.Method;
import java.util.List;

import com.naver.mage4j.php.mage.MageAccessElement;
import com.naver.mage4j.php.mage.MageAtomString;
import com.naver.mage4j.php.mage.MageExpression;
import com.naver.mage4j.php.mage.converter.JavaClassUtils;
import com.naver.mage4j.php.mage.converter.access.AccessConvertContext;
import com.naver.mage4j.php.mage.converter.access.AccessConverterFunction;
import com.naver.mage4j.php.mage.converter.access.MageAccessJavaMethod;
import com.naver.mage4j.php.mage.converter.util.ConvertUtils;

public class FunctionRegexConverter {

	public FunctionRegexConverter() {
		AccessConverterFunction.registFuncionConverter("preg_split", new AccessConverterFunction() {
			@Override
			public MageAccessElement convert(AccessConvertContext context, List<MageExpression> args) {
				if (args.size() == 2) {
					Method method = JavaClassUtils.getMethod(String.class, "split", String.class);
					return new MageAccessJavaMethod(args.get(1), method, toJavaRegexExpression(args.get(0)));
				}

				return context.byPassFunction();
			}
		});

		AccessConverterFunction.registFuncionConverter("preg_match", new AccessConverterFunction() {
			@Override
			public MageAccessElement convert(AccessConvertContext context, List<MageExpression> args) {
				if (args.size() == 2) {
					Method method = JavaClassUtils.getMethod(String.class, "matches", String.class);
					return new MageAccessJavaMethod(args.get(1), method, toJavaRegexExpression(args.get(0)));
				} else if (args.size() == 3) {

				}

				return context.byPassFunction();
			}
		});

		AccessConverterFunction.registFuncionConverter("preg_replace", new AccessConverterFunction() {
			@Override
			public MageAccessElement convert(AccessConvertContext context, List<MageExpression> args) {
				if (args.size() == 3) {
					Method method = JavaClassUtils.getMethod(String.class, "replaceAll", String.class, String.class);
					return new MageAccessJavaMethod(args.get(2), method, toJavaRegexExpression(args.get(0)), args.get(1));
				}

				return context.byPassFunction();
			}
		});
	}

	public static MageExpression toJavaRegexExpression(MageExpression mageExpression) {
		if (mageExpression instanceof MageAtomString) {
			String regex = ConvertUtils.phpToJavaRegex(((MageAtomString)mageExpression).getValue());
			return new MageAtomString(regex);
		} else {
			Method method = JavaClassUtils.getMethod(ConvertUtils.class, "phpToJavaRegex", String.class);
			return new MageAccessJavaMethod(method, mageExpression);
		}
	}
}
