package com.naver.fog.field;

import java.util.Date;

public enum FieldType {
	INTEGER(1, "정수") {
		@Override
		protected String encodeNullSafely(Object value) {
			return ((Number)value).toString();
		}

		@Override
		protected Object decodeNullSafely(String value) {
			return Long.valueOf(value);
		}
	},
	STRING(2, "문자열") {
		@Override
		protected String encodeNullSafely(Object value) {
			return (String)value;
		}

		@Override
		protected Object decodeNullSafely(String value) {
			return value;
		}
	},
	DATETIME(3, "날짜") {
		@Override
		protected String encodeNullSafely(Object value) {
			return Long.toString(((Date)value).getTime());
		}

		@Override
		protected Object decodeNullSafely(String value) {
			return new Date(Long.parseLong(value));
		}
	},
	BOOLEAN(4, "참거짓") {
		@Override
		protected String encodeNullSafely(Object value) {
			return Boolean.toString((Boolean)value);
		}

		@Override
		protected Object decodeNullSafely(String value) {
			return Boolean.valueOf(value);
		}
	},
	FLOAT(5, "소수") {
		@Override
		protected String encodeNullSafely(Object value) {
			return ((Number)value).toString();
		}

		@Override
		protected Object decodeNullSafely(String value) {
			return Double.valueOf(value);
		}
	},
	FRAME(6, "프레임") {
	// encode/decode는 ContentValueEncoder에서 수행
	};

	final int id;
	final String text;

	FieldType(int id, String text) {
		this.id = id;
		this.text = text;
	}

	public int getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	final public String encode(Object value) {
		if (value == null) {
			return null;
		}

		return encodeNullSafely(value);
	}

	final public Object decode(String value) {
		if (value == null) {
			return null;
		}

		return decodeNullSafely(value);
	}

	protected String encodeNullSafely(Object value) {
		throw new UnsupportedOperationException();
	}

	protected Object decodeNullSafely(String value) {
		throw new UnsupportedOperationException();
	}

	public static FieldType fromId(int id) {
		try {
			return FieldType.values()[id - 1];
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Unsupported field-type identifier.(" + id + ")");
		}
	}
}
