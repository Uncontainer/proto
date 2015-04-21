package com.pulsarang.infra.remote;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.server.Project;
import com.pulsarang.infra.server.Server;
import com.pulsarang.infra.server.ServerContext;
import com.pulsarang.infra.server.Solution;
import com.pulsarang.infra.server.SolutionProjectPair;

public class RemoteServiceProxy implements InvocationHandler {
	public static <T> T newServerProxy(Class<T> clazz, String target, Server server, int socketTimeout) {
		return newProxy(clazz, target, new SingleServerSelector(server), socketTimeout);
	}

	public static <T> T newSolutionProxy(Class<T> clazz, String target, String solutionName, int socketTimeout) {
		return newProxy(clazz, target, new SolutionServerSelector(solutionName), socketTimeout);
	}

	public static <T> T newProjectProxy(Class<T> clazz, String target, SolutionProjectPair pair, int socketTimeout) {
		return newProxy(clazz, target, new ProjectServerSelector(pair.getSolutionName(), pair.getProjectName()), socketTimeout);
	}

	@SuppressWarnings("unchecked")
	private static <T> T newProxy(Class<T> clazz, String target, ServerSelector selector, int socketTimeout) {
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] { clazz }, new RemoteServiceProxy(selector, target, socketTimeout));
	}

	private final ServerSelector selector;
	private String target;
	private int socketTimeout;
	private int connectionTimeout = 2000;

	private RemoteServiceProxy(ServerSelector selector, String target, int socketTimeout) {
		super();
		this.selector = selector;
		this.target = target;
		this.socketTimeout = socketTimeout;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws IOException {

		RemoteServiceRequest request = new RemoteServiceRequest();
		request.setTarget(target);
		request.setMethodName(method.getName());
		request.setParameres(args);

		Server server = selector.getServer();

		RemoteServiceResponse response = RemoteServiceInvokeUtil.invoke(request, server, connectionTimeout, socketTimeout);

		return populate(response.getResult(), method.getReturnType());
	}

	/**
	 * stream 자체에 해당 기능 추가.
	 * 
	 * @param object
	 * @param clazz
	 * @return
	 */
	private Object populate(Object object, Class<?> clazz) {
		if (object == null) {
			return null;
		}

		if (clazz.isAssignableFrom(MapModel.class)) {
			@SuppressWarnings("unchecked")
			MapModel mapModel = MapModel.fromMap((Map<String, Object>) object, (Class<? extends MapModel>) clazz);
			return populate(mapModel);
		} else if (clazz.isAssignableFrom(List.class)) {
			return populate((List<?>) object);
		} else {
			return object;
		}
	}

	private Object populate(List<?> list) {
		List<Object> newList = new ArrayList<Object>(list.size());
		for (Object item : list) {
			if (item instanceof Map) {
				newList.add(populate(new MapModel((Map) item)));
			} else if (item instanceof List) {
				newList.add(populate((List) item));
			} else {
				newList.add(item);
			}
		}
		return list;
	}

	private Object populate(MapModel mapModel) {
		for (Entry<String, Object> entry : mapModel.getProperties().entrySet()) {
			Object value = entry.getValue();
			if (value instanceof Map) {
				entry.setValue(populate(new MapModel((Map) value)));
			} else if (value instanceof List) {
				entry.setValue(populate((List) value));
			}
		}

		return mapModel;
	}

	private static interface ServerSelector {
		Server getServer();
	}

	private static class SolutionServerSelector implements ServerSelector {
		String solutionName;
		Random random;

		public SolutionServerSelector(String solutionName) {
			super();
			this.solutionName = solutionName;
			this.random = new Random();
		}

		@Override
		public Server getServer() {
			ServerContext serverContext = InfraContextFactory.getInfraContext().getServerContext();
			Solution solution = serverContext.getSolution(solutionName);
			List<Server> servers = solution.getServers();
			return servers.get(random.nextInt(servers.size()));
		}
	}

	private static class ProjectServerSelector implements ServerSelector {
		String solutionName;
		String projectName;
		Random random;

		public ProjectServerSelector(String solutionName, String projectName) {
			super();
			this.solutionName = solutionName;
			this.projectName = projectName;
			this.random = new Random();
		}

		@Override
		public Server getServer() {
			ServerContext serverContext = InfraContextFactory.getInfraContext().getServerContext();
			Project project = serverContext.getProject(solutionName, projectName);

			List<Server> servers = project.getServers();
			return servers.get(random.nextInt(servers.size()));
		}
	}

	private static class SingleServerSelector implements ServerSelector {
		Server server;

		public SingleServerSelector(Server server) {
			super();
			this.server = server;
		}

		@Override
		public Server getServer() {
			return server;
		}
	}
}
