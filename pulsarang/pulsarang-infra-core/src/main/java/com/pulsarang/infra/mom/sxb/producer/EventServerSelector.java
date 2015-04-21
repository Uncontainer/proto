package com.pulsarang.infra.mom.sxb.producer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.infra.server.Server;
import com.pulsarang.infra.server.ServerContext;

/**
 * TODO bucket server 변경 event를 받아서 동적으로 업데이트 하는 코드 추가.
 * 
 * @author pulsarang
 * 
 */
public class EventServerSelector {
	private final Logger log = LoggerFactory.getLogger(EventServerSelector.class);

	public static final int BUCKET_COUNT = 1024;
	public static final int BUCKNO_NO_MASK = 0x000003ff;

	public static EventServerSelector INSTANCE = new EventServerSelector();

	public static EventServerSelector getInstance() {
		return INSTANCE;
	}

	private Server[] bucketServers = new Server[BUCKET_COUNT];

	private EventServerSelector() {
	}

	public Server select(Event event, boolean autoSelectOnNull) {
		int bucketNo = getBucketNo(event);

		if (bucketServers[bucketNo] == null) {
			bucketServers[bucketNo] = autoSelectOnNull ? selectRandomServer() : null;
		}

		return bucketServers[bucketNo];
	}

	public void changeServer(int bucketNo, String serverIp) {
		ServerContext serverContext = InfraContextFactory.getInfraContext().getServerContext();
		Server server = serverContext.getProject(com.pulsarang.infra.mom.MomConstants.MOM_SERVER).getServerByIp(serverIp);
		if (server != null) {
			bucketServers[bucketNo] = server;
		} else {
			log.error("Fail to find mom-server.({})", serverIp);
		}
	}

	private Server selectRandomServer() {
		ServerContext serverContext = InfraContextFactory.getInfraContext().getServerContext();
		List<Server> servers = serverContext.getServersOfProject(com.pulsarang.infra.mom.MomConstants.MOM_SERVER);
		if (servers.isEmpty()) {
			throw new IllegalStateException("Fail to find mom server.");
		}

		return servers.get((int) (System.nanoTime() % servers.size()));
	}

	public static final int getBucketNo(Event event) {
		long channelNo = event.getChannelNo();
		return (int) (channelNo & BUCKNO_NO_MASK);
	}
}
