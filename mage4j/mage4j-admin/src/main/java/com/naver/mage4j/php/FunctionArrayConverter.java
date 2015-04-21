package com.naver.mage4j.php;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.naver.mage4j.php.mage.MageAccessElement;
import com.naver.mage4j.php.mage.MageAtomArray;
import com.naver.mage4j.php.mage.MageAtomNumber;
import com.naver.mage4j.php.mage.MageExpression;
import com.naver.mage4j.php.mage.converter.JavaClassUtils;
import com.naver.mage4j.php.mage.converter.access.AccessConvertContext;
import com.naver.mage4j.php.mage.converter.access.AccessConverterFunction;
import com.naver.mage4j.php.mage.converter.access.MageAccessJavaMethod;

public class FunctionArrayConverter {
	public FunctionArrayConverter() {
		AccessConverterFunction.registFuncionConverter("array_keys", new AccessConverterFunction() {
			@Override
			public MageAccessElement convert(AccessConvertContext context, List<MageExpression> args) {
				if (args.size() == 1) {
					Method method = JavaClassUtils.getMethod(Map.class, "keySet");
					return new MageAccessJavaMethod(args.get(0), method);
				} else {
					return context.byPassFunction();
				}
			}
		});

		AccessConverterFunction.registFuncionConverter("array_shift", new AccessConverterFunction() {
			@Override
			public MageAccessElement convert(AccessConvertContext context, List<MageExpression> args) {
				if (args.size() == 1) {
					Method method = JavaClassUtils.getMethod(List.class, "remove", int.class);
					return new MageAccessJavaMethod(args.get(0), method, new MageAtomNumber(0));
				} else {
					return context.byPassFunction();
				}
			}
		});

		AccessConverterFunction.registFuncionConverter("array_unshift", new AccessConverterFunction() {
			@Override
			public MageAccessElement convert(AccessConvertContext context, List<MageExpression> args) {
				if (args.size() == 2) {
					Method method = JavaClassUtils.getMethod(List.class, "add", Object.class);
					return new MageAccessJavaMethod(args.get(0), method, args.get(1));
				} else if (args.size() > 2) {
					Method method = JavaClassUtils.getMethod(List.class, "addAll", int.class, Collection.class);
					return new MageAccessJavaMethod(args.get(0), method, new MageAtomNumber(0), new MageAtomArray(args.subList(1, args.size())));
				} else {
					return context.byPassFunction();
				}
			}
		});

		AccessConverterFunction.registFuncionConverter("array_values", new AccessConverterFunction() {
			@Override
			public MageAccessElement convert(AccessConvertContext context, List<MageExpression> args) {
				if (args.size() == 1) {
					Method method = JavaClassUtils.getMethod(Map.class, "values");
					return new MageAccessJavaMethod(args.get(0), method);
				} else {
					return context.byPassFunction();
				}
			}
		});

		AccessConverterFunction.registFuncionConverter("array_key_exists", new AccessConverterFunction() {
			@Override
			public MageAccessElement convert(AccessConvertContext context, List<MageExpression> args) {
				Method method = JavaClassUtils.getMethod(Map.class, "containsKey", Object.class);
				return new MageAccessJavaMethod(args.get(1), method, args.get(0));
			}
		});
	}
}
