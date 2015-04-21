package com.naver.fog.content;

import com.naver.fog.web.FogHandledException;

public class ContentNotFoundException extends FogHandledException {
	private static final long serialVersionUID = 8735789530292501613L;

	public ContentNotFoundException(long contentId) {
		this(contentId, HandleType.ALERT_AND_BACK);
	}

	public ContentNotFoundException(long contentId, HandleType handleType) {
		super(contentId, null, handleType, null, "Fail to find content.(" + contentId + ")");
	}
}
