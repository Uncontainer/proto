package com.yeon.lang.sql;

public class LocalNameReference extends NameReference {
	protected String locale;

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
}
