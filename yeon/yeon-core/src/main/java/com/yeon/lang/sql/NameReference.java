package com.yeon.lang.sql;

public class NameReference implements ResourceReference {
	protected String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
