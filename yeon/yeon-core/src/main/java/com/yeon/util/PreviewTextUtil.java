package com.yeon.util;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class PreviewTextUtil {
	private static Map<String, Character> escapeChar = new HashMap<String, Character>();

	static {
		escapeChar.put("&lt;", '<');
		escapeChar.put("&gt;", '>');
		escapeChar.put("&amp;", '&');
		escapeChar.put("&quote;", '"');
		escapeChar.put("&nbsp;", ' ');
	}

	public static String getPreviewText(String content, int length, int maxByteLength) {
		if (StringUtils.isEmpty(content)) {
			return "";
		}

		// 개행에 해당하는 태그를 공백으로 바꿈.
		content = content.replaceAll("<[b|B][r|R][ ]*/?>|</[p|P]>", "&nbsp;");

		String previewText = BoardHelper.removeHTML(content);

		StringBuilder sb = new StringBuilder(length);
		int index = 0, count = 0, n = previewText.length();
		while (index < n) {
			char ch = previewText.charAt(index);
			if (!BoardHelper.isValidXmlCharacter(ch)) {
				index++;
				continue;
			}

			if (ch == '<') {
				// 주석 제거.
				if (previewText.startsWith("<!--", index)) {
					index += 4;
					int commentEndIndex = previewText.indexOf("-->", index);
					if (commentEndIndex < 0) {
						break;
					}
					index += 3;
					continue;
				}
			}

			if (ch == '&') {
				String excape = getStartEscape(previewText, index);
				if (excape == null) {
					sb.append(ch);
					index++;
				} else {
					sb.append(excape);
					index += excape.length();
				}
			} else {
				sb.append(ch);
				index++;
			}

			if (++count >= length - 3) {
				if ((n - index) > 3) {
					sb.append("...");
				} else {
					while (index < n) {
						sb.append(previewText.charAt(index++));
					}
				}
				break;
			}
		}

		return truncateLength(sb.toString(), maxByteLength);
	}

	public static String truncateLength(String previewText, int maxByteLength) {
		if (previewText.length() * 3 <= maxByteLength) {
			return previewText;
		}

		int size = 0, index = 0, n = previewText.length();
		for (; index < n; index++) {
			size += charByteSize(previewText.charAt(index));
			if (size > maxByteLength) {
				break;
			}
		}

		if (index >= n) {
			return previewText;
		}

		// 중간에 짤린 escape 문자가 있을 경우 마지막의 불완전한 escape를 제거한다.
		int end = index;
		index -= 7;
		if (index < 0) {
			index = 0;
		}

		while (index < end) {
			char ch = previewText.charAt(index);
			if (ch != '&') {
				index++;
				continue;
			}

			String escape = getStartEscape(previewText, index);
			if (escape != null) {
				if (index + escape.length() < end) {
					index += escape.length();
				} else {
					break;
				}
			} else {
				break;
			}
		}
		return previewText.substring(0, index);
	}

	private static String getStartEscape(String content, int index) {
		for (Map.Entry<String, Character> e : escapeChar.entrySet()) {
			if (content.startsWith(e.getKey(), index)) {
				return e.getKey();
			}
		}
		return null;
	}

	private static int charByteSize(char ch) {
		if (ch <= 0x00007F) {
			return 1;
		} else if (ch <= 0x0007FF) {
			return 2;
		} else if (ch <= 0x00FFFF) {
			return 3;
		} else {
			return 4;
		}
	}
}
