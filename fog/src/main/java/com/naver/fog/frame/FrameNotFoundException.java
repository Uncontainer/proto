package com.naver.fog.frame;

import com.naver.fog.web.FogHandledException;

public class FrameNotFoundException extends FogHandledException {
	private static final long serialVersionUID = -8184015281278707398L;

	public FrameNotFoundException(long frameId) {
		this(frameId, HandleType.ALERT_AND_BACK);
	}

	public FrameNotFoundException(long frameId, HandleType handleType) {
		super(frameId, null, handleType, null, "Fail to find frame.(" + frameId + ")");
	}

}
