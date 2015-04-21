package com.naver.fog.web;

public enum ViewMode {
	ADD("create", "추가", false)
	, READ("", "조회", true)
	, EDIT("modify", "수정", false);

	public final String path;
	final String text;
	final boolean readOnly;

	private ViewMode(String path, String text, boolean readOnly) {
		this.path = path;
		this.text = text;
		this.readOnly = readOnly;
	}

	public String getPath() {
		return path;
	}

	public String getText() {
		return text;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public boolean isModifiable() {
		return !readOnly;
	}
}
