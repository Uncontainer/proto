package com.pulsarang.infra.mom.bak;

import java.util.List;

import com.pulsarang.infra.mom.event.EventType;

class MomServerApi {
	private static final MomServerApi INSTANCE = new MomServerApi();

	private MomServerApi() {

	}

	public static final MomServerApi getInstance() {
		return INSTANCE;
	}

	public void subscribe(String canonicalName, List<? extends EventType> subscribeEventTypes) {
		// TODO Auto-generated method stub

	}

}
