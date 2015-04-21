package com.naver.mage4j.php.mage;

import com.naver.mage4j.php.code.PhpAccessVariable;
import com.naver.mage4j.php.code.PhpControlForeach;
import com.naver.mage4j.php.code.PhpPair;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.MageContext.VariableScopeType;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageControlForeach implements MageControlStatement, MageStatementInitializable<PhpControlForeach> {
	private MageExpression target;
	private MageStatement variable;
	private MageStatement body;

	MageControlForeach() {
		super();
	}

	@Override
	public void init(MageContext context, PhpControlForeach statement) {
		this.target = context.buildExpression(statement.getTarget());
		if (statement.getVariable() instanceof PhpPair) {
			this.variable = context.buildStatement(statement.getVariable());
			target.setType(PhpTypeFactory.MAP);
		} else {
			this.variable = context.registVariable((PhpAccessVariable)statement.getVariable(), null, VariableScopeType.FOR);
			//			PhpType type = target.getType();
			//			if (PhpTypeUtils.isDecided(type)) {
			//				// TODO collection의 element type 추출 로직 추가.
			//				context.visit(variable, type).visit(body);
			//			}
		}

		this.body = context.buildStatement(statement.getBody());
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		if (variable instanceof MagePair) {
			MagePair pair = (MagePair)variable;
			MageExpression key = pair.getKey();
			MageExpression value = pair.getValue();
			context.appendCode("for( Map.Entry<").visit(key.getType()).appendCode(",").visit(value.getType()).appendCode("> each : ").visit(target).appendCode(".entrySet() ) {")
				.appendNewLine().increaseIndent()
				.visit(key.getType()).appendCode(" ").visit(key).appendCode(" = ").appendCode("each.getKey();").appendNewLine()
				.visit(value.getType()).appendCode(" ").visit(value).appendCode(" = ").appendCode("each.getValue();").appendNewLine();
		} else {
			MageExpression expr = (MageAccessVariable)variable;
			context.appendCode("for( ").visit(expr.getType()).appendCode(" ").visit(expr).appendCode(" : ").visit(target).appendCode(" ) {")
				.appendNewLine().increaseIndent();
		}

		context.visit(body)
			.appendNewLine().decreaseIndent().appendCode("}");
	}
}
