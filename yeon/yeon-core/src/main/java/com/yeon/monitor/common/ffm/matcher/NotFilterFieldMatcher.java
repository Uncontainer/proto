package com.yeon.monitor.common.ffm.matcher;

import com.yeon.monitor.common.MomString;
import com.yeon.monitor.common.ffm.FilterFieldMatcher;

/**
 * 
 * @author pulsarang
 */
public class NotFilterFieldMatcher implements FilterFieldMatcher {
	private final FilterFieldMatcher matcher;

	public NotFilterFieldMatcher(FilterFieldMatcher matcher) {
		this.matcher = matcher;
	}

	public FilterFieldMatcher getMatcher() {
		return matcher;
	}

	@Override
	public boolean match(String str) {
		return !matcher.match(str);
	}

	@Override
	public boolean match(MomString str) {
		return !matcher.match(str);
	}

	@Override
	public String toString() {
		return "NOT_" + matcher.toString();
	}
}
