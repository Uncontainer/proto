package com.yeon.monitor.common.ffm.matcher;

import com.yeon.monitor.common.MomString;
import com.yeon.monitor.common.ffm.FilterFieldMatcher;

/**
 * 
 * @author pulsarang
 */
public abstract class AbstractFilterFieldMatcher implements FilterFieldMatcher {
	protected final String expression;
	protected final MomString momExpression;

	public AbstractFilterFieldMatcher(String expression) {
		this.expression = expression;
		this.momExpression = new MomString(expression);
	}

	@Override
	public boolean match(String str) {
		if (str == null) {
			return true;
		}

		return nullSafeMatch(str);
	}

	@Override
	public boolean match(MomString str) {
		if (str == null) {
			return true;
		}

		return nullSafeMatch(str);
	}

	public abstract boolean nullSafeMatch(String str);

	public abstract boolean nullSafeMatch(MomString str);
}
