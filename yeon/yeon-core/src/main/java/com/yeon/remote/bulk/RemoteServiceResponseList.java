package com.yeon.remote.bulk;

import com.yeon.remote.RemoteServiceResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RemoteServiceResponseList {
	private List<RemoteServiceResponse> responses;

	protected RemoteServiceResponseList(List<RemoteServiceResponse> responses) {
		if (responses == null) {
			this.responses = Collections.emptyList();
		} else {
			this.responses = responses;
		}
	}

	public boolean isSuccess() {
		return !isFail();
	}

	public boolean isFail() {
		for (RemoteServiceResponse response : responses) {
			if (response.isFail()) {
				return true;
			}
		}

		return false;
	}

	public List<RemoteServiceResponse> getEntries() {
		return responses;
	}

	public List<RemoteServiceResponse> getFailedEntries() {
		if (responses.isEmpty()) {
			return responses;
		}

		List<RemoteServiceResponse> entries = new ArrayList<RemoteServiceResponse>();
		for (RemoteServiceResponse response : responses) {
			if (response.isFail()) {
				entries.add(response);
			}
		}

		return entries;
	}

	public List<RemoteServiceResponse> getSuccessEntries() {
		if (responses.isEmpty()) {
			return responses;
		}

		List<RemoteServiceResponse> entries = new ArrayList<RemoteServiceResponse>();
		for (RemoteServiceResponse response : responses) {
			if (response.isSuccess()) {
				entries.add(response);
			}
		}

		return entries;
	}

	public <T> void iterate(ResponseIterator<T> iterator) {
		List<RemoteServiceResponse> entries = new ArrayList<RemoteServiceResponse>();
		for (RemoteServiceResponse response : responses) {
			if (response.isFail()) {
				entries.add(response);
			} else {

			}
		}

	}
}
