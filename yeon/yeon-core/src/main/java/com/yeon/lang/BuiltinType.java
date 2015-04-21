package com.yeon.lang;

import com.yeon.lang.impl.MapClass;
import com.yeon.lang.impl.MapClassBuilder;

public enum BuiltinType {
	STRING(1),
	INTEGER(2),
	LONG(3),
	FLOAT(4),
	DOUBLE(5),
	SHORT(7),
	CHAR(8),
	DATE(9);

	public static final String PREFIX = "yeon.type.";

	final String id;
	final MapClass clazz;

	BuiltinType(int index, LocalValue... localNames) {
		this.id = ResourceType.CLASS.buildId("t" + index);
		MapClassBuilder builder = MapClassBuilder.aClass().id(id);
		builder.prototype(BuiltinClass.TYPE.getId());
		builder.name("en", this.name().toLowerCase());
		builder.name(NamedClassId.LOCALE, PREFIX + this.name().toLowerCase());
		for (LocalValue localName : localNames) {
			builder = builder.name(localName);
		}
		clazz = builder.build();
	}

	public String getId() {
		return id;
	}

	public Class getTypeClass() {
		return clazz;
	}
}
