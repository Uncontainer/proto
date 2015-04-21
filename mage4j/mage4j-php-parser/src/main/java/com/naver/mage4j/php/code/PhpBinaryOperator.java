package com.naver.mage4j.php.code;

import java.util.HashMap;
import java.util.Map;

public enum PhpBinaryOperator {
	NULL("", ""),
	// weak logical
	WEAK_LOGICAL_OR("or", "||"),
	WEAK_LOGICAL_XOR("xor"),
	WEAK_LOGICAL_AND("and", "&&"),

	// assign
	ASSIGN("="),
	PLUS_ASSIGN("+="),
	MINUS_ASSIGN("-="),
	MULTIPLY_ASSIGN("*="),
	DIVIDE_ASSIGN("/="),
	CONCATENATE_ASSIGN(".=", "+="),
	MODULUS_ASSIGN("%="),
	AND__ASSIGN("&="),
	OR_ASSIGN("|="),
	XOR_ASSIGN("^="),
	SHIFT_LEFT_ASSIGN("<<="),
	SHIFT_RIGHT_ASSIGN(">>="),

	// logical
	LOCIAL_OR("||"),
	LOGICAL_AND("&&"),
	// bitwise
	BITWISE_OR("|"),
	BITWISE_XOR("^"),
	BITWISE_AND("&"),
	// equality
	EQUAL("=="),
	NOT_EQUAL("!="),
	STRONG_EQUAL("===", "=="),
	STRONG_NOT_EQUAL("!==", "!="),
	// comparison
	LESS("<"),
	LESS_OR_EQUAL("<="),
	GREATER(">"),
	GREATER_OR_EQUAL(">="),
	INEQUAL("<>"),
	// shift
	SHIFT_LEFT("<<"),
	SHIFT_RIGHT(">>"),
	// addition
	PLUS("+"),
	MINUS("-"),
	CONCATENATE(".", "+"),
	// multiplication
	MULTIPLY("*"),
	DIVIDE("/"),
	MODULUS("%");

	final String php;
	final String java;

	PhpBinaryOperator(String text) {
		this(text, text);
	}

	PhpBinaryOperator(String php, String java) {
		this.php = php;
		this.java = java;
	}

	public String getPhp() {
		return php;
	}

	public String getJava() {
		return java;
	}

	static Map<String, PhpBinaryOperator> operatorByPhp;

	public synchronized static PhpBinaryOperator fromPhpOperator(String phpOperator) {
		if (operatorByPhp == null) {
			operatorByPhp = new HashMap<String, PhpBinaryOperator>();
			for (PhpBinaryOperator operator : PhpBinaryOperator.values()) {
				operatorByPhp.put(operator.php, operator);
			}
		}

		return operatorByPhp.get(phpOperator);
	}
}
