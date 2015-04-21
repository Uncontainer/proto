package com.naver.mage4j.php.mage.converter.access;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.naver.mage4j.php.code.PhpAccessClass;
import com.naver.mage4j.php.code.PhpAccessFunction;
import com.naver.mage4j.php.code.PhpExpression;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeClass;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.MageAccessConstant;
import com.naver.mage4j.php.mage.MageExpression;
import com.naver.mage4j.php.mage.MageFunctionSignature;

public abstract class AccessConverterClass implements AccessConverter {
	private static Map<String, AccessConverterClass> classConverterMap = new ConcurrentHashMap<String, AccessConverterClass>();

	public static void registTypeConverter(String typeName, AccessConverterClass converter) {
		classConverterMap.put(typeName, converter);
	}

	public static AccessConverterClass getClassConverter(String className) {
		return classConverterMap.get(className);
	}

	public static final AccessConverterClass BY_PASS = new AccessConverterClass() {
		@Override
		public MageExpression convert(AccessConvertContext context, PhpAccessClass access) {
			PhpAccessClass c = access;
			MageExpression name = context.buildExpression(c.getName());
			if (name instanceof MageAccessConstant) {
				String className = ((MageAccessConstant)name).getValue();
				name.setType("self".equals(className) ? context.getCurrentClass().getType() : PhpTypeFactory.get(className));
			}

			PhpExpression phpIndex = c.getIndex();

			MageExpression index;
			if (phpIndex instanceof PhpAccessFunction) {
				PhpType type = name.getType();
				MageFunctionSignature functionSignature;
				if (type instanceof PhpTypeClass) {
					functionSignature = ((PhpTypeClass)type).getSignature((PhpAccessFunction)phpIndex);
				} else {
					functionSignature = null;
				}
				index = context.fromFunction((PhpAccessFunction)phpIndex, functionSignature);
			} else {
				index = context.buildExpression(phpIndex);
			}

			return new MageAccessClass(name, index);
		}
	};

	//	@Override
	//	public MageExpression convert(AccessConvertContext context) {
	//		PhpAccess access = context.getElement();
	//		return convert(context);
	//	}

	protected List<MageExpression> getMethodArgs(AccessConvertContext context, PhpAccessFunction functionCall) {
		return AccessConverterFunction.getArgs(context, functionCall);
	}

	@Override
	public MageExpression convert(AccessConvertContext context) {
		return convert(context, (PhpAccessClass)context.getElement());
	}

	protected abstract MageExpression convert(AccessConvertContext context, PhpAccessClass access);
}
