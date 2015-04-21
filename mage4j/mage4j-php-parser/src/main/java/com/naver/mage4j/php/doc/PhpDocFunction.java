package com.naver.mage4j.php.doc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.php.lang.PhpType;

public class PhpDocFunction extends PhpDocElementAbstract {
	List<PhpDocFunctionParam> params;
	PhpDocFunctionReturn returnValue;

	public PhpDocFunction(String str) {
		super(str);
	}

	@Override
	protected void parse(String type, String body) {
		switch (type) {
			case "param":
				if (params == null) {
					params = new ArrayList<PhpDocFunctionParam>();
				}
				params.add(new PhpDocFunctionParam(body));
				break;
			case "return":
				returnValue = new PhpDocFunctionReturn(body);
				break;
		}
	}

	@Override
	public boolean isBlank() {
		return CollectionUtils.isEmpty(params) && (returnValue == null || returnValue.isBlank()) && super.isBlank();
	}

	public PhpType getReturnType() {
		return returnValue != null ? returnValue.getType() : null;
	}

	public PhpType getParamType(String parameterName) {
		if (params == null) {
			return null;
		}

		for (PhpDocFunctionParam param : params) {
			if (parameterName.equals(param.name)) {
				return param.getType();
			}
		}

		return null;
	}

	public List<PhpDocFunctionParam> getParams() {
		return params != null ? params : Collections.emptyList();
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append("/**").append("\n");
		for (String line : StringUtils.split(description, "\n")) {
			out.append(" * ").append(line).append("\n");
			out.append(" * ").append("\n");
		}
		for (PhpDocFunctionParam param : getParams()) {
			out.append(" * @param ").append(param).append("\n");
		}
		if (returnValue != null) {
			out.append(" * @return ").append(returnValue).append("\n");
		}
		out.append(" */");

		return out.toString();
	}

	public static void main(String[] args) {
		String doc = "/**" + "\n" +
			" * Save block content to cache storage" + "\n" +
			" *" + "\n" +
			" * @param string $data" + "\n" +
			" * @return Mage_Core_Block_Abstract" + "\n" +
			" */";
		PhpDocFunction method = new PhpDocFunction(doc);
		System.out.println(method);
		//		boolean matches = " @s string ".matches("^(\\s)*@[^\\s]+.+");
		//		System.out.println(matches);
	}
}
