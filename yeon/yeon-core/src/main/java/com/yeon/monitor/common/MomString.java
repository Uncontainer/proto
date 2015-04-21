package com.yeon.monitor.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 
 * @author pulsarang
 */
public class MomString implements CharSequence {
	private final static char[] EMPTY = new char[0];

	private char[] buffer;
	private int offset;
	private int count;

	public MomString() {
	}

	public MomString(String string) {
		char[] charArray = string.toCharArray();
		init(charArray, 0, charArray.length);
	}

	public MomString(char[] value) {
		this(value, 0, 0);
	}

	public MomString(char[] value, int offset, int count) {
		init(value, offset, count);
	}

	public void init(char[] value, int offset, int count) {
		setBuffer(value);
		this.offset = offset;
		this.count = count;
	}

	public void init(String string) {
		if (string == null) {
			reset();
		} else {
			init(string.toCharArray(), 0, string.length());
		}
	}

	public void clear() {
		this.offset = 0;
		this.count = 0;
	}

	public void reset() {
		this.buffer = EMPTY;
		this.offset = 0;
		this.count = 0;
	}

	public char[] getBuffer() {
		return buffer;
	}

	public void setBuffer(char[] buffer) {
		this.buffer = buffer != null ? buffer : EMPTY;
	}

	public boolean isNull() {
		return buffer == EMPTY;
	}

	public boolean isEmpty() {
		return buffer == EMPTY || count == 0;
	}

	public boolean isFull() {
		return buffer != EMPTY && offset == buffer.length;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int toOffset(int index) {
		return offset + index;
	}

	public int getLength() {
		return count;
	}

	public void setLength(int length) {
		this.count = length;
	}

	public int increaseLength(int amount) {
		return (count += amount);
	}

	public int decreaseLength(int amount) {
		return (count -= amount);
	}

	@Override
	public int length() {
		return count;
	}

	@Override
	public char charAt(int index) {
		if ((index < 0) || (index >= count)) {
			throw new StringIndexOutOfBoundsException(index);
		}

		return buffer[offset + index];
	}

	public char charAtOffset(int offset) {
		return buffer[offset];
	}

	public void append(char ch) {
		buffer[offset + count++] = ch;
	}

	public void append(char[] value, int start, int length) {
		System.arraycopy(value, start, buffer, offset + count, length);
		count += length;
	}

	public void append(String str) {
		if (str == null) {
			return;
		}

		append(str.toCharArray(), 0, str.length());
	}

	public void insert(int index, String str) {
		if (str == null) {
			return;
		}

		// shift right
		shiftByOffset(toOffset(index), toOffset(index) + str.length(), str.length());
		count += str.length();

		System.arraycopy(str.toCharArray(), 0, buffer, offset + index, str.length());
	}

	public void shiftByOffset(int from, int to, int amount) {
		System.arraycopy(buffer, from, buffer, to, amount);
	}

	public void trimRightNewLine(boolean trimAll) {
		while (count > 0 && buffer[offset + count - 1] == '\n') {
			count--;

			if (!trimAll) {
				break;
			}
		}
	}

	public void trimRight() {
		for (int i = offset + count - 1; i >= offset; i--) {
			if (Character.isWhitespace(buffer[i])) {
				count--;
			}
		}
	}

	public boolean contains(MomString str) {
		return indexOf(buffer, offset, count, str.buffer, str.offset, str.count, 0) != -1;
	}

	public boolean endsWith(MomString suffix) {
		return startsWith(suffix, count - suffix.count);
	}

	public boolean startsWith(MomString prefix) {
		return startsWith(prefix, 0);
	}

	public boolean startsWith(MomString prefix, int toffset) {
		int to = offset + toffset;
		int po = prefix.offset;
		int pc = prefix.count;

		if ((toffset < 0) || (toffset > count - pc)) {
			return false;
		}

		while (--pc >= 0) {
			if (buffer[to++] != prefix.buffer[po++]) {
				return false;
			}
		}

		return true;
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		if (start < 0) {
			throw new StringIndexOutOfBoundsException(start);
		}
		if (end > count) {
			throw new StringIndexOutOfBoundsException(end);
		}
		if (start > end) {
			throw new StringIndexOutOfBoundsException(end - start);
		}

		return new String(buffer, offset + start, end - start);
	}

	@Override
	public String toString() {
		if (isNull()) {
			return null;
		}

		return new String(buffer, offset, count);
	}

	public MomString substring(int index, int count) {
		if (isNull()) {
			return null;
		}

		return new MomString(buffer, this.offset + index, count);
	}

	public String toMutableString() {
		return createMutableString(buffer, offset, count);
	}

	@Override
	public int hashCode() {
		if (isNull()) {
			return 0;
		}

		return toMutableString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (obj instanceof MomString) {
			MomString other = (MomString) obj;
			if (count != other.count) {
				return false;
			}

			int to = offset;
			int tc = count;
			int oo = other.offset;
			while (--tc >= 0) {
				if (buffer[to++] != other.buffer[oo++]) {
					return false;
				}
			}

			return true;
		} else if (obj instanceof String) {
			return toMutableString().equals(obj);
		} else {
			return false;
		}
	}

	static int indexOf(char[] source, int sourceOffset, int sourceCount,
			char[] target, int targetOffset, int targetCount,
			int fromIndex) {
		if (fromIndex >= sourceCount) {
			return (targetCount == 0 ? sourceCount : -1);
		}
		if (fromIndex < 0) {
			fromIndex = 0;
		}
		if (targetCount == 0) {
			return fromIndex;
		}

		char first = target[targetOffset];
		int max = sourceOffset + (sourceCount - targetCount);

		for (int i = sourceOffset + fromIndex; i <= max; i++) {
			/* Look for first character. */
			if (source[i] != first) {
				while (++i <= max && source[i] != first) {
				}
			}

			/* Found first character, now look at the rest of v2 */
			if (i <= max) {
				int j = i + 1;
				int end = j + targetCount - 1;
				for (int k = targetOffset + 1; j < end && source[j] == target[k]; j++, k++) {
				}

				if (j == end) {
					/* Found whole string. */
					return i - sourceOffset;
				}
			}
		}
		return -1;
	}

	static Constructor<String> constructor;

	static {
		try {
			constructor = String.class.getDeclaredConstructor(int.class, int.class, char[].class);
			constructor.setAccessible(true);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	public static String createMutableString(char[] value, int offset, int count) {
		try {
			return constructor.newInstance(offset, count, value);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
