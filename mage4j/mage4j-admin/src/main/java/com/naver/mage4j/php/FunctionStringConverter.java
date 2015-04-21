package com.naver.mage4j.php;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.core.util.PhpStringUtils;
import com.naver.mage4j.external.php.Standard;
import com.naver.mage4j.php.mage.MageAccessElement;
import com.naver.mage4j.php.mage.MageAtomString;
import com.naver.mage4j.php.mage.MageExpression;
import com.naver.mage4j.php.mage.converter.JavaClassUtils;
import com.naver.mage4j.php.mage.converter.access.AccessConvertContext;
import com.naver.mage4j.php.mage.converter.access.AccessConverterFunction;
import com.naver.mage4j.php.mage.converter.access.MageAccessJavaMethod;

public class FunctionStringConverter {
	public FunctionStringConverter() {
		AccessConverterFunction.registFuncionConverter("strstr", new AccessConverterFunction() {
			@Override
			public MageAccessElement convert(AccessConvertContext context, List<MageExpression> args) {
				Method method;
				switch (args.size()) {
					case 2:
						method = JavaClassUtils.getMethod(PhpStringUtils.class, "replaceEach", String.class, Map.class);
						break;
					case 3:
						method = JavaClassUtils.getMethod(PhpStringUtils.class, "replaceEach", String.class, String.class, String.class);
						break;
					default:
						method = null;
				}

				if (method != null) {
					return new MageAccessJavaMethod(method, args);
				} else {
					return context.byPassFunction();
				}
			}
		});

		AccessConverterFunction.registFuncionConverter("ucfirst", new AccessConverterFunction() {
			@Override
			public MageAccessJavaMethod convert(AccessConvertContext context, List<MageExpression> args) {
				Method method = JavaClassUtils.getMethod(StringUtils.class, "capitalize", String.class);

				return new MageAccessJavaMethod(method, args);
			}
		});

		AccessConverterFunction.registFuncionConverter("strpos", new AccessConverterFunction() {
			@Override
			public MageAccessJavaMethod convert(AccessConvertContext context, List<MageExpression> args) {
				MageExpression target = args.get(0);
				List<MageExpression> javaArgs = args.subList(1, args.size());
				Method method;
				if (javaArgs.size() > 1) {
					method = JavaClassUtils.getMethod(String.class, "indexOf", String.class, int.class);
				} else {
					method = JavaClassUtils.getMethod(String.class, "indexOf", String.class);
				}

				return new MageAccessJavaMethod(target, method, javaArgs);
			}
		});

		AccessConverterFunction.registFuncionConverter("strrpos", new AccessConverterFunction() {
			@Override
			public MageAccessJavaMethod convert(AccessConvertContext context, List<MageExpression> args) {
				MageExpression target = args.get(0);
				List<MageExpression> javaArgs = args.subList(1, args.size());
				Method method;
				if (javaArgs.size() > 1) {
					method = JavaClassUtils.getMethod(String.class, "lastIndexOf", String.class, int.class);
				} else {
					method = JavaClassUtils.getMethod(String.class, "lastIndexOf", String.class);
				}

				return new MageAccessJavaMethod(target, method, javaArgs);
			}
		});

		AccessConverterFunction.registFuncionConverter("strtok", new AccessConverterFunction() {
			@Override
			public MageAccessJavaMethod convert(AccessConvertContext context, List<MageExpression> args) {
				Method method = JavaClassUtils.getMethod(StringUtils.class, "split", String.class, String.class);

				return new MageAccessJavaMethod(method, args);
			}
		});

		AccessConverterFunction.registFuncionConverter("str_replace", new AccessConverterFunction() {
			@Override
			public MageAccessElement convert(AccessConvertContext context, List<MageExpression> args) {
				if (args.size() == 3) {
					Method method = JavaClassUtils.getMethod(String.class, "replace", CharSequence.class, CharSequence.class);
					return new MageAccessJavaMethod(args.get(2), method, args.get(0), args.get(1));
				}

				return context.byPassFunction();
			}
		});

		AccessConverterFunction.registFuncionConverter("explode", new AccessConverterFunction() {
			@Override
			public MageExpression convert(AccessConvertContext context, List<MageExpression> args) {
				switch (args.size()) {
					case 3: {
						Method method = JavaClassUtils.getMethod(StringUtils.class, "split", String.class, String.class, int.class);
						return new MageAccessJavaMethod(method, args.get(1), args.get(0), args.get(2));
					}
					case 2: {
						Method method = JavaClassUtils.getMethod(StringUtils.class, "split", String.class, String.class);
						return new MageAccessJavaMethod(args.get(1), method, args.get(0));
					}
					case 1:
						// TODO 파라미터를 한개만 넣었을 경우 어떻게 동작하는지 확인.
					default:
						return context.byPassFunction();
				}
			}
		});

		AccessConverterFunction.registFuncionConverter("implode", new AccessConverterFunction() {
			@Override
			public MageExpression convert(AccessConvertContext context, List<MageExpression> args) {
				List<MageExpression> javaArgs = new ArrayList<MageExpression>(2);
				switch (args.size()) {
					case 1:
						javaArgs.add(args.get(0));
						javaArgs.add(new MageAtomString(""));
						break;
					case 2:
						javaArgs.add(args.get(1));
						javaArgs.add(args.get(0));
						break;
					default:
						return context.byPassFunction();
				}

				Method method = JavaClassUtils.getMethod(StringUtils.class, "join", Object[].class, String.class);
				return new MageAccessJavaMethod(method, javaArgs);
			}
		});

		AccessConverterFunction.registFuncionConverter("strtoupper", new AccessConverterFunction() {
			@Override
			public MageAccessJavaMethod convert(AccessConvertContext context, List<MageExpression> args) {
				Method method = JavaClassUtils.getMethod(String.class, "toUpperCase");
				return new MageAccessJavaMethod(args.get(0), method);
			}
		});

		AccessConverterFunction.registFuncionConverter("strtolower", new AccessConverterFunction() {
			@Override
			public MageAccessJavaMethod convert(AccessConvertContext context, List<MageExpression> args) {
				Method method = JavaClassUtils.getMethod(String.class, "toLowerCase");
				return new MageAccessJavaMethod(args.get(0), method);
			}
		});

		AccessConverterFunction.registFuncionConverter("strlen", new AccessConverterFunction() {
			@Override
			public MageAccessJavaMethod convert(AccessConvertContext context, List<MageExpression> args) {
				Method method = JavaClassUtils.getMethod(String.class, "length");
				return new MageAccessJavaMethod(args.get(0), method);
			}
		});

		AccessConverterFunction.registFuncionConverter("substr", new AccessConverterFunction() {
			@Override
			public MageAccessJavaMethod convert(AccessConvertContext context, List<MageExpression> args) {
				Method method;
				if (args.size() == 2) {
					method = JavaClassUtils.getMethod(String.class, "substring", int.class);
				} else {
					method = JavaClassUtils.getMethod(String.class, "substring", int.class, int.class);
				}

				return new MageAccessJavaMethod(args.get(0), method, args.subList(1, args.size()));
			}
		});

		AccessConverterFunction.registFuncionConverter("trim", new AccessConverterFunction() {
			@Override
			public MageAccessElement convert(AccessConvertContext context, List<MageExpression> args) {
				switch (args.size()) {
					case 2: {
						Method method = JavaClassUtils.getMethod(Standard.class, "trim", String.class, String.class);
						return new MageAccessJavaMethod(method, args);
					}
					case 1: {
						Method method = JavaClassUtils.getMethod(StringUtils.class, "trim", String.class);
						return new MageAccessJavaMethod(method, args);
					}
				}

				return context.byPassFunction();
			}
		});
	}
}
