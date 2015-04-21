package com.pulsarang.infra.remote;

public class RemoteServiceResponseListHolder {
	static ThreadLocal<RemoteServiceResponseList> holder;

	public static RemoteServiceResponseList get() {
		return holder.get();
	}

	public static RemoteServiceResponseList getAndClear() {
		try {
			return holder.get();
		} finally {
			holder.remove();
		}
	}

	public static void clear() {
		holder.remove();
	}

	static void set(RemoteServiceResponseList list) {
		holder.set(list);
	}
}
