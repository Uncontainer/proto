package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpNew;
import com.naver.mage4j.php.lang.PhpType;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageNew extends MageExpressionTypeDelegated implements MageStatementInitializable<PhpNew> {
	private MageAccessElement source;

	MageNew() {
		super();
	}

	@Override
	public void init(MageContext context, PhpNew statement) {
		source = context.buildExpression(statement.getSource());

		if (source instanceof MageAccessFunction) {
			MageExpression name = ((MageAccessFunction)source).getName();
			if (name instanceof MageAccessConstant) {
				PhpType type = PhpTypeFactory.get(((MageAccessConstant)name).getValue());
				name.setType(type);
				setType(type);
			}
		}

		//		if (PhpAccessUtils.isLiteral(element)) {
		//		}
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		context.addImport(getType());
		context.appendCode("new ").visit(source);
	}

	@Override
	protected MageAccessElement getDeletateElement() {
		return source;
	}
}
