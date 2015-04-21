package com.naver.mage4j.php.mage;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.php.code.PhpClassField;
import com.naver.mage4j.php.code.PhpExpression;
import com.naver.mage4j.php.doc.PhpDocField;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.converter.PhpTypeUtils;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageClassField implements MageClassMember, MageStatementInitializable<PhpClassField> {
	private PhpClassField original;

	private PhpDocField comment;
	private PhpType type;

	private MageExpression value;

	MageClassField() {
	}

	@Override
	public String getName() {
		return original.getName();
	}

	@Override
	public void init(MageContext context, PhpClassField field) {
		this.original = field;
		context.getCurrentClass().setField(field, this);

		List<String> commentStrings = field.getDocumentComments();
		if (CollectionUtils.isNotEmpty(commentStrings)) {
			comment = new PhpDocField(commentStrings.get(commentStrings.size() - 1));
		}

		if (comment != null) {
			type = comment.getType();
		}

		MageAccessVariable variable = context.registVariable(field.getName(), null, MageContext.VariableScopeType.CLASS);
		PhpExpression originalValue = field.getValue();
		if (originalValue != null) {
			if (type != null && type.getJavaClass() != null) {
				value = context.buildStatement(field.getValue());
			} else {
				value = context.buildExpression(field.getValue(), type);
			}

			type = PhpTypeUtils.select(type, value.getType());
			variable.setType(type);
		}
	}

	public PhpType getType() {
		return type != null ? type : PhpTypeFactory.UNDECIDED;
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		if (comment != null && !comment.isBlank()) {
			context.appendMultilineCode(comment.toString()).appendNewLine();
		}

		if (original.isConstant()) {
			context.appendCode("public static final");
		} else {
			context.appendCode(StringUtils.join(original.getModifiers(), " "));
		}
		context.appendCode(" ").visit(getType()).appendCode(" ").appendCode(original.getName());
		if (value != null) {
			context.appendCode(" = ").visit(value);
		}
	}
}
