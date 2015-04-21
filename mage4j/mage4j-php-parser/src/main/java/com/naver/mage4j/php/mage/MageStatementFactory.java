package com.naver.mage4j.php.mage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.naver.mage4j.php.MageProgram;
import com.naver.mage4j.php.code.PhpAccess;
import com.naver.mage4j.php.code.PhpAccessClass;
import com.naver.mage4j.php.code.PhpAccessFunction;
import com.naver.mage4j.php.code.PhpAccessVariable;
import com.naver.mage4j.php.code.PhpAtomNull;
import com.naver.mage4j.php.code.PhpExpressionAssignment;
import com.naver.mage4j.php.code.PhpExpressionBinary;
import com.naver.mage4j.php.code.PhpFunction;
import com.naver.mage4j.php.code.PhpStatement;
import com.naver.mage4j.php.mage.converter.access.AccessConvertContext;
import com.naver.mage4j.php.mage.converter.op.BinaryOperationConvertContext;

public class MageStatementFactory {
	private static Map<Class<? extends PhpStatement>, Converter> converterMap;

	static {
		converterMap = new HashMap<Class<? extends PhpStatement>, Converter>();

		add(MageAtomArray.class);
		add(MageAtomBoolean.class);
		add(MageAtomNumber.class);
		add(MageAtomString.class);
		add(MageBodyString.class);
		add(MageClass.class);
		add(MageClassField.class);
		add(MageClassMethod.class);
		add(MageClone.class);
		add(MageControlFor.class);
		add(MageControlForeach.class);
		add(MageControlIf.class);
		add(MageControlSwitch.class);
		add(MageControlTry.class);
		add(MageControlWhile.class);
		add(MageExpressionCast.class);
		add(MageExpressionIncrement.class);
		add(MageExpressionInstanceOf.class);
		add(MageExpressionLogicalNot.class);
		add(MageExpressionNegation.class);
		add(MageExpressionPrint.class);
		add(MageExpressionTernary.class);
		add(MageFunction.class, PhpFunction.class);
		add(MageInterface.class);
		add(MageNew.class);
		add(MagePair.class);
		add(MageStatementBlock.class);
		add(MageStatementBreak.class);
		add(MageStatementContinue.class);
		add(MageStatementEcho.class);
		add(MageStatementGlobal.class);
		add(MageStatementRequire.class);
		add(MageStatementReturn.class);
		add(MageStatementStatic.class);
		add(MageStatementThrow.class);
		add(MageVariableDeclaration.class);
		add(MageVariablesDeclaration.class);
		add(MageProgram.class);

		Converter<PhpAccess> accessConverter = new Converter<PhpAccess>() {
			@Override
			public MageStatement convert(MageContext context, PhpAccess element) {
				return new AccessConvertContext(context, element).convert();
			}
		};
		converterMap.put(PhpAccessClass.class, accessConverter);
		converterMap.put(PhpAccessFunction.class, accessConverter);
		converterMap.put(PhpAccessVariable.class, accessConverter);

		Converter<PhpExpressionBinary> binaryExpressionConverter = new Converter<PhpExpressionBinary>() {
			@Override
			public MageExpressionBinary convert(MageContext context, PhpExpressionBinary statement) {
				return new BinaryOperationConvertContext(statement, context).convert();
			}
		};
		converterMap.put(PhpExpressionBinary.class, binaryExpressionConverter);
		converterMap.put(PhpExpressionAssignment.class, binaryExpressionConverter);
		converterMap.put(PhpAtomNull.class, new Converter<PhpAtomNull>() {
			@Override
			public MageAtomNull convert(MageContext context, PhpAtomNull statement) {
				return MageAtomNull.INSTANCE;
			}
		});
	}

	private static <T extends MageStatement & MageStatementInitializable<? extends PhpStatement>> void add(Class<T> clazz) {
		Class<? extends PhpStatement> phpClass = null;
		for (Type each : clazz.getGenericInterfaces()) {
			if (each.getTypeName().startsWith(MageStatementInitializable.class.getTypeName())) {
				phpClass = (Class<? extends PhpStatement>)((ParameterizedType)each).getActualTypeArguments()[0];
				break;
			}
		}

		add(clazz, phpClass);
	}

	private static void add(Class<? extends MageStatement> mageClass, Class<? extends PhpStatement> phpClass) {
		converterMap.put(phpClass, new InitMethodConverter(phpClass, mageClass));
	}

	@SuppressWarnings("unchecked")
	public static <T extends MageStatement> T create(MageContext context, PhpStatement statement) {
		Converter converter = converterMap.get(statement.getClass());
		if (converter == null) {
			throw new IllegalArgumentException("No constructor for '" + statement.getClass().getSimpleName() + "'");
		}

		return (T)converter.convert(context, statement);
	}

	public interface Converter<T extends PhpStatement> {
		MageStatement convert(MageContext context, T statement);
	}

	private static class InitMethodConverter implements Converter {
		final Constructor<? extends MageStatement> constructor;
		final Method initMethod;

		InitMethodConverter(Class<? extends PhpStatement> phpClass, Class<? extends MageStatement> mageClass) {
			try {
				constructor = mageClass.getDeclaredConstructor();
				constructor.setAccessible(true);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new RuntimeException("No default constructor.(" + mageClass.getSimpleName() + ")");
			}

			try {
				initMethod = mageClass.getMethod("init", MageContext.class, phpClass);
				initMethod.setAccessible(true);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new RuntimeException("No init method with parameter '" + phpClass.getSimpleName() + "'.(" + mageClass.getSimpleName() + ")");
			}
		}

		@Override
		public MageStatement convert(MageContext context, PhpStatement statement) {
			try {
				MageStatement instance = constructor.newInstance();

				boolean newScope = instance instanceof MageScope;
				boolean isClass = instance instanceof MageClass;
				if (newScope) {
					context.enterScope((MageScope)instance);
				}
				if (isClass) {
					context.enterClass((MageClass)instance);
				}

				try {
					((MageStatementInitializable<PhpStatement>)instance).init(context, statement);
					//					initMethod.invoke(instance, context, statement);
				} finally {
					if (newScope) {
						context.leaveScope();
					}
					if (isClass) {
						context.leaveClass();
					}
				}

				return instance;
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				Throwable cause = e instanceof InvocationTargetException ? e.getCause() : e;
				throw new RuntimeException("Fail to create instacne.(" + statement.getClass().getSimpleName() + ")", cause);
			}
		}
	}
}
