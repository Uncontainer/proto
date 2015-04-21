package com.yeon.util;

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ListUtilTest {

	@Test
	public void getChangedTest() {
		List<String> oldList = new ArrayList<String>();
		List<String> newList = new ArrayList<String>();

		oldList.add("5");
		oldList.add("3");
		oldList.add("1");
		oldList.add("4");
		oldList.add("2");
		oldList.add("9");

		newList.add("5");
		newList.add("3");
		newList.add("4");
		newList.add("8");
		newList.add("7");

		CollectionUtil.ChangeInfo<String> info = CollectionUtil.getChanged(oldList, newList);
		Assert.assertTrue(info.isRemove());
		Assert.assertTrue(info.hasAdded());

		Assert.assertTrue(info.removed.contains("1"));
		Assert.assertTrue(info.removed.contains("2"));
		Assert.assertTrue(info.removed.contains("9"));

		Assert.assertTrue(info.added.contains("7"));
		Assert.assertTrue(info.added.contains("8"));
	}

	@Test
	public void getChangedTestNotAdded() {
		List<String> oldList = new ArrayList<String>();
		List<String> newList = new ArrayList<String>();

		oldList.add("5");
		oldList.add("3");
		oldList.add("1");
		oldList.add("4");
		oldList.add("2");
		oldList.add("9");

		newList.add("5");
		newList.add("3");
		newList.add("4");

		CollectionUtil.ChangeInfo<String> info = CollectionUtil.getChanged(oldList, newList);
		Assert.assertFalse(info.hasAdded());
		Assert.assertTrue(info.isRemove());

		Assert.assertTrue(info.removed.contains("1"));
		Assert.assertTrue(info.removed.contains("2"));
		Assert.assertTrue(info.removed.contains("9"));
	}

	@Test
	public void getChangedTestNotRemoved() {
		List<String> oldList = new ArrayList<String>();
		List<String> newList = new ArrayList<String>();

		oldList.add("5");
		oldList.add("3");
		oldList.add("4");

		newList.add("7");
		newList.add("5");
		newList.add("4");
		newList.add("8");
		newList.add("3");

		CollectionUtil.ChangeInfo<String> info = CollectionUtil.getChanged(oldList, newList);
		Assert.assertTrue(info.hasAdded());
		Assert.assertFalse(info.isRemove());

		Assert.assertTrue(info.added.contains("7"));
		Assert.assertTrue(info.added.contains("8"));
	}

	@Test
	public void getChangedTest_Duplicate1() {
		List<String> oldList = new ArrayList<String>();
		List<String> newList = new ArrayList<String>();

		oldList.add("5");
		oldList.add("3");
		oldList.add("1");
		oldList.add("4");
		oldList.add("2");
		oldList.add("9");

		newList.add("5");
		newList.add("3");
		newList.add("3");
		newList.add("4");
		newList.add("8");
		newList.add("8");
		newList.add("8");
		newList.add("7");

		CollectionUtil.ChangeInfo<String> info = CollectionUtil.getChanged(oldList, newList);
		Assert.assertTrue(info.isRemove());
		Assert.assertTrue(info.hasAdded());

		Assert.assertTrue(info.removed.contains("1"));
		Assert.assertTrue(info.removed.contains("2"));
		Assert.assertTrue(info.removed.contains("9"));

		Assert.assertTrue(info.added.contains("7"));
		Assert.assertTrue(info.added.contains("8"));
	}

	@Test
	public void getChangedTest_Duplicate2() {
		List<String> oldList = new ArrayList<String>();
		List<String> newList = new ArrayList<String>();

		oldList.add("5");
		oldList.add("3");
		oldList.add("1");
		oldList.add("1");
		oldList.add("4");
		oldList.add("4");
		oldList.add("2");
		oldList.add("9");

		newList.add("5");
		newList.add("3");
		newList.add("4");
		newList.add("8");
		newList.add("7");
		newList.add("11");
		newList.add("12");

		CollectionUtil.ChangeInfo<String> info = CollectionUtil.getChanged(oldList, newList);
		Assert.assertTrue(info.isRemove());
		Assert.assertTrue(info.hasAdded());

		Assert.assertTrue(info.removed.contains("1"));
		Assert.assertTrue(info.removed.contains("2"));
		Assert.assertTrue(info.removed.contains("9"));

		Assert.assertTrue(info.added.contains("7"));
		Assert.assertTrue(info.added.contains("8"));
		Assert.assertTrue(info.added.contains("11"));
		Assert.assertTrue(info.added.contains("12"));
	}
}
