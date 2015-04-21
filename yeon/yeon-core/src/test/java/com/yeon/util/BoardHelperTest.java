package com.yeon.util;

import org.junit.Assert;
import org.junit.Test;

public class BoardHelperTest {
	@Test
	public void removeHTML_주석제거() {
		String content = "<!--quote_txt-->test <!--/quote_txt-->";

		String actual = BoardHelper.removeHTML(content);
		String expected = "test ";

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void removeHTML_xml제거() {
		String content = "<?xml namespace:ns>test </xml>";

		String actual = BoardHelper.removeHTML(content);
		String expected = "test ";

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void removeUnaccepableCharForIdTest() {
		Assert.assertEquals("", BoardHelper.stripInvalidCharacterForId("\"?>"));
		Assert.assertEquals("가1_-s", BoardHelper.stripInvalidCharacterForId("가, 1._-s\t\n"));
	}
}
