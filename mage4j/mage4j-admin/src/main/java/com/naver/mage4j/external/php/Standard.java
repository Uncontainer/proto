package com.naver.mage4j.external.php;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.digest.DigestUtils;

public class Standard {
	/**
	 * Exchanges all keys with their associated values in an array
	 * @link http://www.php.net/manual/en/function.array-flip.php
	 * @param array array <p>
	 * An array of key/value pairs to be flipped.
	 * </p>
	 * @return array the flipped array on success and &null; on failure.
	 */
	public static <K, V> Map<V, K> array_flip(Map<K, V> params) {
		Map<V, K> result = new HashMap<V, K>();
		for (Entry<K, V> each : params.entrySet()) {
			result.put(each.getValue(), each.getKey());
		}
		return result;
	}

	/**
	 * Extract a slice of the array
	 * @link http://www.php.net/manual/en/function.array-slice.php
	 * @param array array <p>
	 * The input array.
	 * </p>
	 * @param offset int <p>
	 * If offset is non-negative, the sequence will
	 * start at that offset in the array. If
	 * offset is negative, the sequence will
	 * start that far from the end of the array.
	 * </p>
	 * @param length int[optional] <p>
	 * If length is given and is positive, then
	 * the sequence will have up to that many elements in it. If the array
	 * is shorter than the length, then only the
	 * available array elements will be present. If
	 * length is given and is negative then the
	 * sequence will stop that many elements from the end of the
	 * array. If it is omitted, then the sequence will have everything
	 * from offset up until the end of the
	 * array.
	 * </p>
	 * @param preserve_keys bool[optional] <p>
	 * Note that array_slice will reorder and reset the
	 * numeric array indices by default. You can change this behaviour by setting
	 * preserve_keys to true.
	 * </p>
	 * @return array the slice.
	 */
	public static <T> T[] array_slice(T[] array, int offset, int length) {
		return Arrays.copyOfRange(array, offset, offset + length);
	}

	public static <T> T[] array_slice(T[] array, int offset) {
		return array_slice(array, offset, array.length - offset);
	}

	public static <T> T[] array_splice(T[] input, int offset, int length, T[] replacement) {
		if (input == null) {
			return null;
		}

		T[] result;
		if (offset >= input.length - 1) {
			if (replacement == null) {
				result = Arrays.copyOf(input, input.length);
			} else {
				result = Arrays.copyOf(input, input.length + replacement.length);
				System.arraycopy(replacement, 0, result, input.length, replacement.length);
			}
		} else {
			int end = offset + length;
			if (end >= input.length) {
				end = input.length;
			}
			int newLength = input.length - (end - offset);
			if (replacement != null) {
				newLength += replacement.length;
			}

			result = (T[])Array.newInstance(input.getClass().getComponentType(), newLength);

			System.arraycopy(input, 0, result, 0, offset);

			int index = offset;
			if (replacement != null) {
				System.arraycopy(replacement, 0, result, index, replacement.length);
				index += replacement.length;
			}

			System.arraycopy(input, end, result, index, input.length - end);
		}

		return result;
	}

	public static String md5(String data) {
		return DigestUtils.md5Hex(data);
	}

	public static String sha1(String source) {
		return DigestUtils.sha1Hex(source);
	}

	/**
	 * Merge one or more arrays
	 * @link http://www.php.net/manual/en/function.array-merge.php
	 * @param array1 array <p>
	 * Initial array to merge.
	 * </p>
	 * @param _ array[optional] 
	 * @return array the resulting array.
	 */
	public static <K, V> Map<K, V> array_merge(Map<K, V> array1, Map<K, V> array2) {
		Map<K, V> result = new HashMap<K, V>(array1);
		if (array2 != null) {
			result.putAll(array2);
		}

		return result;
	}

	public static String ltrim(String haystack, String needle) {
		if (needle == null || haystack == null) {
			return haystack;
		}

		for (int i = 0, n = haystack.length(); i < n; i++) {
			if (needle.indexOf(haystack.charAt(i)) < 0) {
				return i != 0 ? haystack.substring(i) : haystack;
			}
		}

		return "";
	}

	public static String rtrim(String haystack, String needle) {
		if (needle == null || haystack == null) {
			return haystack;
		}

		int lastPosition = haystack.length() - 1;
		for (int i = lastPosition; i >= 0; i--) {
			if (needle.indexOf(haystack.charAt(i)) < 0) {
				return i != lastPosition ? haystack.substring(0, i + 1) : haystack;
			}
		}

		return "";
	}

	public static String trim(String haystack, String needle) {
		return Standard.rtrim(ltrim(haystack, needle), needle);
	}

	public static String ucwords(String str) {
		if (str == null) {
			return null;
		}

		StringBuilder result = new StringBuilder(str.length());
		boolean whitespaceBefore = true;
		for (char ch : str.toCharArray()) {
			boolean whitespace = Character.isWhitespace(ch);
			if (!whitespace && whitespaceBefore) {
				result.append(Character.toUpperCase(ch));
			} else {
				result.append(ch);
			}

			whitespaceBefore = whitespace;
		}

		return result.toString();
	}

	public static String str_replace(List<String> search, List<String> replace, String subject) {
		if (search.size() != replace.size()) {
			throw new IllegalArgumentException();
		}

		// TODO 성능 개선 여지 확인
		for (int i = 0; i < search.size(); i++) {
			subject = subject.replace(search.get(i), replace.get(i));
		}

		return subject;
	}
}
