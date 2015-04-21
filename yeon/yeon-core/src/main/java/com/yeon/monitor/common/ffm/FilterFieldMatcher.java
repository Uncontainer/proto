package com.yeon.monitor.common.ffm;

import com.yeon.monitor.common.MomString;

/**
 * 
 * @author pulsarang
 */
public interface FilterFieldMatcher {
	boolean match(String str);

	boolean match(MomString str);

	FilterFieldMatcher TRUE = new FilterFieldMatcher() {
		@Override
		public boolean match(String str) {
			return true;
		}

		@Override
		public boolean match(MomString str) {
			return true;
		}
	};
}
