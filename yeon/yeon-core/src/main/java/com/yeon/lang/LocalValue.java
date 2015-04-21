package com.yeon.lang;

import java.io.Serializable;

public class LocalValue implements Serializable, Comparable<LocalValue> {
	private static final long serialVersionUID = -5721372647357439304L;

	@SuppressWarnings("serial")
	public static LocalValue NULL = new LocalValue() {
		@Override
		public void setLocale(String locale) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setValue(String value) {
			throw new UnsupportedOperationException();
		}
	};

	protected String locale;
	protected String value;

	public LocalValue() {
		super();
	}

	public LocalValue(String locale, String value) {
		super();
		this.locale = locale;
		this.value = value;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return locale + ':' + value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		LocalValue other = (LocalValue) obj;
		if (locale == null) {
			if (other.locale != null) {
				return false;
			}
		} else if (!locale.equals(other.locale)) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}

		return true;
	}

	@Override
	public int compareTo(LocalValue o) {
		if (o == null) {
			return -1;
		}

		if (locale == null) {
			if (o.locale != null) {
				return 1;
			}
		} else {
			if (o.locale == null) {
				return -1;
			} else {
				int compareResult = locale.compareTo(o.locale);
				if (compareResult != 0) {
					return compareResult;
				}
			}
		}

		if (value == null) {
			return (o.value == null) ? 0 : 1;
		} else {
			if (o.value == null) {
				return -1;
			} else {
				return value.compareTo(o.value);
			}
		}
	}
}
