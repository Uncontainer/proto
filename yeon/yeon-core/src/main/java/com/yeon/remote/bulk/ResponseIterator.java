package com.yeon.remote.bulk;

import com.yeon.server.Server;

public interface ResponseIterator<T> {
	void nextSuccess(Server server, T result);

	void nextSkip(Server server, RemoteServiceInvokeSkipReason skipReason);

	void nextFail(Server server, String errorCode, String errorMessage);
}
