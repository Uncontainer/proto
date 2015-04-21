package com.naver.fog.ui.layout;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.naver.fog.ui.layout.component.BooleanLayout;
import com.naver.fog.ui.layout.component.DateLayout;
import com.naver.fog.ui.layout.component.LabelLayout;
import com.naver.fog.ui.layout.component.NumberLayout;
import com.naver.fog.ui.layout.component.StringLayout;
import com.naver.fog.ui.layout.composite.LayoutWithLabel;
import com.naver.fog.ui.layout.composite.LabeledLinearLayout;
import com.naver.fog.ui.layout.composite.LinearLayout;

public class LayoutFactory {
	private static final Map<String, Constructor<? extends AbstractLayout>> layoutConstructorMap = new HashMap<String, Constructor<? extends AbstractLayout>>();

	static {
		LayoutFactory.regist(StringLayout.ALIAS, StringLayout.class);
		LayoutFactory.regist(BooleanLayout.ALIAS, BooleanLayout.class);
		LayoutFactory.regist(NumberLayout.ALIAS, NumberLayout.class);
		LayoutFactory.regist(DateLayout.ALIAS, DateLayout.class);
		LayoutFactory.regist(LabelLayout.ALIAS, LabelLayout.class);

		LayoutFactory.regist(LinearLayout.ALIAS, LinearLayout.class);
		LayoutFactory.regist(LabeledLinearLayout.ALIAS, LabeledLinearLayout.class);
		LayoutFactory.regist(LayoutWithLabel.ALIAS, LayoutWithLabel.class);
	}

	public static <T extends AbstractLayout> T newInstance(String alias, long targetResourceId) {
		Constructor<? extends AbstractLayout> constructor = layoutConstructorMap.get(alias);
		if (constructor == null) {
			throw new IllegalArgumentException("Unregistered layout alias:" + alias);
		}

		try {
			@SuppressWarnings("unchecked")//
			T instance = (T)constructor.newInstance(targetResourceId);
			return instance;
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends AbstractLayout> T clone(Layout layout, long targetResourceId) {
		return (T)AbstractLayout.fromLayoutHtml(AbstractLayout.toLayoutHtml(layout).getMarkup(), targetResourceId);
	}

	public synchronized static void regist(String alias, Class<? extends AbstractLayout> clazz) {
		if (layoutConstructorMap.containsKey(alias)) {
			throw new IllegalArgumentException("Layout has already registered.(" + alias + ":" + clazz.getCanonicalName() + ")");
		}

		try {
			Constructor<? extends AbstractLayout> constructor = clazz.getConstructor(long.class);
			layoutConstructorMap.put(alias, constructor);
		} catch (SecurityException e) {
			throw new IllegalArgumentException("Layout requires default constructor.(" + clazz.getCanonicalName() + ")");
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("Layout requires default constructor.(" + clazz.getCanonicalName() + ")");
		}
	}
}
