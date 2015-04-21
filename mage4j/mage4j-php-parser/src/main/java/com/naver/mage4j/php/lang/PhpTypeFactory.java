package com.naver.mage4j.php.lang;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.naver.mage4j.php.mage.MageClass;
import com.naver.mage4j.php.mage.PhpTypeMageClass;

public class PhpTypeFactory {
	public static final PhpType STRING = new PhpTypeVariable("String", String.class);
	public static final PhpType BOOLEAN = new PhpTypeVariable("boolean", Boolean.class);
	public static final PhpType OBJECT = new PhpTypeVariable("Object", Object.class);
	public static final PhpType VOID = new PhpTypeVariable("void", void.class);
	public static final PhpType INTEGER = new PhpTypeVariable("int", Integer.class);
	public static final PhpType REAL = new PhpTypeVariable("double", Double.class);
	public static final PhpType MAP = new PhpTypeVariable("Map", Map.class);
	public static final PhpType LIST = new PhpTypeVariable("List", List.class);
	public static final PhpType JAVA_ARRAY = new PhpTypeVariable("T[]", Object[].class);
	public static final PhpType UNDECIDED = new PhpTypeAdapter("NoType");
	public static final PhpType RUNTIME = new PhpTypeAdapter("Variable");
	public static final PhpType ARRAY = new PhpTypeAdapter("array");

	static Map<String, PhpType> cache = new HashMap<String, PhpType>();

	static {
		regist(STRING);
		regist(BOOLEAN, "bool", "Boolean");
		regist(OBJECT, "mixed");
		regist(VOID);
		regist(INTEGER, "Integer", "integer");
		regist(REAL);
		regist(MAP);
		regist(LIST);
		regist(ARRAY);
		regist(new PhpTypeAdapter("Zend_Date"));
	}

	public static void regist(PhpType type, String... names) {
		cache.put(type.getName().toLowerCase(), type);
		for (String name : names) {
			cache.put(name, type);
		}
	}

	public static PhpType getLazyLoading(String name) {
		return new PhpTypeLazyLoading(name);
	}

	public static PhpType get(String name) {
		return get(name, false);
	}

	public static PhpType get(String name, boolean primitive) {
		PhpType result = cache.get(name.toLowerCase());
		if (result == null) {
			Class<?> clazz = MageClassLoader.getContext().getJavaClass(name);
			if (clazz != null) {
				result = getType(clazz);
			}
		}

		if (result == null) {
			MageClass mageClass = MageClassLoader.getContext().load(name);
			if (mageClass != null) {
				result = new PhpTypeMageClass(mageClass);
			}
		}

		if (result == null) {
			result = new PhpTypeVariable(name, Object.class);
		}

		regist(result);

		return result;
	}

	public static PhpType getType(Class<?> type) {
		// TODO 캐싱하여 동일한 java clas에 대해서는 하나의 instance가 나오도록 함.
		return new PhpTypeJava(type);
	}
}
