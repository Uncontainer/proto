package com.naver.fog.field;

import org.junit.Test;

public class FieldTypeTest {
	@Test
	public void fromIdTest() {
		for (FieldType fieldType : FieldType.values()) {
			FieldType.fromId(fieldType.getId());
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void fromIdTest_예외() {
		FieldType.fromId(0);
	}
}
