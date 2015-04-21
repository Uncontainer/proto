package com.yeon.mom.rabbitmq.producer;

import com.yeon.YeonContext;
import com.yeon.mom.EventProducer;
import com.yeon.mom.event.Event;
import com.yeon.mom.rabbitmq.consumer.LocalEventReceiver;
import com.yeon.monitor.MomScheduledExecutor;
import com.yeon.remote.RemoteParameters;
import com.yeon.remote.RemoteServiceMonitor;
import com.yeon.remote.bulk.RemoteServiceBulkProxy;
import com.yeon.remote.bulk.RemoteServiceResponseListHolder;
import com.yeon.remote.reload.ReloadService;
import com.yeon.server.ProjectId;
import com.yeon.server.Server;
import com.yeon.server.ServerApiService;
import com.yeon.util.LocalAddressHolder;
import com.yeon.util.MapModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author pulsarang
 */
public class LocalEventProducer implements EventProducer, Runnable {
	private final Logger log = LoggerFactory.getLogger(LocalEventProducer.class);

	volatile List<Server> servers = Collections.emptyList();
	volatile List<String> serverNames = Collections.emptyList();

	public LocalEventProducer() {
		MomScheduledExecutor.schedule(this, 30, TimeUnit.SECONDS);
	}

	@Override
	public void publish(Event event) {
		if (servers.isEmpty()) {
			servers = getServers();
		}

		log.debug("[YEON] Publish event to local projects '{}'.({})", serverNames, event);

		ReloadService invoker = RemoteServiceBulkProxy.newServerProxy(ReloadService.class, LocalEventReceiver.NAME, servers, 30000);

		MapModel optionMap = new MapModel();
		optionMap.put("event", event.getValues());

		try {
			invoker.add(optionMap);
		} finally {
			RemoteServiceResponseListHolder.clear();
		}
	}

	@Override
	public void publish(Event event, boolean async) {
		publish(event);
	}

	@Override
	public void retry(Event event) {
		log.warn("[YEON] Unsupported method.");
	}

	@Override
	public void run() {
		try {
			servers = getServers();

			// 순간적인 데이터 불일치가 있을 수 있으나 로컬의 테스트 용도이므로 무시함.
			List<String> newServerNames = new ArrayList<String>(servers.size());
			for (Server server : servers) {
				newServerNames.add(server.getName());
			}
			serverNames = newServerNames;
		} catch (Exception e) {
			log.info("[YEON] Fail to find servers for event local multicasting.", e);
			servers = Collections.emptyList();
		} finally {
			synchronized (this) {
				if (!MomScheduledExecutor.isShutdown()) {
					MomScheduledExecutor.schedule(this, 180, TimeUnit.SECONDS);
				}
			}
		}
	}

	private static List<Server> getServers() {
		ServerApiService api = YeonContext.getRemoteContext().newProjectProxy(ServerApiService.class, ServerApiService.NAME, ProjectId.INFRA_ADMIN, 3000);

		List<Server> candidateServers = api.getServersByIp(LocalAddressHolder.getLocalAddress());
		List<Server> servers = new ArrayList<Server>(candidateServers.size());
		for (Server candidateServer : candidateServers) {
			if (hasLocallyEventReceiver(candidateServer)) {
				servers.add(candidateServer);
			}
		}

		return servers;
	}

	private static boolean hasLocallyEventReceiver(Server server) {
		List<MapModel> remoteServiceInfos;
		try {
			ReloadService invoker = YeonContext.getRemoteContext().newServerProxy(ReloadService.class, RemoteServiceMonitor.NAME, server, 3000);
			remoteServiceInfos = invoker.list(null);
		} catch (Exception e) {
			return false;
		}

		for (MapModel rpcListenerInfo : remoteServiceInfos) {
			if (LocalEventReceiver.NAME.equals(rpcListenerInfo.getString(RemoteParameters.OBJECT_NAME))) {
				return true;
			}
		}

		return false;
	}
}
