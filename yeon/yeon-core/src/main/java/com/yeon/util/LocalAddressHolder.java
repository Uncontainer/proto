package com.yeon.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class LocalAddressHolder {
	private static volatile String localAddress;
	private static volatile String hostName;

	public static String getLocalAddress() {
		String la = localAddress;
		if (la == null) {
			try {
				la = InetAddress.getLocalHost().getHostAddress();
				if ("127.0.0.1".equals(la) || la.startsWith("169.")) {
					la = getSiteLocalInetAddress().getHostAddress();
				}
			} catch (UnknownHostException e) {
				throw new RuntimeException(e);
			} catch (SocketException e) {
				throw new RuntimeException(e);
			}

			localAddress = la;
		}

		return la;
	}

	public static String getLocalHostName() {
		String hn = hostName;
		if (hn == null) {
			try {
				hn = hostName = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				throw new RuntimeException(e);
			}
		}

		return hn;
	}

	static InetAddress getSiteLocalInetAddress() throws SocketException {
		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
		while (networkInterfaces.hasMoreElements()) {
			NetworkInterface ni = networkInterfaces.nextElement();
			Enumeration<InetAddress> addresses = ni.getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress addr = addresses.nextElement();
				if (addr.isSiteLocalAddress()) {
					return addr;
				}
			}
		}

		throw new RuntimeException("Fail to find local address.");
	}

	public static boolean isOfficePrivateAddrassBand(String address) {
		if (address == null) {
			return false;
		}

		// TODO [LOW] local의 대역폭을 좀 더 자세히 알아봐야 함.
		if (address.startsWith("10.64.") // 그린팩토리
				|| address.startsWith("10.66.") // 퍼서트타워
				|| address.startsWith("10.67.") // 미래에셋
				|| address.startsWith("10.70.")) { // AK
			return true;
		}

		return false;
	}

	public static boolean isOfficePrivateAddrassBand() {
		return isOfficePrivateAddrassBand(getLocalAddress());
	}
}
