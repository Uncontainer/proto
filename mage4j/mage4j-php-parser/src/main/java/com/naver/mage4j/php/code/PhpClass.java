package com.naver.mage4j.php.code;

import java.util.ArrayList;
import java.util.List;

public class PhpClass implements PhpStatement, PhpDocumentable, PhpScope {
	private String modifier;
	private String name;
	private String extendsClassName;
	private List<String> implementsInterfaceNames;
	private List<PhpClassField> fields = new ArrayList<PhpClassField>();
	private List<PhpClassFunction> methods = new ArrayList<PhpClassFunction>();
	private List<PhpClassMember> members = new ArrayList<PhpClassMember>();

	private List<String> documentComments;

	public PhpClass() {
		super();
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtendsClassName() {
		return extendsClassName;
	}

	public void setExtendsClassName(String extendsClassName) {
		this.extendsClassName = extendsClassName;
	}

	public List<String> getImplementsInterfaceNames() {
		return implementsInterfaceNames;
	}

	public void setImplementsInterfaceNames(List<String> implementsInterfaceNames) {
		this.implementsInterfaceNames = implementsInterfaceNames;
	}

	public List<PhpClassMember> getMembers() {
		return members;
	}

	public void addMember(PhpClassMember member) {
		if (member instanceof PhpClassField) {
			fields.add((PhpClassField)member);
		} else if (member instanceof PhpClassFunction) {
			methods.add((PhpClassFunction)member);
		} else {
			throw new IllegalArgumentException("UnsupporetedClass: " + member.getClass());
		}

		this.members.add(member);
	}

	@Override
	public List<String> getDocumentComments() {
		return documentComments;
	}

	@Override
	public void setDocumentComments(List<String> documentComments) {
		this.documentComments = documentComments;
	}

	public PhpFunction getFunction(String name) {
		for (PhpClassFunction method : methods) {
			if (method.getName().equals(name)) {
				return method;
			}
		}

		return null;
	}

	@Override
	public String toString() {
		return "class " + name;
	}
}
