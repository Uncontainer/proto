package com.pulsarang.core.util;

import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Test;

public class PreivewTextUtilTest {
	
	@Test
	public void previewText() throws UnsupportedEncodingException {
		Assert.assertEquals("test123", getPreviewText("test123", 7, 100));
		Assert.assertEquals("tes...", getPreviewText("test123", 6, 100));

		Assert.assertEquals("&nbsp;abc", getPreviewText("&nbsp;abc", 4, 100));

		Assert.assertEquals("te&amp;st", getPreviewText("<h1>te&amp;st</h1>", 10, 100));

		String preview = getPreviewText("뷁뷁뷁뷁뷁뷁뷁뷁뷁뷁", 10, 10);
		Assert.assertEquals("뷁뷁뷁", preview);
		Assert.assertEquals(9, preview.getBytes("UTF8").length);

		preview = getPreviewText("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", 10, 10);
		Assert.assertEquals("&nbsp;", preview);
		Assert.assertEquals(6, preview.getBytes("UTF8").length);

		preview = getPreviewText("a<Br />b", 10, 10);
		Assert.assertEquals("a&nbsp;b", preview);

		preview = getPreviewText("a</P>b", 10, 10);
		Assert.assertEquals("a&nbsp;b", preview);
		
		preview = getPreviewText("abc<!--sss-->de", 10, 100);
		Assert.assertEquals("abcde", preview);
		
		preview = getPreviewText("abc<!--sss--de>", 10, 100);
		Assert.assertEquals("abc", preview);
	}

	private String getPreviewText(String content, int length, int maxByteLength) {
		return PreviewTextUtil.getPreviewText(content, length, maxByteLength);
	}
}
