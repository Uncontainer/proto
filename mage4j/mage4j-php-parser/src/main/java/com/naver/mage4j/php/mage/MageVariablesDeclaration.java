package com.naver.mage4j.php.mage;

import java.util.ArrayList;
import java.util.List;

import com.naver.mage4j.php.code.PhpAccess;
import com.naver.mage4j.php.code.PhpVariablesDeclaration;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageVariablesDeclaration extends MageExpressionAbstract implements MageStatementInitializable<PhpVariablesDeclaration> {
	private List<MageAccessElement> names;
	private MageAccessElement value;

	MageVariablesDeclaration() {
	}

	@Override
	public void init(MageContext context, PhpVariablesDeclaration statement) {
		this.value = context.buildExpression(statement.getValue());
		this.names = new ArrayList<MageAccessElement>(statement.getNames().size());
		for (PhpAccess name : statement.getNames()) {
			if (name == null) {
				this.names.add(null);
			} else {
				this.names.add(context.buildExpression(name));
			}
		}

		// TODO type을 어떻게 처리할지 정의 필요
		setType(PhpTypeFactory.VOID);
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		for (int i = 0; i < names.size(); i++) {
			MageAccessElement name = names.get(i);
			if (name == null) {
				continue;
			}

			context.visit(name.getType()).appendCode(" ").visit(name);
			//			new MageAccessArray(name, new MageAtomNumber(i));
			context.appendCode("/* [NOT-IMPLEMENT] PhpVariableArray {value[index]} */");
			context.appendCode(";").appendNewLine();
		}
	}
}
