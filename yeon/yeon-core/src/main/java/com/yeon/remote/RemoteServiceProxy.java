package com.yeon.remote;

import com.yeon.YeonContext;
import com.yeon.remote.annotation.RemoteMethod;
import com.yeon.remote.server.RemoteServiceInvoker;
import com.yeon.server.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Random;

public class RemoteServiceProxy implements InvocationHandler {
	public static <T> T newServerProxy(Class<T> clazz, String objectName, Server server, int socketTimeout) {
		return newProxy(clazz, objectName, new SingleServerSelector(server), socketTimeout);
	}

	public static <T> T newSolutionProxy(Class<T> clazz, String objectName, String solutionName, int socketTimeout) {
		return newProxy(clazz, objectName, new SolutionServerSelector(solutionName), socketTimeout);
	}

	public static <T> T newProjectProxy(Class<T> clazz, String objectName, ProjectId pair, int socketTimeout) {
		return newProxy(clazz, objectName, new ProjectServerSelector(pair.getSolutionName(), pair.getProjectName()), socketTimeout);
	}

	@SuppressWarnings("unchecked")
	public static <T> T newProxy(Class<T> clazz, String objectName, ServerSelector selector, int socketTimeout) {
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] { clazz }, new RemoteServiceProxy(selector, objectName, socketTimeout));
	}

	private final ServerSelector selector;
	private String objectName;
	private int socketTimeout;
	private int connectionTimeout = 2000;

	private RemoteServiceProxy(ServerSelector selector, String objectName, int socketTimeout) {
		super();
		this.selector = selector;
		this.objectName = objectName;
		this.socketTimeout = socketTimeout;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		RemoteServiceRequest request = new RemoteServiceRequest();
		request.setObjectName(objectName);
		RemoteMethod annotation = method.getAnnotation(RemoteMethod.class);
		if (annotation != null && !annotation.name().isEmpty()) {
			request.setMethodName(annotation.name());
		} else {
			request.setMethodName(method.getName());
		}
		request.setParameres(args);

		Server server = selector.getServer();

		RemoteServiceInvoker remoteServiceInvoker = YeonContext.getRemoteContext().getRemoteServiceInvoker();
		RemoteServiceResponse response = remoteServiceInvoker.invoke(request, server, connectionTimeout, socketTimeout);

		return response.getResult();
	}

	public static interface ServerSelector {
		Server getServer();
	}

	public static class SolutionServerSelector implements ServerSelector {
		String solutionName;
		Random random;

		public SolutionServerSelector(String solutionName) {
			super();
			this.solutionName = solutionName;
			this.random = new Random();
		}

		@Override
		public Server getServer() {
			ServerContext serverContext = YeonContext.getServerContext();
			Solution solution = serverContext.getSolution(solutionName);
			List<Server> servers = solution.getServers();
			return servers.get(random.nextInt(servers.size()));
		}
	}

	public static class ProjectServerSelector implements ServerSelector {
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
			ServerContext serverContext = YeonContext.getServerContext();
			Project project = serverContext.getProject(solutionName, projectName);

			List<Server> servers = project.getServers();
			return servers.get(random.nextInt(servers.size()));
		}
	}

	public static class SingleServerSelector implements ServerSelector {
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
