package com.pulsarang.infra.mom.sxb.producer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.core.util.LocalAddressHolder;
import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.Disposable;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.mom.EventProducer;
import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.infra.mom.sxb.EventReceiverService;
import com.pulsarang.infra.remote.RemoteServiceBulkProxy;
import com.pulsarang.infra.remote.RemoteServiceProxy;
import com.pulsarang.infra.remote.RemoteServiceReloadable;
import com.pulsarang.infra.remote.RemoteServiceRequest;
import com.pulsarang.infra.remote.RemoteServiceResponseListHolder;
import com.pulsarang.infra.remote.reload.ReloadService;
import com.pulsarang.infra.server.Server;
import com.pulsarang.infra.server.ServerContext;

public class LocalEventProducer implements EventProducer, Runnable, Disposable {
	private final Logger log = LoggerFactory.getLogger(LocalEventProducer.class);

	volatile List<Server> servers = Collections.emptyList();

	private final ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1);

	public LocalEventProducer() {
		scheduler.schedule(this, 3, TimeUnit.SECONDS);
	}

	@Override
	public void publish(Event event) {
		if (servers.isEmpty()) {
			return;
		}

		ReloadService remoteService = RemoteServiceBulkProxy.newServerProxy(ReloadService.class, EventReceiverService.NAME, servers, 5000);

		MapModel optionMap = new MapModel(2);
		optionMap.setMap("event", event);
		try {
			remoteService.add(optionMap);
		} catch (Exception e) {
			throw new RuntimeException(e);
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
		log.warn("[MOM] Unsupported method.");
	}

	@Override
	public synchronized void dispose() throws Exception {
		scheduler.shutdownNow();
	}

	@Override
	public void run() {
		try {
			servers = getServers();
		} catch (Exception e) {
			log.info("[MOM] Fail to find servers for event local multicasting.", e);
			servers = Collections.emptyList();
		} finally {
			synchronized (this) {
				if (!scheduler.isShutdown()) {
					scheduler.schedule(this, 180, TimeUnit.SECONDS);
				}
			}
		}
	}

	private static List<Server> getServers() {
		ServerContext serverContext = InfraContextFactory.getInfraContext().getServerContext();

		List<Server> candidateServers = serverContext.getServersByIp(LocalAddressHolder.getLocalAddress());
		List<Server> servers = new ArrayList<Server>(candidateServers.size());
		for (Server candidateServer : candidateServers) {
			if (hasLocallyEventReceiver(candidateServer)) {
				servers.add(candidateServer);
			}
		}

		return servers;
	}

	private static boolean hasLocallyEventReceiver(Server server) {
		List<MapModel> rpcListenerInfos;
		try {
			ReloadService reloadService = RemoteServiceProxy.newServerProxy(ReloadService.class, RemoteServiceReloadable.NAME, server, 3000);
			rpcListenerInfos = reloadService.list(null);
		} catch (Exception e) {
			return false;
		}

		for (MapModel rpcListenerInfo : rpcListenerInfos) {
			if (EventReceiverService.NAME.equals(rpcListenerInfo.getString(RemoteServiceRequest.TARGET_NAME))) {
				return true;
			}
		}

		return false;
	}
}
