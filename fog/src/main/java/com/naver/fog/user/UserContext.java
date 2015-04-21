package com.naver.fog.user;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.naver.fog.Resource;

@Component
public class UserContext implements InitializingBean {

	private static UserContext CONTEXT;

	public void afterPropertiesSet() throws Exception {
		CONTEXT = this;
	}

	public static UserContext getContext() {
		return CONTEXT;
	}

	public User getCurrentUser() {
		// TODO 설정 코드 추가.
		return null;
	}

	public User getUser(long id) {
		if (id == Resource.NULL_ID) {
			return null;
		}

		throw new UnsupportedOperationException();
	}
}
