package com.naver.fog.field;

public enum FramedFieldType {
	VALUE(1)
	, REFERENCE(2);

	final int id;

	private FramedFieldType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
