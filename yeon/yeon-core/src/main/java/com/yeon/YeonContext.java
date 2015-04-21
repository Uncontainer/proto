package com.yeon;

import com.yeon.lang.ResourceContext;
import com.yeon.mom.MomContext;
import com.yeon.mom.rabbitmq.RabbitMqContext;
import com.yeon.monitor.MomScheduledExecutor;
import com.yeon.monitor.collector.LocalMonitorablePropertyCollector;
import com.yeon.monitor.log.ServerLogMonitor;
import com.yeon.monitor.logger.LoggerMonitor;
import com.yeon.monitor.resource.MomMBeanFactory;
import com.yeon.monitor.resource.SystemResourceMonitor;
import com.yeon.monitor.thread.ThreadMonitor;
import com.yeon.remote.RemoteContext;
import com.yeon.remote.reload.AbstractReloadService;
import com.yeon.remote.server.RequestListenerServer;
import com.yeon.server.ProjectId;
import com.yeon.server.ServerContext;
import com.yeon.util.MapModel;
import com.yeon.util.NameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class YeonContext {
	private static final Logger log = LoggerFactory.getLogger(YeonContext.class);

	public static final String TARGET_VERSION = "__version";

	private final YeonConfiguration yeonConfiguration;

	private final ProjectId projectId;
	private final String solutionName;
	private final String projectName;

	private RemoteContext remoteContext;
	private ServerContext serverContext;
	private ResourceContext resourceContext;
	private RabbitMqContext momContext;

	private DisposableManager disposeManager;

	private RequestListenerServer requestListenerServer;

	static YeonContext yeonContext;

	YeonContext(YeonConfiguration yeonConfiguration) {
        yeonContext = this;
		this.yeonConfiguration = yeonConfiguration;
		disposeManager = new DisposableManager();

		solutionName = yeonConfiguration.getSolutionName();
		if (NameUtils.isInvalidYeonIdentifier(solutionName)) {
			throw new IllegalArgumentException("Solution name must be a '-' allowed java idenfier.(" + solutionName + ")");
		}

		projectName = yeonConfiguration.getProjectName();
		if (NameUtils.isInvalidYeonIdentifier(projectName)) {
			throw new IllegalArgumentException("Project name must be a '-' allowed java idenfier.(" + projectName + ")");
		}

		projectId = new ProjectId(solutionName, projectName);

		remoteContext = new RemoteContext(projectId);
		disposeManager.add(remoteContext);

        resourceContext = new ResourceContext(remoteContext);
        serverContext = new ServerContext(remoteContext);

		((BasicYeonConfiguration) yeonConfiguration).initEnvironment();

		remoteContext.setRemoteService(TARGET_VERSION, new VersionInfoListener(yeonConfiguration.getVersion()));

		// Remote 요청 수신 서버 시작
		try {
			int eventServerPort = yeonConfiguration.getRpcListenerPort();
			requestListenerServer = new RequestListenerServer(eventServerPort, remoteContext.getRemoteServiceRegistry());
			requestListenerServer.start();
		} catch (Throwable t) {
			requestListenerServer = null;
			log.warn("[YEON] Yeon RPC request listener server start failed.", t);
		}
		disposeManager.add(requestListenerServer);

		initMonitor();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					return;
				}

				delayedInit();
			}
		}, "yeon-context-delayed-initializer").start();
	}

	private void initMonitor() {
		LocalMonitorablePropertyCollector collector = LocalMonitorablePropertyCollector.getInstance();
		remoteContext.setRemoteService(LocalMonitorablePropertyCollector.NAME, collector);

		LoggerMonitor loggerMonitor = new LoggerMonitor();
		MomMBeanFactory.getInstance().addMBean("logger", loggerMonitor);
		remoteContext.setRemoteService(LoggerMonitor.NAME, loggerMonitor);

		remoteContext.setRemoteService(ThreadMonitor.NAME, new ThreadMonitor());

		new ServerLogMonitor().registAsRemoteService(remoteContext);

		MomScheduledExecutor.registAsRemoteService(remoteContext);
	}

	private void initMomContext() {
		// TODO 옵션으로 momContext 파라미터 처리
		boolean forceProduceEvent = true;
		boolean forceSubscribeEvent = true;
		boolean localPublishing = false;
		momContext = new RabbitMqContext();
		momContext.init(forceProduceEvent, forceSubscribeEvent, localPublishing);

		// TODO 호출 순서를 고려해야 할 수도 있음.
		disposeManager.add((Disposable) momContext);
	}

	// MomConfigContex가 필요한 초기화.
	private void delayedInit() {
		initMomContext();

		try {
			SystemResourceMonitor.getInstance();

			LocalMonitorablePropertyCollector.getInstance().start();
		} catch (Exception e) {
			log.warn("[YEON] Fail to initialize monitor.", e);
		}
	}

	public static YeonContext getYeonContext() {
		return yeonContext;
	}

	public static RemoteContext getRemoteContext() {
		return yeonContext.remoteContext;
	}

	public static ServerContext getServerContext() {
		return yeonContext.serverContext;
	}

	public static ResourceContext getResourceContext() {
		return yeonContext.resourceContext;
	}

	public static MomContext getMomContext() {
		return yeonContext.momContext;
	}

	public static ProjectId getProjectId() {
		return yeonContext.projectId;
	}

	public static String getSolutionName() {
		return yeonContext.solutionName;
	}

	public static String getProjectName() {
		return yeonContext.projectName;
	}

	public static YeonEnvironment getEnvironment() {
		return getConfiguration().getEnvironment();
	}

	public static YeonConfiguration getConfiguration() {
		return yeonContext.yeonConfiguration;
	}

	public void addDisposable(Disposable disposable) {
		disposeManager.add(disposable);
	}

	void close() {
		disposeManager.disposeAll();
	}

	/**
	 *
	 * @author pulsarang
	 */
	private static class VersionInfoListener extends AbstractReloadService {
		private final MapModel versionMap;
		private final List<MapModel> versionMaps;

		VersionInfoListener(String version) {
			versionMap = new MapModel();
			versionMap.put("version", version);

			versionMaps = Arrays.asList(versionMap);
		}

		@Override
		public List<MapModel> list(MapModel optionMap) {
			return versionMaps;
		}

		@Override
		public int listCount(MapModel optionMap) {
			return 1;
		}

		@Override
		public MapModel get(MapModel optionMap) {
			return versionMap;
		}
	}
}
