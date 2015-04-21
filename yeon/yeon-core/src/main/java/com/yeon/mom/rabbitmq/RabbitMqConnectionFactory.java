package com.yeon.mom.rabbitmq;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yeon.YeonPredefinedResourceId;
import com.yeon.lang.ResourceServiceUtils;
import com.yeon.mom.MomInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 
 * @author pulsarang
 */
public class RabbitMqConnectionFactory {
	private final Logger log = LoggerFactory.getLogger(RabbitMqConnectionFactory.class);

	ConnectionFactory factory;
	private volatile String addressString;
	private volatile Address[] addresses;

	public RabbitMqConnectionFactory() {
		this.factory = new ConnectionFactory();
		this.factory.setConnectionTimeout(2000);

		MomInfo momInfo = ResourceServiceUtils.getResourceSafely(YeonPredefinedResourceId.MOM, MomInfo.class);

		initAddress(momInfo.getMqAddress(), true);
	}

	public Connection createConnection() throws IOException {
		return factory.newConnection(addresses);
	}

	public String getAddressString() {
		return addressString;
	}

	public void destory() {
		// TODO [LOW] close 해야할 것은 없는지 확인.
	}

	private Address[] initAddress(String mqAddress, boolean apply) {
		log.info("[YEON] Connect to '{}'.", mqAddress);

		String[] strAddresses = mqAddress.split(";");
		Address[] tempAddresses = new Address[strAddresses.length];

		for (int i = 0; i < strAddresses.length; i++) {
			String strAddress = strAddresses[i];
			URI uri;
			try {
				uri = new URI(strAddress);
			} catch (URISyntaxException e) {
				throw new RuntimeException("Invalid mq address.(" + mqAddress + ")", e);
			}

			tempAddresses[i] = new Address(uri.getHost(), uri.getPort());
		}

		if (apply) {
			this.addressString = mqAddress;
			this.addresses = tempAddresses;
		}

		return tempAddresses;
	}

	public void validate(String mqAddress) {
		Address[] validatedAddresses = initAddress(mqAddress, false);

		Connection connection = null;
		try {
			connection = factory.newConnection(validatedAddresses);
		} catch (IOException e) {
			throw new RuntimeException("Fail to connect rabbitMq.(" + mqAddress + ")", e);
		} finally {
			RabbitMqUtils.closeQuietly(connection);
		}
	}

	public void changeMqAddress(String mqAddress) {
		initAddress(mqAddress, true);
	}
}
