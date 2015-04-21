package com.yeon.util;

import org.junit.Assert;
import org.junit.Test;

public class StringEscapeUtilTest {
	@Test
	public void escapeHtml() {
		String str = "하하하하하 & < > 메롱메롱 '크크크' 히히히 \"\"";
		String actual = StringEscapeUtil.escapeHtml(str);
		String expected = "하하하하하 &amp; &lt; &gt; 메롱메롱 '크크크' 히히히 &quot;&quot;";
		Assert.assertEquals(expected, actual);
	}
}
