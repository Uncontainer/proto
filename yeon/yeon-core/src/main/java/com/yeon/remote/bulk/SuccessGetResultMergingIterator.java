package com.yeon.remote.bulk;

import com.yeon.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SuccessGetResultMergingIterator<T> implements ResponseIterator<T> {
	private final Logger log = LoggerFactory.getLogger(SuccessGetResultMergingIterator.class);

	private final List<T> merged = new ArrayList<T>();

	@Override
	public void nextSuccess(Server server, T result) {
		merged.add(result);
	}

	@Override
	public void nextSkip(Server server, RemoteServiceInvokeSkipReason skipReason) {
		log.debug("[YEON] Skip to call rpc '{}'.({})", server, skipReason.getText());
	}

	@Override
	public void nextFail(Server server, String errorCode, String errorMessage) {
		log.warn("[YEON] Fail to call rpc '{}'. ({})", server, errorMessage);
	}

	public List<T> getMergedResult() {
		return merged;
	}
}
