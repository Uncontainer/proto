package com.yeon.lang;

import com.yeon.lang.clazz.MapClassService;
import com.yeon.lang.impl.MapClassBuilder;

public class ClassInitializer {
	MapClassService classService;

	public void test() {
		classService.add(MapClassBuilder.aClass().id(BuiltinClass.CLASS.getId()).name("en", "class").build());
		classService.add(MapClassBuilder.aClass().id(BuiltinType.STRING.getId()).name("en", "string").build());
	}
}
