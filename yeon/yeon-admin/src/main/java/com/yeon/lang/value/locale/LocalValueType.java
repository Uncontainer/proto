package com.yeon.lang.value.locale;

import com.yeon.lang.value.ValueLengthType;

public enum LocalValueType {
	NAME(ValueLengthType.SMALL),
	DESCRIPTION(ValueLengthType.LARGE);

	final ValueLengthType lengthType;

	LocalValueType(ValueLengthType lengthType) {
		this.lengthType = lengthType;
	}

	public ValueLengthType getValueLengthType() {
		return lengthType;
	}

	public String getTableName() {
		return "local_value_" + lengthType.name().toLowerCase();
	}
}
