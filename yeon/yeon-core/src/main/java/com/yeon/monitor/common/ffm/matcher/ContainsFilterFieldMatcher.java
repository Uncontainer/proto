package com.yeon.monitor.common.ffm.matcher;

import com.yeon.monitor.common.MomString;

/**
 * 
 * @author pulsarang
 */
public class ContainsFilterFieldMatcher extends AbstractFilterFieldMatcher {
	public ContainsFilterFieldMatcher(String expression) {
		super(expression);
	}

	@Override
	public boolean nullSafeMatch(String str) {
		return str.contains(expression);
	}

	@Override
	public boolean nullSafeMatch(MomString str) {
		return str.contains(momExpression);
	}

	@Override
	public String toString() {
		return "CONTAINS:" + expression;
	}
}
