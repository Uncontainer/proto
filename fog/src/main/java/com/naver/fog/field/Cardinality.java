package com.naver.fog.field;

public class Cardinality {
	private final int minimum;
	private final int maxium;

	public Cardinality(int minimum, int maxium) {
		super();
		validate(minimum, maxium);
		this.minimum = minimum;
		this.maxium = maxium;
	}

	public Cardinality(String expression) {
		int sepIndex = expression.indexOf("..");
		if (sepIndex < 0) {
			minimum = maxium = Integer.parseInt(expression);
		} else if (sepIndex == 0) {
			minimum = 0;
			if (expression.length() == 2) {
				maxium = Integer.MAX_VALUE;
			} else {
				maxium = Integer.parseInt(expression.substring(2));
			}
		} else if (sepIndex == expression.length() - 2) {
			minimum = Integer.parseInt(expression.substring(0, sepIndex));
			maxium = Integer.MAX_VALUE;
		} else {
			minimum = Integer.parseInt(expression.substring(0, sepIndex));
			maxium = Integer.parseInt(expression.substring(sepIndex + 2));
		}

		validate(minimum, maxium);
	}

	private void validate(int minimum, int maxium) {
		if (minimum > maxium) {
			throw new IllegalArgumentException("Minimum value is greater than maximum value.");
		}

		if (minimum < 0) {
			throw new IllegalArgumentException("Cardinality must not be a negative integer.");
		}

		if (minimum == maxium && minimum < 1) {
			throw new IllegalArgumentException("Exact cardinality must be a positive integer.");
		}
	}

	public int getMinimum() {
		return minimum;
	}

	public int getMaxium() {
		return maxium;
	}

	@Override
	public String toString() {
		if (minimum == maxium) {
			return Integer.toString(minimum);
		}

		return minimum + ".." + maxium;
	}
}
