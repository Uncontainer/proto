package com.naver.fog.field;

import com.naver.fog.web.FogHandledException;

public class FieldNotFoundException extends FogHandledException {
	private static final long serialVersionUID = 8735789530292501613L;

	public FieldNotFoundException(long fieldId) {
		this(fieldId, HandleType.ALERT_AND_BACK);
	}

	public FieldNotFoundException(long fieldId, HandleType handleType) {
		super(fieldId, null, handleType, null, "Fail to find field.(" + fieldId + ")");
	}
}
