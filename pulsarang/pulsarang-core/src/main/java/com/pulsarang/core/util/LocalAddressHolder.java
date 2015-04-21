package com.pulsarang.core.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalAddressHolder {
	private static volatile String localAddress;
	private static volatile String hostName;

	public static String getLocalAddress() {
		if (localAddress == null) {
			try {
				localAddress = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				throw new RuntimeException(e);
			}
		}

		return localAddress;
	}

	public static String getLocalHostName() {
		if (hostName == null) {
			try {
				hostName = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				throw new RuntimeException(e);
			}
		}

		return hostName;
	}
}
