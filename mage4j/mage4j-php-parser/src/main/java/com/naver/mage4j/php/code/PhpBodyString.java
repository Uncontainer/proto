package com.naver.mage4j.php.code;

public class PhpBodyString implements PhpStatement {
	private String body;

	public PhpBodyString(String body) {
		super();
		this.body = body;
	}

	public String getBody() {
		return body;
	}
}
