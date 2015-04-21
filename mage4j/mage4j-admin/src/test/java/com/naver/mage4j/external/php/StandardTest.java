package com.naver.mage4j.external.php;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

public class StandardTest {

	@Test
	public void array_spliceTest() {
		String[] input = new String[] {"1", "2", "3"};

		String[] result = Standard.array_splice(input, 0, 2, new String[] {"4", "5"});
		Assert.assertTrue(Arrays.equals(new String[] {"4", "5", "3"}, result));
	}
}
