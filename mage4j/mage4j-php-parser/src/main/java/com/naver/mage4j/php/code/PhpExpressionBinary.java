package com.naver.mage4j.php.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PhpExpressionBinary implements PhpExpression {
	protected PhpExpression left;
	protected List<PhpBinaryOperator> operations;
	protected List<PhpExpression> rights;

	public static PhpExpressionBinary create(PhpExpression left, String operation, PhpExpression right) {
		return new PhpExpressionBinary(left, Collections.singletonList(PhpBinaryOperator.fromPhpOperator(operation)), Collections.singletonList(right));
	}

	public static PhpExpressionBinary create(PhpExpression left, String operation, List<PhpExpression> rights) {
		List<PhpBinaryOperator> operations = new ArrayList<PhpBinaryOperator>(rights.size());
		PhpBinaryOperator op = PhpBinaryOperator.fromPhpOperator(operation);
		for (int i = 0; i < rights.size(); i++) {
			operations.add(op);
		}

		return new PhpExpressionBinary(left, operations, rights);
	}

	public static PhpExpressionBinary create(PhpExpression left, List<String> operations, List<PhpExpression> rights) {
		if (operations.isEmpty()) {
			throw new IllegalArgumentException();
		}

		return new PhpExpressionBinary(left, operations.stream().map(s -> PhpBinaryOperator.fromPhpOperator(s)).collect(Collectors.toList()), rights);
	}

	public PhpExpressionBinary(PhpExpression left, List<PhpBinaryOperator> operations, List<PhpExpression> rights) {
		super();
		if (operations.size() != rights.size()) {
			throw new IllegalArgumentException();
		}

		this.left = left;
		this.operations = operations;
		this.rights = rights;
	}

	public PhpExpressionBinary(PhpExpression left, PhpBinaryOperator operation, PhpExpression right) {
		this(left, Collections.singletonList(operation), Collections.singletonList(right));
	}

	public PhpExpression getLeft() {
		return left;
	}

	public List<PhpBinaryOperator> getOperations() {
		return operations;
	}

	public List<PhpExpression> getRights() {
		return rights;
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
