package com.naver.mage4j.php.lang;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.naver.mage4j.php.code.PhpAccessFunction;
import com.naver.mage4j.php.mage.MageAccessVariable;
import com.naver.mage4j.php.mage.MageFunctionSignature;
import com.naver.mage4j.php.mage.MageVariableDeclaration;
import com.naver.mage4j.php.mage.converter.JavaClassUtils;
import com.naver.mage4j.php.mage.converter.PhpAccessUtils;

public class PhpTypeJava implements PhpTypeClass {
	private final Logger log = LoggerFactory.getLogger(PhpTypeJava.class);

	private final Class<?> clazz;

	public PhpTypeJava(Class<?> clazz) {
		super();
		if (clazz == null) {
			throw new IllegalArgumentException();
		}

		this.clazz = clazz;
	}

	@Override
	public String getName() {
		return clazz.getSimpleName();
	}

	@Override
	public boolean isAssignableFrom(PhpType type) {
		if (clazz != null) {
			if (type.getJavaClass() != null) {
				return clazz.isAssignableFrom(type.getJavaClass());
			} else {
				return false;
			}
		} else {
			return type.getJavaClass() != null;
		}
	}

	@Override
	public Class<?> getJavaClass() {
		return clazz;
	}

	@Override
	public MageFunctionSignature getSignature(PhpAccessFunction phpFunctionCall) {
		Method method = null;
		String name = PhpAccessUtils.getLiteralFunctionName(phpFunctionCall);
		if (name != null) {
			try {
				method = JavaClassUtils.getMethodWithArgumentCount(clazz, name, phpFunctionCall.getArgs());
			} catch (IllegalArgumentException e) {
				log.info("", e);
				return null;
			}
		}

		if (method == null) {
			return null;
		}

		return new MageFunctionSignatureJava(method);
	}

	static class MageFunctionSignatureJava implements MageFunctionSignature {
		final Method method;

		public MageFunctionSignatureJava(Method method) {
			super();
			this.method = method;
		}

		@Override
		public PhpType getReturnType() {
			return PhpTypeFactory.getType(method.getReturnType());
		}

		@Override
		public String getName() {
			return method.getName();
		}

		@Override
		public List<MageVariableDeclaration> getParameters() {
			Class<?>[] parameterTypes = method.getParameterTypes();
			List<MageVariableDeclaration> result = new ArrayList<MageVariableDeclaration>(parameterTypes.length);
			for (int i = 0; i < parameterTypes.length; i++) {
				MageAccessVariable variable = new MageAccessVariable("param_" + i, PhpTypeFactory.getType(parameterTypes[i]));
				result.add(new MageVariableDeclaration(variable));
			}

			return result;
		}
	}
}
