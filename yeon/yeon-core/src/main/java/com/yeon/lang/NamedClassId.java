package com.yeon.lang;

import com.yeon.YeonContext;

public class NamedClassId implements NamedResourceIdentifiable {
	private String name;
	private String classId;
	// TODO 상수로 분리
	public static final String LOCALE = "__";

	public NamedClassId(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public LocalValue getLocalName() {
		return new LocalValue(LOCALE, name);
	}

	@Override
	public String getResourceId() {
		String ci = classId;
		if (ci == null) {
			ResourceGetCondition condition = ResourceGetCondition.createClassByAliasCondition(name);
			ci = classId = ResourceServiceUtils.getId(condition);
		}

		return ci;
	}

	@Override
	public String toString() {
		return name;
	}
}
