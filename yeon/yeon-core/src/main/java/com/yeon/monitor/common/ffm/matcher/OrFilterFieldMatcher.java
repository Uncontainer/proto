package com.yeon.monitor.common.ffm.matcher;

import com.yeon.monitor.common.MomString;
import com.yeon.monitor.common.ffm.FilterFieldMatcher;

/**
 * 
 * @author pulsarang
 */
public class OrFilterFieldMatcher extends FilterFieldMatcherComposite {
	public OrFilterFieldMatcher(FilterFieldMatcher... matchers) {
		super(Type.OR, matchers);
	}

	@Override
	public boolean match(String str) {
		for (FilterFieldMatcher matcher : matchers) {
			if (matcher.match(str)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean match(MomString str) {
		for (FilterFieldMatcher matcher : matchers) {
			if (matcher.match(str)) {
				return true;
			}
		}

		return false;
	}
}
