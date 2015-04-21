package com.naver.mage4j.php;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.core.util.PhpDateUtils;
import com.naver.mage4j.external.php.Standard;
import com.naver.mage4j.php.code.PhpBinaryOperator;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.MageAtomNull;
import com.naver.mage4j.php.mage.MageAtomNumber;
import com.naver.mage4j.php.mage.MageExpression;
import com.naver.mage4j.php.mage.MageExpressionBinary;
import com.naver.mage4j.php.mage.MageExpressionNegation;
import com.naver.mage4j.php.mage.converter.JavaClassUtils;
import com.naver.mage4j.php.mage.converter.access.AccessConvertContext;
import com.naver.mage4j.php.mage.converter.access.AccessConverterFunction;
import com.naver.mage4j.php.mage.converter.access.MageAccessJavaMethod;

public class FunctionConverter {
	public FunctionConverter() {
		AccessConverterFunction.registFuncionConverter("strtotime", new AccessConverterFunction() {
			@Override
			public MageAccessJavaMethod convert(AccessConvertContext context, List<MageExpression> args) {
				Method method = JavaClassUtils.getMethod(PhpDateUtils.class, "parseAndGetTime", String.class);

				return new MageAccessJavaMethod(method, args);
			}
		});

		AccessConverterFunction.registFuncionConverter("is_empty_date", new AccessConverterFunction() {
			@Override
			public MageAccessJavaMethod convert(AccessConvertContext context, List<MageExpression> args) {
				Method method = JavaClassUtils.getMethod(PhpDateUtils.class, "isEmptyDate", String.class);

				return new MageAccessJavaMethod(method, args);
			}
		});

		AccessConverterFunction.registFuncionConverter("isset", new AccessConverterFunction() {
			@Override
			public MageExpression convert(AccessConvertContext context, List<MageExpression> args) {
				MageExpression expression = args.get(0);
				return new MageExpressionBinary(expression, PhpBinaryOperator.NOT_EQUAL, MageAtomNull.INSTANCE);
			}
		});

		AccessConverterFunction.registFuncionConverter("is_null", new AccessConverterFunction() {
			@Override
			public MageExpression convert(AccessConvertContext context, List<MageExpression> args) {
				MageExpression expression = args.get(0);
				return new MageExpressionBinary(expression, PhpBinaryOperator.EQUAL, MageAtomNull.INSTANCE);
			}
		});

		AccessConverterFunction.registFuncionConverter("empty", new AccessConverterFunction() {
			@Override
			public MageExpression convert(AccessConvertContext context, List<MageExpression> args) {
				MageExpression expression = args.get(0);
				PhpType type = expression.getType();
				// empty string
				if (type == PhpTypeFactory.STRING) {
					Method method = JavaClassUtils.getMethod(StringUtils.class, "isEmpty", CharSequence.class);
					return new MageAccessJavaMethod(method, expression);
				}
				// false
				else if (type == PhpTypeFactory.BOOLEAN) {
					return new MageExpressionNegation(expression, "!");
				}
				// 0
				else if (type == PhpTypeFactory.INTEGER) {
					return new MageExpressionBinary(expression, PhpBinaryOperator.EQUAL, new MageAtomNumber(0));
				} else if (type == PhpTypeFactory.MAP) {
					return new MageExpressionBinary(new MageExpressionBinary(expression, PhpBinaryOperator.EQUAL, MageAtomNull.INSTANCE)
						, PhpBinaryOperator.LOCIAL_OR
						, new MageAccessJavaMethod(expression, JavaClassUtils.getMethod(Map.class, "isEmpty")));
				} else {
					return new MageExpressionBinary(expression, PhpBinaryOperator.EQUAL, MageAtomNull.INSTANCE);
				}
			}
		});

		AccessConverterFunction.registFuncionConverter("md5", new AccessConverterFunction() {
			@Override
			public MageExpression convert(AccessConvertContext context, List<MageExpression> args) {
				if (args.size() == 1) {
					return new MageAccessJavaMethod(JavaClassUtils.getMethod(Standard.class, "md5", String.class), args);
				}

				return context.byPassFunction();
			}
		});

		AccessConverterFunction.registFuncionConverter("sha1", new AccessConverterFunction() {
			@Override
			public MageExpression convert(AccessConvertContext context, List<MageExpression> args) {
				if (args.size() == 1) {
					return new MageAccessJavaMethod(JavaClassUtils.getMethod(Standard.class, "sha1", String.class), args);
				}

				return context.byPassFunction();
			}
		});
	}
}
