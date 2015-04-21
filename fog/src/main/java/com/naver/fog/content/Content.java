package com.naver.fog.content;

import java.util.Map;

import com.naver.fog.Resource;
import com.naver.fog.ResourceType;
import com.naver.fog.frame.Frame;
import com.naver.fog.frame.FrameContext;

public class Content extends Resource {
	private long frameId;
	private String value;
	private transient Map<Long, Object> fieldValueMap;

	public Content() {
	}

	@Override
	public ResourceType getType() {
		return ResourceType.CONTENT;
	}

	public Content(long frameId) {
		this.frameId = frameId;
	}

	public Content(long frameId, String value) {
		this.frameId = frameId;
		this.value = value;
	}

	public long getFrameId() {
		return frameId;
	}

	public void setFrameId(long frameId) {
		this.frameId = frameId;
	}

	public Frame getFrame() {
		return FrameContext.getContext().get(frameId);
	}

	public String getValue() {
		return value;
	}

	public synchronized void setValue(String value) {
		this.value = value;
		this.fieldValueMap = null;
	}

	public synchronized Map<Long, Object> getFieldValueMap() {
		if (fieldValueMap == null) {
			fieldValueMap = ContentValueEncoder.decode(getFrame(), value);
		}

		return fieldValueMap;
	}

	public Object getFieldValue(long fieldId) {
		return getFieldValueMap().get(fieldId);
	}

	@Override
	public void replaceWith(Resource other) {
		throw new UnsupportedOperationException();
	}
}
