package com.naver.mage4j.php.mage.converter.access;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.MageAccessElement;
import com.naver.mage4j.php.mage.MageExpression;
import com.naver.mage4j.php.mage.converter.JavaClassUtils;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageAccessJavaMethod implements MageAccessElement {
	private final MageExpression target;
	private final Method method;
	private final List<MageExpression> args;
	private volatile PhpType type;

	public MageAccessJavaMethod(Method method, List<MageExpression> args) {
		this(null, method, args);
	}

	public MageAccessJavaMethod(Method method, MageExpression... args) {
		this(method, Arrays.asList(args));
	}

	public MageAccessJavaMethod(MageExpression target, Method method, MageExpression... args) {
		this(target, method, Arrays.asList(args));
	}

	public MageAccessJavaMethod(MageExpression target, Method method, List<MageExpression> args) {
		super();
		if ((method == null)
			|| (target == null && !JavaClassUtils.isStatic(method))) {
			throw new IllegalArgumentException();
		}

		this.target = target;
		this.method = method;
		this.args = args;

		if (target != null) {
			target.setType(PhpTypeFactory.getType(method.getDeclaringClass()));
		}

		Class<?>[] parameterTypes = method.getParameterTypes();
		for (int i = 0; i < args.size(); i++) {
			if (i < parameterTypes.length) {
				args.get(i).setType(PhpTypeFactory.getType(parameterTypes[i]));
			}/* else {
				context.visit(args.get(i));
				}*/
		}
	}

	@Override
	public PhpType getType() {
		if (type == null) {
			type = PhpTypeFactory.getType(method.getReturnType());
		}

		return type;
	}

	@Override
	public void setType(PhpType type) {
		//		throw new UnsupportedOperationException();
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		if (JavaClassUtils.isStatic(method)) {
			context.appendCode(method.getDeclaringClass().getSimpleName()).appendCode(".");
		} else {
			context.visit(target).appendCode(".");
		}

		context.addImport(method.getDeclaringClass());
		context.appendCode(method.getName()).visit(args, "(", ")", ", ");
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		if (JavaClassUtils.isStatic(method)) {
			out.append(method.getDeclaringClass().getSimpleName()).append(".");
		} else {
			out.append(target).append(".");
		}

		out.append(method.getName()).append("(").append(args).append(")");

		return out.toString();
	}

	//	@Override
	//	public int getAccessTypes() {
	//		return LITERAL | FUNCTION;
	//	}
	//
	//	@Override
	//	public String getNameString() {
	//		return method.getName();
	//	}
	//
	//	public List<MageExpression> getArgs() {
	//		return args != null ? args : Collections.emptyList();
	//	}
}
