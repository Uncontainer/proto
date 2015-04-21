package com.yeon.monitor.common.ffm.matcher;

import com.yeon.monitor.common.ffm.FilterFieldMatcher;

import java.util.Arrays;

/**
 * 
 * @author pulsarang
 */
public abstract class FilterFieldMatcherComposite implements FilterFieldMatcher {
	protected final Type type;
	protected FilterFieldMatcher[] matchers;

	public FilterFieldMatcherComposite(Type type, FilterFieldMatcher... matchers) {
		this.type = type;

		int count = 0;
		for (FilterFieldMatcher matcher : matchers) {
			if (matcher != null) {
				count++;
			}
		}

		if (count == matchers.length) {
			this.matchers = matchers;
		} else {
			this.matchers = new FilterFieldMatcher[count];
			int index = 0;
			for (FilterFieldMatcher matcher : matchers) {
				if (matcher != null) {
					this.matchers[index++] = matcher;
				}
			}
		}
	}

	public void add(FilterFieldMatcher matcher) {
		if (matcher == null) {
			return;
		}

		matchers = Arrays.copyOf(matchers, matchers.length + 1);
		matchers[matchers.length - 1] = matcher;
	}

	public FilterFieldMatcher[] getMatchers() {
		return matchers;
	}

	@Override
	public String toString() {
		if (matchers.length == 0) {
			return "";
		}

		String sep = " " + type.name() + " ";

		StringBuilder builder = new StringBuilder();
		builder.append('(').append(matchers[0].toString());
		for (int i = 1; i < matchers.length; i++) {
			builder.append(sep).append(matchers[i].toString());
		}
		builder.append(')');

		return builder.toString();
	}

	/**
	 * 
	 * @author pulsarang
	 */
	public enum Type {
		AND, OR;
	}
}
