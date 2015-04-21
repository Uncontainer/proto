package com.pulsarang.core.util;

import org.apache.commons.lang.StringUtils;

/**
 * 게시물 전반적인 도우미 Class.
 */
public class BoardHelper {
	private static final String TAG_PATTERN = "<[?]?(!--)?\\s*/?[a-zA-Z1-9]+\\b[^>]*>";
	private static final String ANNOTATION_PATTERN = "<!--[^>]+-->";
	private static final String GENERAL_TAG_PATTERN = "<[^>]*>";

	/**
	 * 정규표현식을 사용해 HTML 을 제거한다.
	 * 
	 * @param content
	 *            스트링
	 * @return html을 제거한 스트링
	 */
	public static String removeHTML(String content) {
		if (StringUtils.isEmpty(content)) {
			return content;
		}
		content = content.replaceAll(ANNOTATION_PATTERN, StringUtils.EMPTY);
		content = content.replaceAll(TAG_PATTERN, StringUtils.EMPTY);
		return content;
	}

	/**
	 * 좀 더 광범위하게, 태그 비슷한 것들은 모조리 없애도록 수정
	 * 
	 * @param content
	 * @return
	 */
	public static String removeTagsAndTrim(String content) {
		return content.replaceAll("&nbsp;", " ").trim().replaceAll(GENERAL_TAG_PATTERN, "");
	}

	public static String stripInvalidXMLCharacters(String in) {

		if (StringUtils.isEmpty(in))
			return StringUtils.EMPTY; // vacancy test.

		StringBuilder out = new StringBuilder(in.length()); // Used to hold the
		// output.

		char current; // Used to reference the current character.
		for (int i = 0; i < in.length(); i++) {
			current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught
			// here; it should not happen.
			if (isValidXmlCharacter(current))
				out.append(current);
		}
		return out.toString();
	}

	public static boolean isValidXmlCharacter(char current) {
		return (current == 0x9) || (current == 0xA) || (current == 0xD) || ((current >= 0x20) && (current <= 0xD7FF))
				|| ((current >= 0xE000) && (current <= 0xFFFD)) || ((current >= 0x10000) && (current <= 0x10FFFF));
	}

	public static String getDocumentURL(String documentUrlTemplate, String docId) {
		if (StringUtils.isEmpty(documentUrlTemplate))
			return null;

		return documentUrlTemplate.replaceAll("_docId_", docId);
	}

	/**
	 * ID로 사용하기에 부적합한 문자들을 제거한다.<br>
	 * 허용되는 문자 : 한글,알파벳,숫자,'-','_'
	 * 
	 * @param id
	 * @return
	 */
	public static String stripInvalidCharacterForId(String id) {
		if (StringUtils.isEmpty(id))
			return id;

		StringBuilder builder = new StringBuilder(id.length());
		for (int i = 0, n = id.length(); i < n; i++) {
			char ch = id.charAt(i);
			if (isAcceptableCharacterForId(ch)) {
				builder.append(ch);
			}
		}

		return builder.toString();
	}

	private static boolean isAcceptableCharacterForId(char ch) {
		if (ch == '-' || ch == '_')
			return true;
		if (!isValidXmlCharacter(ch))
			return false;
		return Character.isLetterOrDigit(ch);
	}
}
