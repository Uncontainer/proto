package com.naver.mage4j.php.code;

import java.util.ArrayList;
import java.util.List;

public class PhpInterface implements PhpStatement, PhpDocumentable, PhpScope {
	private String name;
	private List<String> extendsInterfaceNames;
	private List<PhpClassMember> members;

	private List<String> documentComments;

	public PhpInterface(String name, List<String> extendsInterfaceNames, List<PhpClassMember> members) {
		super();
		this.name = name;
		this.extendsInterfaceNames = extendsInterfaceNames;
		this.members = members;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getExtendsInterfaceNames() {
		return extendsInterfaceNames;
	}

	public void setExtendsInterfaceNames(List<String> extendsInterfaceNames) {
		this.extendsInterfaceNames = extendsInterfaceNames;
	}

	public List<PhpClassMember> getMembers() {
		return members;
	}

	public void addMember(PhpClassMember member) {
		if (members == null) {
			members = new ArrayList<PhpClassMember>();
		}
		members.add(member);
	}

	@Override
	public List<String> getDocumentComments() {
		return documentComments;
	}

	@Override
	public void setDocumentComments(List<String> documentComments) {
		this.documentComments = documentComments;
	}
}
