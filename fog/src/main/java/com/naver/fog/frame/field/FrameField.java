package com.naver.fog.frame.field;

import com.naver.fog.Blamable;
import com.naver.fog.field.Field;
import com.naver.fog.field.FieldContext;
import com.naver.fog.frame.Frame;
import com.naver.fog.frame.FrameContext;

public class FrameField extends Blamable {
	private long frameId;
	private long fieldId;

	public FrameField() {
	}

	public FrameField(long frameId, long fieldId) {
		this.frameId = frameId;
		this.fieldId = fieldId;
	}

	public long getFrameId() {
		return frameId;
	}

	public void setFrameId(long frameId) {
		this.frameId = frameId;
	}

	public long getFieldId() {
		return fieldId;
	}

	public void setFieldId(long fieldId) {
		this.fieldId = fieldId;
	}

	public Frame getFrame() {
		return FrameContext.getContext().get(frameId);
	}

	public Field getField() {
		return FieldContext.getContext().get(fieldId);
	}

	@Override
	public String toString() {
		return frameId + " has " + fieldId;
	}
}
