package com.naver.fog.content;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.naver.fog.field.Field;
import com.naver.fog.field.FieldContext;
import com.naver.fog.field.FieldType;
import com.naver.fog.frame.Frame;

public class ContentValueEncoder {
	public static final char VERSION = 'a';

	public static String encode(Frame frame, Map<Long, Object> fieldValueMap) {
		StringBuilder builder = new StringBuilder();
		builder.append(VERSION);
		List<Field> fields = frame.getFieldsAll();
		for (Field field : fields) {
			Object value = fieldValueMap.get(field.getId());
			if (value == null) {
				continue;
			}

			builder.append('|');
			builder.append(field.getId()).append(':');

			String encodedValue;
			if (field.getFieldType() == FieldType.FRAME) {
				encodedValue = ContentEncoder.encode((Content)value);
			} else {
				encodedValue = field.getFieldType().encode(value);
			}

			builder.append(encodedValue.length()).append('.').append(encodedValue);
		}

		return builder.toString();
	}

	public static Map<Long, Object> decode(Frame frame, String encodedValue) {
		if (StringUtils.isEmpty(encodedValue)) {
			return Collections.emptyMap();
		}

		FieldContext fieldContent = FieldContext.getContext();
		DecodeContext decodeContext = new DecodeContext(encodedValue);
		Map<Long, Object> result = new HashMap<Long, Object>();

		while (!decodeContext.isEof()) {
			long fieldId = decodeContext.nextLong();
			String encodedFieldValue = decodeContext.nextString();
			Field field = fieldContent.get(fieldId);
			if (field == null) {
				continue;
			}

			Object decodedValue;
			if (field.getFieldType() == FieldType.FRAME) {
				decodedValue = ContentEncoder.decode(encodedFieldValue);
			} else {
				decodedValue = field.getFieldType().decode(encodedFieldValue);
			}

			result.put(field.getId(), decodedValue);
		}

		return result;
	}

	static class DecodeContext {
		final String encodedValue;
		int index;

		public DecodeContext(String encodedValue) {
			super();
			this.encodedValue = encodedValue;
			char version = encodedValue.charAt(0);
			if (version != VERSION) {
				throw new IllegalArgumentException("Unsupported content encoding version.(" + version + ")");
			}
			this.index = 1;
		}

		long nextLong() {
			index++; // skip field seperator '|'
			int colonIndex = encodedValue.indexOf(':', index);
			long result = Long.valueOf(encodedValue.substring(index, colonIndex));
			index = colonIndex + 1;

			return result;
		}

		String nextString() {
			int dotIndex = encodedValue.indexOf('.', index);
			int length = Integer.valueOf(encodedValue.substring(index, dotIndex));
			index = dotIndex + 1;

			String value = encodedValue.substring(index, index + length);
			index += length;

			return value;
		}

		boolean isEof() {
			return index >= encodedValue.length();
		}
	}

}
