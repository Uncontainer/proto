package com.pulsarang.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class CollectionUtil {
	public static boolean isEmpty(Collection<?> list) {
		return list == null || list.isEmpty();
	}

	public static boolean isNotEmpty(Collection<?> list) {
		return list != null && !list.isEmpty();
	}

	public static <T> List<T> getCommonList(Collection<T> x, Collection<T> y) {
		if (x == null || x.isEmpty())
			return null;
		if (y == null || y.isEmpty())
			return null;

		SortedSet<T> sorted;
		Collection<T> comparing;
		if (x.size() > y.size()) {
			sorted = new TreeSet<T>(y);
			comparing = x;
		} else {
			sorted = new TreeSet<T>(x);
			comparing = y;
		}

		int size = sorted.size() > 10 ? 10 : sorted.size();
		List<T> result = new ArrayList<T>(size);

		for (T item : comparing) {
			if (sorted.contains(item))
				result.add(item);
		}

		return result;
	}

	public static <T> boolean isDifferent(Collection<T> x, Collection<T> y) {
		ChangeInfo<T> result = getChanged(x, y);
		return result.isChanged();
	}

	public static <T> ChangeInfo<T> getChanged(Collection<T> oldCollection, Collection<T> newCollection) {
		ChangeInfo<T> result = new ChangeInfo<T>();
		if (oldCollection == null) {
			if (newCollection != null) {
				result.added = new ArrayList<T>(newCollection);
			}
			return result;
		} else {
			if (newCollection == null) {
				result.removed = new ArrayList<T>(oldCollection);
				return result;
			}
		}

		if (oldCollection.isEmpty()) {
			if (!newCollection.isEmpty())
				result.added = new ArrayList<T>(newCollection);
			return result;
		}
		if (newCollection.isEmpty()) {
			result.removed = new ArrayList<T>(oldCollection);
			return result;
		}

		Set<T> x = new HashSet<T>(oldCollection);

		for (T item : newCollection) {
			if (x.remove(item)) {
				result.addCommonItem(item);
			} else {
				result.addAddedItem(item);
			}
		}

		if (result.removed == null) {
			result.removed = new ArrayList<T>(x);
		} else {
			result.removed.addAll(x);
		}

		return result;
	}

	public static class ChangeInfo<T> {
		List<T> added = null;
		List<T> removed = null;
		List<T> common = null;

		private void addAddedItem(T item) {
			if (added == null)
				added = new ArrayList<T>();
			added.add(item);
		}

		private void addCommonItem(T item) {
			if (common == null)
				common = new ArrayList<T>();
			common.add(item);
		}

		public boolean isAdded() {
			return isNotEmpty(added);
		}

		public boolean isRemove() {
			return isNotEmpty(removed);
		}

		public boolean isChanged() {
			return isAdded() || isRemove();
		}

		public List<T> getAdded() {
			return added;
		}

		public List<T> getRemoved() {
			return removed;
		}

		public List<T> getCommon() {
			return common;
		}
	}
}
