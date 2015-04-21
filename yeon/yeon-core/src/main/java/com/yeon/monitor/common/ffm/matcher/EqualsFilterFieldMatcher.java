package com.yeon.monitor.common.ffm.matcher;

import com.yeon.monitor.common.MomString;

/**
 * 
 * @author pulsarang
 */
public class EqualsFilterFieldMatcher extends AbstractFilterFieldMatcher {
	public EqualsFilterFieldMatcher(String expression) {
		super(expression);
	}

	@Override
	public boolean nullSafeMatch(String str) {
		return expression.equals(str);
	}

	@Override
	public boolean nullSafeMatch(MomString str) {
		return momExpression.equals(str);
	}

	@Override
	public String toString() {
		return "EQUALS:" + expression;
	}
}
