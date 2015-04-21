package com.pulsarang.infra;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.config.ConfigContext;
import com.pulsarang.infra.mom.MomContext;
import com.pulsarang.infra.mom.sxb.SxbMomContext;
import com.pulsarang.infra.remote.RemoteContext;
import com.pulsarang.infra.remote.annotation.RemoteService;
import com.pulsarang.infra.remote.reload.AbstractReloadService;
import com.pulsarang.infra.remote.server.RemoteServiceRequestListenServer;
import com.pulsarang.infra.server.ServerContext;
import com.pulsarang.infra.server.ServerInfoNotifier;

/**
 * 
 * @author pulsarang
 */
public class InfraContext {
	private static final Logger log = LoggerFactory.getLogger(InfraContext.class);

	public static final String TARGET_VERSION = "__version";

	private final InfraConfiguration infraConfiguration;

	private RemoteContext remoteContext;

	private ConfigContext configContext;

	private ServerContext serverContext;

	private MomContext momContext;

	private RemoteServiceRequestListenServer remoteServiceListenerServer;

	private DisposableManager disposeManager;

	InfraContext(InfraConfiguration infraConfiguration) {
		this.infraConfiguration = infraConfiguration;

		if (StringUtils.isBlank(infraConfiguration.getSolutionName())) {
			throw new IllegalArgumentException("Solution name is required.");
		}

		if (StringUtils.isBlank(infraConfiguration.getProjectName())) {
			throw new IllegalArgumentException("Project name is required.");
		}
	}

	void init() {
		log.info("[PIC] Initialize InfraContext. ({})", infraConfiguration.getInfraAdminAddress());

		remoteContext = new RemoteContext();
		configContext = new ConfigContext(remoteContext, infraConfiguration);

		remoteContext.setRemoteService(new VersionInfoListener(infraConfiguration.getVersion()));

		((BasicInfraConfiguration) infraConfiguration).initEnvironment(configContext);

		// Mom 초기화
		momContext = new SxbMomContext();
		// TODO 파라미터로 받도록 수정.
		boolean forceProduceEvent = false;
		boolean forceSubscribeEvent = false;
		// TODO this를 넘기는 것은 아닌 것 같음...
		((SxbMomContext) momContext).init(this, forceProduceEvent, forceSubscribeEvent);

		// RPC 요청 수신 서버 시작
		try {
			int eventServerPort = infraConfiguration.getRpcListenerPort();
			remoteServiceListenerServer = new RemoteServiceRequestListenServer(eventServerPort);
			remoteServiceListenerServer.start();
		} catch (Throwable t) {
			remoteServiceListenerServer = null;
			log.warn("[PIC] Infra RPC request listener server start failed.", t);
		}

		int serverPort = remoteServiceListenerServer != null ? remoteServiceListenerServer.getPort() : 0;

		serverContext = new ServerContext(remoteContext, serverPort);

		// 서버 정보 송신
		if (remoteServiceListenerServer == null) {
			new ServerInfoNotifier(infraConfiguration, true).start();
		} else {
			new ServerInfoNotifier(infraConfiguration, remoteServiceListenerServer.getPort(), true).start();
		}

		disposeManager = new DisposableManager();
		disposeManager.add(remoteServiceListenerServer);
		// TODO 호출 순서를 고려해야 할 수도 있음.
		disposeManager.add((Disposable) momContext);
	}

	public void addDisposable(Disposable disposable) {
		disposeManager.add(disposable);
	}

	void close() {
		new ServerInfoNotifier(infraConfiguration, false).start();
		disposeManager.disposeAll();
	}

	public InfraConfiguration getInfraConfiguration() {
		return infraConfiguration;
	}

	public RemoteContext getRemoteContext() {
		return remoteContext;
	}

	public String getSolutionName() {
		return infraConfiguration.getSolutionName();
	}

	public String getProjectName() {
		return infraConfiguration.getProjectName();
	}

	public ServerContext getServerContext() {
		return serverContext;
	}

	public ConfigContext getConfigContext() {
		return configContext;
	}

	public MomContext getMomContext() {
		return momContext;
	}

	public String getVersion() {
		return infraConfiguration.getVersion();
	}

	private static class DisposableManager {
		private final Set<Disposable> disposables = new HashSet<Disposable>();

		public synchronized boolean add(Disposable disposable) {
			if (disposable == null) {
				return false;
			}

			return disposables.add(disposable);
		}

		public void disposeAll() {
			log.info("[PIC] Disposing infra-core...");
			if (disposables.isEmpty()) {
				return;
			}

			final CountDownLatch latch = new CountDownLatch(disposables.size());
			for (final Disposable disposable : disposables) {
				new Thread() {
					@Override
					public void run() {
						try {
							log.info("[PIC] Disposing '{}'...", disposable.getClass().getCanonicalName());
							disposable.dispose();
							log.info("[PIC] Disposed Success '{}'", disposable.getClass().getCanonicalName());
						} catch (Exception e) {
							log.warn("[PIC] Fail to dispose.", e);
						} finally {
							latch.countDown();
						}
					}
				}.start();
			}

			boolean interrupted = Thread.currentThread().isInterrupted();
			if (interrupted) {
				Thread.interrupted();
			}

			try {
				if (latch.await(3, TimeUnit.SECONDS)) {
					log.info("[PIC] Dispose infra-core completed.");
				} else {
					log.warn("[PIC] Dispose infra-core incomplete. Some threads are running.");
				}
			} catch (InterruptedException e) {
				log.info("[PIC] Wait for dispose all fail.", e);
				interrupted = true;
			}

			if (interrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}

	@RemoteService(name = TARGET_VERSION)
	private static class VersionInfoListener extends AbstractReloadService {
		private final MapModel versionMap;
		private final List<MapModel> versionMaps;

		VersionInfoListener(String version) {
			versionMap = new MapModel();
			versionMap.setString("version", version);

			versionMaps = Arrays.asList(versionMap);
		}

		@Override
		public List<MapModel> list(MapModel option) {
			return versionMaps;
		}

		@Override
		public int listCount(MapModel option) {
			return 1;
		}

		@Override
		public MapModel get(MapModel option) {
			return versionMap;
		}
	}
}
