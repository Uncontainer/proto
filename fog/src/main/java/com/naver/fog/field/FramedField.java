package com.naver.fog.field;

import com.naver.fog.Resource;
import com.naver.fog.frame.Frame;
import com.naver.fog.frame.FrameContext;

public class FramedField extends Field {
	public static final String PARAM_FRAME_ID = "frameId";

	public FramedField() {
		setFieldType(FieldType.FRAME);
	}

	@Override
	public void replaceWith(Resource other) {
		if (!(other instanceof FramedField)) {
			throw new IllegalArgumentException();
		}

		FramedField otherFramedField = (FramedField)other;
		if (otherFramedField.getFieldType() != FieldType.FRAME) {
			throw new IllegalArgumentException();
		}

		super.replaceWith(otherFramedField);
	}

	@Override
	public void setFieldType(FieldType type) {
		if (type != FieldType.FRAME) {
			throw new IllegalArgumentException();
		}
	}

	public long getFrameId() {
		Number frameId = getProperty(PARAM_FRAME_ID);
		if (frameId == null) {
			return Resource.NULL_ID;
		} else {
			return frameId.longValue();
		}
	}

	public void setFrameId(long frameId) {
		setProperty(PARAM_FRAME_ID, frameId);
	}

	public Frame getFrame() {
		return FrameContext.getContext().get(getFrameId());
	}
}
