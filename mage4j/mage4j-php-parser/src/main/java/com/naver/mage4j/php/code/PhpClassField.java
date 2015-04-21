package com.naver.mage4j.php.code;

import java.util.List;

import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;

public class PhpClassField implements PhpClassMember {
	private boolean constant;
	private List<String> modifiers;
	private String name;
	private PhpExpression value;
	private PhpType type;

	private List<String> documentComments;

	public PhpClassField(boolean constant, String name) {
		this(constant, name, null);
	}

	public PhpClassField(boolean constant, String name, PhpExpression value) {
		super();
		this.constant = constant;
		this.name = name;
		this.value = value;
	}

	public PhpClassField(List<String> modifiers, String name, PhpExpression value) {
		super();
		this.modifiers = modifiers;
		this.name = name;
		this.value = value;
	}

	public boolean isConstant() {
		return constant;
	}

	public List<String> getModifiers() {
		return modifiers;
	}

	@Override
	public String getName() {
		return name;
	}

	public PhpExpression getValue() {
		return value;
	}

	public void setValue(PhpExpression value) {
		this.value = value;
	}

	@Override
	public List<String> getDocumentComments() {
		return documentComments;
	}

	@Override
	public void setDocumentComments(List<String> documentComments) {
		this.documentComments = documentComments;
	}

	public PhpType getType() {
		return type != null ? type : PhpTypeFactory.UNDECIDED;
	}

	@Override
	public String toString() {
		return name + ((value != null) ? " = " + value : "");
	}
}
