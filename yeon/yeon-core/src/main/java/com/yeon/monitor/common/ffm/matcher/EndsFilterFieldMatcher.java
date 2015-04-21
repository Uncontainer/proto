package com.yeon.monitor.common.ffm.matcher;

import com.yeon.monitor.common.MomString;

/**
 * 
 * @author pulsarang
 */
public class EndsFilterFieldMatcher extends AbstractFilterFieldMatcher {
	public EndsFilterFieldMatcher(String expression) {
		super(expression);
	}

	@Override
	public boolean nullSafeMatch(String str) {
		return str.endsWith(expression);
	}

	@Override
	public boolean nullSafeMatch(MomString str) {
		return str.endsWith(momExpression);
	}

	@Override
	public String toString() {
		return "ENDS:" + expression;
	}
}
