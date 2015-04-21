package com.naver.mage4j.core.mage;

import com.naver.mage4j.php.lang.MageClassLoader;

public class MageInstanceLoader {
	private static final MageInstanceLoader INSTANCE = new MageInstanceLoader();

	public static final MageInstanceLoader get() {
		return INSTANCE;
	}

	public <T> T getInstance(String mageClassName) {
		Class<?> javaClass;
		try {
			javaClass = MageClassLoader.getContext().getJavaClassUnsafe(mageClassName);
			return (T)javaClass.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public Object getInstanceIfExist(String mageClassName) {
		Class<?> javaClass;
		try {
			javaClass = MageClassLoader.getContext().getJavaClassUnsafe(mageClassName);
		} catch (ClassNotFoundException e) {
			return null;
		}

		try {
			return javaClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
