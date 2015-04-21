package com.naver.mage4j.php.mage.converter.access;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.naver.mage4j.php.code.PhpAccessFunction;
import com.naver.mage4j.php.code.PhpExpression;
import com.naver.mage4j.php.mage.MageExpression;
import com.naver.mage4j.php.mage.converter.PhpAccessUtils;

public abstract class AccessConverterFunction implements AccessConverter {
	private static Map<String, AccessConverterFunction> functionConverterMap = new ConcurrentHashMap<String, AccessConverterFunction>();

	public static void registFuncionConverter(String name, AccessConverterFunction converter) {
		functionConverterMap.put(name, converter);
	}

	public static AccessConverterFunction getFunctionConverter(PhpAccessFunction functionCall) {
		String name = PhpAccessUtils.getLiteralFunctionName(functionCall);
		if (name != null) {
			AccessConverterFunction functionConverter = functionConverterMap.get(name);
			if (functionConverter != null) {
				return functionConverter;
			}
		}

		return null;
	}

	public static final AccessConverterFunction BY_PASS = new AccessConverterFunction() {
		@Override
		public MageExpression convert(AccessConvertContext context, List<MageExpression> args) {
			return context.byPassFunction();
		}
	};

	@Override
	public final MageExpression convert(AccessConvertContext context) {
		return convert(context, getArgs(context, (PhpAccessFunction)context.getElement()));
	}

	protected static List<MageExpression> getArgs(AccessConvertContext context, PhpAccessFunction phpFunctionCall) {
		List<PhpExpression> phpArgs = phpFunctionCall.getArgs();
		List<MageExpression> args = new ArrayList<MageExpression>(phpArgs.size());
		for (PhpExpression phpArg : phpArgs) {
			args.add(context.buildExpression(phpArg));
		}

		return args;
	}

	protected abstract MageExpression convert(AccessConvertContext context, List<MageExpression> args);
}
