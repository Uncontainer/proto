package com.naver.mage4j.php.code;

import java.util.List;

public class PhpClassFunction extends PhpFunction implements PhpClassMember {
	private List<String> modifiers;
	private boolean reference = false;
	private String className;

	public PhpClassFunction(List<String> modifiers, String name, List<PhpVariableDeclaration> params) {
		this(modifiers, name, params, null);
	}

	public PhpClassFunction(List<String> modifiers, String name, List<PhpVariableDeclaration> params, PhpStatementBlock body) {
		super(name, params, body);
		this.modifiers = modifiers;
	}

	public List<String> getModifiers() {
		return modifiers;
	}

	public void setModifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}

	public boolean isReference() {
		return reference;
	}

	public void setReference(boolean reference) {
		this.reference = reference;
	}
}
