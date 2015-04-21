package com.naver.mage4j.core.mage.core.model.translate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Translate expression object
 */
public class Mage_Core_Model_Translate_Expr {
	/**
	 * Translate expression object
	 */
	protected String _text;

	/**
	 * Translate expression object
	 */
	protected String _module;

	/**
	 * Translate expression object
	 * 
	 */
	public Mage_Core_Model_Translate_Expr(String text/* = "" */, String module/* = "" */) {
		this._text = text;
		this._module = module;
	}

	/**
	 * Translate expression object
	 * 
	 */
	public Mage_Core_Model_Translate_Expr setText(String text) {
		this._text = text;
		return this;
	}

	/**
	 * Translate expression object
	 * 
	 */
	public Mage_Core_Model_Translate_Expr setModule(String module) {
		this._module = module;
		return this;
	}

	/**
	 * Retrieve expression text
	 * 
	 * @return string
	 */
	public String getText() {
		return this._text;
	}

	/**
	 * Retrieve expression module
	 * 
	 * @return string
	 */
	public String getModule() {
		return this._module;
	}

	/**
	 * Retrieve expression code
	 * 
	 * @param separator
	 * @return string
	 */
	public String getCode(String separator/* = "::" */) {
		return this.getModule() + separator + this.getText();
	}

	public static void main(String[] args) {
		String _tokenRegex = "\\{\\{\\{(.*?)\\}\\}\\{\\{(.*?)\\}\\}\\{\\{(.*?)\\}\\}\\{\\{(.*?)\\}\\}\\}";
		String tagRegExp = "(?i)<([a-z]+)\\s*?[^>]+?((" + _tokenRegex + ")[^>]*?)+\\\\\\\\?/?>";

		List<String> matches = new ArrayList<String>();
		match(tagRegExp, "<a href='www.naver.com' target=\"_blank\"></a>", matches);
		System.out.println(matches);
		System.out.println(_tokenRegex);
	}

	public static boolean match(String pattern, String subject, List<String> matches) {
		Matcher matcher = Pattern.compile(pattern).matcher(subject);
		if (!matcher.matches()) {
			return false;
		}

		for (int i = 1; i < matcher.groupCount(); i++) {
			matches.add(matcher.group(i));
		}

		return true;
	}

}