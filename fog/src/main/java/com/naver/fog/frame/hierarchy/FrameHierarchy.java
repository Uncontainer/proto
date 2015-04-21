package com.naver.fog.frame.hierarchy;

import com.naver.fog.Blamable;

public class FrameHierarchy extends Blamable {
	private long frameId;
	private long parentFrameId;

	public FrameHierarchy() {
	}

	public FrameHierarchy(long frameId, long parentFrameId) {
		super();
		this.frameId = frameId;
		this.parentFrameId = parentFrameId;
	}

	public long getFrameId() {
		return frameId;
	}

	public void setFrameId(long frameId) {
		this.frameId = frameId;
	}

	public long getParentFrameId() {
		return parentFrameId;
	}

	public void setParentFrameId(long parentFrameId) {
		this.parentFrameId = parentFrameId;
	}

	@Override
	public String toString() {
		return frameId + " extends " + parentFrameId;
	}
}
