package com.naver.mage4j.php.mage;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.php.code.PhpClassField;
import com.naver.mage4j.php.code.PhpInterface;
import com.naver.mage4j.php.doc.PhpDocClass;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageInterface implements MageStatement, MageScope, MageStatementInitializable<PhpInterface> {
	private PhpInterface original;

	private List<MageClassMember> members;
	private PhpDocClass comment;

	MageInterface() {
		super();
	}

	@Override
	public void init(MageContext context, PhpInterface phpInterface) {
		this.original = phpInterface;
		this.members = context.buildStatements(phpInterface.getMembers());

		List<String> documentComments = phpInterface.getDocumentComments();
		if (CollectionUtils.isNotEmpty(documentComments)) {
			comment = new PhpDocClass(documentComments.get(documentComments.size() - 1));
		}
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		if (comment != null) {
			context.appendCode(comment.toString()).appendNewLine();
		}

		context.appendCode("public interface ").appendCode(original.getName());
		List<String> extendsInterfaceNames = original.getExtendsInterfaceNames();
		if (CollectionUtils.isNotEmpty(extendsInterfaceNames)) {
			context.appendCode(StringUtils.join(extendsInterfaceNames, ", "));
		}
		context.appendCode(" {").appendNewLine().increaseIndent();

		for (MageClassMember member : members) {
			context.visit(member);
			if (member instanceof PhpClassField) {
				context.appendCode(";");
			}
			context.appendNewLine().appendNewLine();
		}

		context.appendNewLine().decreaseIndent().appendCode("}");
	}

	@Override
	public String getName() {
		return original.getName();
	}
}
