package com.naver.mage4j.core.util;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class HtmlUtils {
	public static String htmlspecialchars(String html) {
		throw new UnsupportedOperationException();
	}

	public static String htmlspecialchars(String html, int flags, String encoding, boolean double_encode) {
		throw new UnsupportedOperationException();
	}

	public static String strip_tags(String html) {
		return strip_tags(html, null);
	}

	public static String strip_tags(String html, List<String> allowableTags) {
		throw new UnsupportedOperationException();
	}

	public static String htmlspecialchars_decode(String html) {
		return htmlspecialchars_decode(html, 0);
	}

	public static String htmlspecialchars_decode(String html, int flags) {
		throw new UnsupportedOperationException();
	}

	public static String escapeHtml(String data, List<String> allowedTags/* = null */) {
		String result;
		if (data.length() > 0) {
			if (CollectionUtils.isNotEmpty(allowedTags)) {
				String allowed = StringUtils.join(allowedTags, "|");
				result = data.replaceAll("(?si)<([\\/\\s\\r\\n]*)(" + allowed + ")([\\/\\s\\r\\n]*)>", "##$1$2$3##");
				result = HtmlUtils.htmlspecialchars(result, Flags.ENT_COMPAT, "UTF-8", false);
				result = result.replaceAll("(?si)##([\\/\\s\\r\\n]*)(" + allowed + ")([\\/\\s\\r\\n]*)##", "<$1$2$3>");
			} else {
				result = HtmlUtils.htmlspecialchars(data, Flags.ENT_COMPAT, "UTF-8", false);
			}

		} else {
			(result) = (data);
		}

		return result;
	}

	/**
	 * http://www.php.net/manual/en/function.addslashes.php
	 */
	public static String addslashes(String data) {
		throw new UnsupportedOperationException();
	}
}
