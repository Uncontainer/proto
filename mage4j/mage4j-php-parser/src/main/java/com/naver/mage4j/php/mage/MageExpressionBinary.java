package com.naver.mage4j.php.mage;

import java.util.Arrays;
import java.util.List;

import com.naver.mage4j.php.code.PhpBinaryOperator;
import com.naver.mage4j.php.lang.PhpTypeFactory;
import com.naver.mage4j.php.mage.visitor.JavaCodeGenerateContext;

public class MageExpressionBinary extends MageExpressionTypeDelegated {
	protected final MageExpression left;
	protected final List<PhpBinaryOperator> operations;
	protected final List<MageExpression> rights;

	public MageExpressionBinary(MageExpression left, PhpBinaryOperator op, MageExpression right) {
		this(left, Arrays.asList(op), Arrays.asList(right));
	}

	public MageExpressionBinary(MageExpression left, List<PhpBinaryOperator> operations, List<MageExpression> rights) {
		super();
		if (operations == null || rights == null || operations.size() != rights.size()) {
			throw new IllegalArgumentException();
		}

		this.left = left;
		this.operations = operations;
		this.rights = rights;

		for (int i = 0; i < operations.size(); i++) {
			switch (operations.get(i)) {
				case CONCATENATE:
				case CONCATENATE_ASSIGN:
					rights.get(i).setType(PhpTypeFactory.STRING);
					if (i == 0) {
						left.setType(PhpTypeFactory.STRING);
					}
			}
		}
	}

	public MageExpression getLeft() {
		return left;
	}

	public List<PhpBinaryOperator> getOperations() {
		return operations;
	}

	public List<MageExpression> getRights() {
		return rights;
	}

	@Override
	public void visitJava(JavaCodeGenerateContext context) {
		if (rights.isEmpty()) {
			context.visit(left);
			return;
		}

		visitJavaOperand(context, left);

		for (int i = 0; i < operations.size(); i++) {
			context.appendCode(" " + operations.get(i).getJava() + " ");
			visitJavaOperand(context, rights.get(i));
		}
	}

	private void visitJavaOperand(JavaCodeGenerateContext context, MageExpression operand) {
		boolean needsParan = true;
		if (operand instanceof MageAtom) {
			needsParan = false;
		} else if (operand instanceof MageExpressionBinary) {
			needsParan = !((MageExpressionBinary)operand).rights.isEmpty();
		} else if (operand instanceof MageAccessElement) {
			needsParan = false;
		}

		if (needsParan) {
			context.appendCode("(").visit(operand).appendCode(")");
		} else {
			context.visit(operand);
		}
	}

	@Override
	public MageExpression getDeletateElement() {
		return left;
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append(left);

		for (int i = 0; i < operations.size(); i++) {
			output.append(" " + operations.get(i).getJava() + " ");
			output.append(rights.get(i));
		}

		return output.toString();
	}
}
