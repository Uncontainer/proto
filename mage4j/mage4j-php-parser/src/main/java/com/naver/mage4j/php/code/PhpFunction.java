package com.naver.mage4j.php.code;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.php.lang.PhpType;

public class PhpFunction implements PhpStatement, PhpDocumentable, PhpScope {
	protected PhpType returnType;
	protected String name;
	protected List<PhpVariableDeclaration> params;
	protected PhpStatementBlock body;

	private List<String> documentComments;

	public PhpFunction(String name, List<PhpVariableDeclaration> params) {
		this(name, params, null);
	}

	public PhpFunction(String name, List<PhpVariableDeclaration> params, PhpStatementBlock body) {
		super();
		this.name = name;
		this.params = params;
		this.body = body;
	}

	@Override
	public String getName() {
		return name;
	}

	public PhpType getReturnType() {
		return returnType;
	}

	public List<PhpVariableDeclaration> getParams() {
		return params != null ? params : Collections.emptyList();
	}

	public void setParams(List<PhpVariableDeclaration> params) {
		this.params = params;
	}

	public PhpStatementBlock getBody() {
		return body;
	}

	public void setBody(PhpStatementBlock body) {
		this.body = body;
	}

	@Override
	public List<String> getDocumentComments() {
		return documentComments;
	}

	@Override
	public void setDocumentComments(List<String> documentComments) {
		this.documentComments = documentComments;
	}

	@Override
	public String toString() {
		return returnType + " " + name + "(" + StringUtils.join(getParams().stream().map(s -> s.toString()).collect(Collectors.toList()), ", ") + ")";
	}
}
