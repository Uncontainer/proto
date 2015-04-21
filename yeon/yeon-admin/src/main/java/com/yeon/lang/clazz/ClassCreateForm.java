package com.yeon.lang.clazz;

import com.yeon.lang.impl.MapClass;
import com.yeon.lang.resource.ResourceCreateForm;

import java.util.Collections;
import java.util.List;

public class ClassCreateForm extends ResourceCreateForm {
	private String alias;
	private List<String> superClassIds;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public List<String> getSuperClassIds() {
		if (superClassIds == null) {
			return Collections.emptyList();
		} else {
			return superClassIds;
		}
	}

	public void setSuperClassIds(List<String> superClassIds) {
		this.superClassIds = superClassIds;
	}

	public MapClass getMapClass() {
		MapClass clazz = new MapClass();

		// TODO type을 class로 고정.

		return clazz;
	}
}
