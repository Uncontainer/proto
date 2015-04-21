package com.pulsarang.infra.config.propinfo.type;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.pulsarang.core.util.CollectionJsonMapper;

public enum PropertyValueType {
	BYTE {
		public Class<?> getJavaType() {
			return Byte.class;
		}

		public Byte parse(String param) {
			return Byte.parseByte(param);
		}
	},
	SHORT {
		public Class<?> getJavaType() {
			return Short.class;
		}

		public Short parse(String param) {
			return Short.parseShort(param);
		}
	},
	INT {
		public Class<?> getJavaType() {
			return Integer.class;
		}

		public Integer parse(String param) {
			return Integer.parseInt(param);
		}
	},
	INTEGER {
		public Class<?> getJavaType() {
			return Integer.class;
		}

		public Integer parse(String param) {
			return Integer.parseInt(param);
		}
	},
	FLOAT {
		public Class<?> getJavaType() {
			return Float.class;
		}

		public Float parse(String param) {
			return Float.parseFloat(param);
		}
	},
	LONG {
		public Class<?> getJavaType() {
			return Long.class;
		}

		public Long parse(String param) {
			return Long.parseLong(param);
		}
	},
	DOUBLE {
		public Class<?> getJavaType() {
			return Double.class;
		}

		public Double parse(String param) {
			return Double.parseDouble(param);
		}
	},
	BIGDECIMAL {
		public Class<?> getJavaType() {
			return BigDecimal.class;
		}

		public BigDecimal parse(String param) {
			return new BigDecimal(param);
		}
	},
	BIGINTEGER {
		public Class<?> getJavaType() {
			return BigInteger.class;
		}

		public BigInteger parse(String param) {
			return new BigInteger(param);
		}
	},
	BOOLEAN {
		public Class<?> getJavaType() {
			return Boolean.class;
		}

		public Boolean parse(String param) {
			return Boolean.valueOf(param);
		}
	},
	DATE {
		public Class<?> getJavaType() {
			return Date.class;
		}

		public Date parse(String param) {
			return DatePropertyValueParser.parseDate(param);
		}
	},
	MAP {
		public Class<?> getJavaType() {
			return Map.class;
		}

		public Map<String, Object> parse(String param) {
			return CollectionJsonMapper.getInstance().toMap(param);
		}
	},
	CHARACTER {
		public Class<Character> getJavaType() {
			return Character.class;
		}

		public Character parse(String params) {
			if (params.length() != 1) {
				throw new IllegalArgumentException("Not a character.");
			}

			return params.charAt(0);
		}
	},
	STRING {
		public Class<?> getJavaType() {
			return String.class;
		}

		public String parse(String params) {
			return params;
		}
	},
	LIST {
		public Class<?> getJavaType() {
			return List.class;
		}

		public List<String> parse(String params) {
			return CollectionJsonMapper.getInstance().toList(params);
		}
	},
	SET {
		public Class<?> getJavaType() {
			return Set.class;
		}

		public Set<String> parse(String params) {
			StringTokenizer st = new StringTokenizer(params, ",");
			Set<String> set = new HashSet<String>();
			while (st.hasMoreTokens()) {
				set.add(st.nextToken().trim());
			}

			return set;
		}
	},
	TIME {
		public Class<?> getJavaType() {
			return Long.class;
		}

		public Long parse(String params) {
			return TimePropertyValueParser.parseTime(params);
		}
	};

	public Class<?> getJavaType() {
		return Object.class;
	}

	public Object parse(String param) {
		throw new IllegalArgumentException("String(" + param + ") for " + name() + " type");
	}
}
