package com.yeon.monitor.common.ffm.matcher;

import com.yeon.monitor.common.MomString;
import com.yeon.monitor.common.ffm.FilterFieldMatcher;

/**
 * 
 * @author pulsarang
 */
public class AndFilterFieldMatcher extends FilterFieldMatcherComposite {
	public AndFilterFieldMatcher(FilterFieldMatcher... matchers) {
		super(Type.AND, matchers);
	}

	@Override
	public boolean match(String str) {
		for (FilterFieldMatcher matcher : matchers) {
			if (!matcher.match(str)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean match(MomString str) {
		for (FilterFieldMatcher matcher : matchers) {
			if (!matcher.match(str)) {
				return false;
			}
		}

		return true;
	}
}
