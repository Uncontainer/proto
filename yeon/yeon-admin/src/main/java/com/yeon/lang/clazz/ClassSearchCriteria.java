package com.yeon.lang.clazz;

public class ClassSearchCriteria {
	private ClassSearchType type = ClassSearchType.NAME;
	private String keyword;

	public ClassSearchType getType() {
		return type;
	}

	public void setType(ClassSearchType type) {
		this.type = type;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
