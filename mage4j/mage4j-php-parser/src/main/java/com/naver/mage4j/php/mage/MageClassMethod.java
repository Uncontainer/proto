package com.naver.mage4j.php.mage;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.php.code.PhpClassFunction;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageClassMethod extends MageFunction<PhpClassFunction> implements MageClassMember, MageStatementInitializable<PhpClassFunction> {
	private String className;

	MageClassMethod() {
	}

	@Override
	public void init(MageContext context, PhpClassFunction function) {
		// 제일 먼저 호출해줘야 한다.
		context.getCurrentClass().setMethod(function, this);

		super.init(context, function);
		className = context.getCurrentClass().getName();
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		visitComment(context);

		List<String> modifiers = ((PhpClassFunction)original).getModifiers();
		if (CollectionUtils.isNotEmpty(modifiers)) {
			context.appendCode(StringUtils.join(modifiers, " "));
			context.appendCode(" ");
		}

		String name = super.getName();
		if (name.endsWith("_construct")) {
			context.appendCode(className);
		} else {
			context.visit(returnType).appendCode(" ").appendCode(name);
		}
		context.visit(params, "(", ")", ", ");

		visitBody(context);
	}
}
