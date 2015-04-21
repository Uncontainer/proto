package com.naver.mage4j.php.mage.converter.access;

import java.util.ArrayList;
import java.util.List;

import com.naver.mage4j.php.code.PhpAccess;
import com.naver.mage4j.php.code.PhpAccessClass;
import com.naver.mage4j.php.code.PhpAccessFunction;
import com.naver.mage4j.php.code.PhpAccessVariable;
import com.naver.mage4j.php.code.PhpExpression;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.MageAccessConstant;
import com.naver.mage4j.php.mage.MageAccessElement;
import com.naver.mage4j.php.mage.MageAccessFunction;
import com.naver.mage4j.php.mage.MageAccessVariable;
import com.naver.mage4j.php.mage.MageAtomNumber;
import com.naver.mage4j.php.mage.MageClass;
import com.naver.mage4j.php.mage.MageClassMethod;
import com.naver.mage4j.php.mage.MageContext;
import com.naver.mage4j.php.mage.MageContextAdapter;
import com.naver.mage4j.php.mage.MageExpression;
import com.naver.mage4j.php.mage.MageFunctionSignature;
import com.naver.mage4j.php.mage.MageVariableDeclaration;
import com.naver.mage4j.php.mage.converter.PhpAccessUtils;
import com.naver.mage4j.php.mage.converter.PhpTypeUtils;

public class AccessConvertContext extends MageContextAdapter {
	private MageClass currentClass;
	private PhpAccess element;

	//	private Stack<PhpClass> callStack;

	public AccessConvertContext(MageContext mageContext, PhpAccess element) {
		super(mageContext instanceof AccessConvertContext ? ((AccessConvertContext)mageContext).context : mageContext);
		//		if (mageContext instanceof AccessConvertContext) {
		//			this.callStack = ((AccessConvertContext)mageContext).callStack;
		//		}

		this.currentClass = getCurrentClass();
		this.element = element;
	}

	public PhpAccess getElement() {
		return element;
	}

	private MageExpression fromFunction(PhpAccessFunction phpFunction) {
		//		PhpExpression phpName = phpFunction.getName();
		//		MageExpression name;
		//		MageClassMethod functionSignature = null;
		//		if (phpName instanceof PhpAccessVariable) {
		//			PhpAccessVariable v = (PhpAccessVariable)name;
		//			if (v.isLiteral()) {
		//				functionSignature = currentClass.getFunction(v.getName());
		//			}
		//		} else {
		//			name = context.buildExpression(phpName);
		//			PhpType type = name.getType();
		//			if(type instanceof PhpTypeClass) {
		//				((PhpTypeClass)type).getSignature(phpIndex)
		//			}
		//
		//		}
		//
		//		AccessConverterFunction converter = AccessConverterFunction.getFunctionConverter(phpFunction);
		//		return converter != null ? converter.convert(this) : fromFunction(phpFunction, null);

		String name = PhpAccessUtils.getLiteralFunctionName(phpFunction);
		if (name != null) {
			MageClassMethod functionSignature = currentClass.getFunction(phpFunction);
			if (functionSignature != null) {
				return fromFunction(phpFunction, functionSignature);
			}
		}

		AccessConverterFunction converter = AccessConverterFunction.getFunctionConverter(phpFunction);
		return converter != null ? converter.convert(this) : fromFunction(phpFunction, null);
	}

	public MageAccessElement fromFunction(PhpAccessFunction phpFunctionCall, MageFunctionSignature functionSignature) {
		MageExpression name = context.buildExpression(phpFunctionCall.getName());

		List<PhpExpression> phpArgs = phpFunctionCall.getArgs();
		List<MageExpression> args = new ArrayList<MageExpression>(phpArgs.size());
		for (PhpExpression phpArg : phpArgs) {
			args.add(context.buildExpression(phpArg));
		}

		if (functionSignature != null) {
			List<MageVariableDeclaration> params = functionSignature.getParameters();
			for (int i = 0; i < args.size() && i < params.size(); i++) {
				PhpType type = PhpTypeUtils.select(args.get(i).getType(), params.get(i).getType());
				args.get(i).setType(type);
				params.get(i).setType(type);
			}

			if (args.size() < params.size()) {
				for (int i = args.size(); i < params.size(); i++) {
					MageVariableDeclaration param = params.get(i);
					MageExpression defaultValue = param.getDefaultValue();
					if (defaultValue != null) {
						args.add(defaultValue);
					} else {
						break;
					}
				}
			}
		}

		MageAccessFunction result = new MageAccessFunction(name, args);
		if (functionSignature != null) {
			result.setType(functionSignature.getReturnType());
		}

		return result;
	}

	public MageAccessElement byPassFunction() {
		return fromFunction((PhpAccessFunction)element, null);
	}

	private MageAccessElement fromVariable(PhpAccessVariable v) {
		if (v.isLiteral()) {
			return new MageAccessConstant(v.getName());
		} else {
			return getVariableOrRegist(v, null, null);
		}
	}

	private MageExpression fromClass(PhpAccessClass c) {
		MageExpression name = buildExpression(c.getName());
		MageExpression index = buildExpression(c.getIndex());

		AccessConverter converter = null;
		if (name instanceof MageAccessVariable) {
			String nameString = ((MageAccessVariable)name).getName();
			if ("this".equals(nameString)) {
				converter = createClassConverter(currentClass.getName());
			} else if ("super".equals(nameString)) {
				converter = createClassConverter(currentClass.getExtendsClass().getName());
			}
		} else if (name instanceof MageAccessConstant) {
			String className = ((MageAccessConstant)name).getValue();
			if ("self".equals(className)) {
				converter = createClassConverter(currentClass.getName());
			} else {
				converter = createClassConverter(className);
			}
		}

		PhpType type = name.getType();
		if (converter == null) {
			if (PhpTypeUtils.isUndecided(type)) {
				if (MageAccessUtils.isNotFunction(index)) {
					if (index instanceof MageAtomNumber) {
						type = PhpTypeFactory.LIST;
					} else {
						type = PhpTypeFactory.MAP;
					}
					name.setType(type);
				}
			} else {
				Class<?> javaClass = type.getJavaClass();
				if (javaClass != null && javaClass.isArray()) {
					return new MageAccessArray(name, index, PhpTypeFactory.getType(javaClass.getComponentType()));
				}
			}

			if (type == PhpTypeFactory.LIST) {
				return new MageAccessList(name, index);
			} else if (type == PhpTypeFactory.MAP) {
				return new MageAccessMap(name, index);
			}
		}

		if (converter == null) {
			converter = createClassConverter(type);
		}

		return converter.convert(this);
	}

	public static AccessConverter createClassConverter(PhpType type) {
		if (type != null) {
			return createClassConverter(type.getName());
		} else {
			return AccessConverterClass.BY_PASS;
		}
	}

	public static AccessConverterClass createClassConverter(String className) {
		AccessConverterClass converter = AccessConverterClass.getClassConverter(className);
		if (converter == null) {
			converter = AccessConverterClass.BY_PASS;
		}

		return converter;
	}

	public MageExpression convert() {
		if (element instanceof PhpAccessClass) {
			return fromClass((PhpAccessClass)element);
		} else if (element instanceof PhpAccessFunction) {
			return fromFunction((PhpAccessFunction)element);
		} else if (element instanceof PhpAccessVariable) {
			return fromVariable((PhpAccessVariable)element);
		} else {
			throw new IllegalArgumentException("'" + element.getClass().getSimpleName() + "' is not support convert.");
		}
	}
}
