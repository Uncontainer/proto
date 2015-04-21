package com.yeon.monitor.common.ffm.matcher;

import com.yeon.monitor.common.MomString;

/**
 * 
 * @author pulsarang
 */
public class StartsFilterFieldMatcher extends AbstractFilterFieldMatcher {
	public StartsFilterFieldMatcher(String expression) {
		super(expression);
	}

	@Override
	public boolean nullSafeMatch(String str) {
		return str.startsWith(expression);
	}

	@Override
	public boolean nullSafeMatch(MomString str) {
		return str.startsWith(momExpression);
	}

	@Override
	public String toString() {
		return "STARTS:" + expression;
	}
}
