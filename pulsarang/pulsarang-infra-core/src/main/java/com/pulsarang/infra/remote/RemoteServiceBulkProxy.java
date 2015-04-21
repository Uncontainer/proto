package com.pulsarang.infra.remote;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.server.Project;
import com.pulsarang.infra.server.Server;
import com.pulsarang.infra.server.ServerContext;
import com.pulsarang.infra.server.Solution;
import com.pulsarang.infra.server.SolutionProjectPair;

public class RemoteServiceBulkProxy implements InvocationHandler {
	public static <T> T newAllSolutionProxy(Class<T> clazz, String target, int socketTimeout) {
		return newProxy(clazz, target, new AllSolutionServerSelector(), socketTimeout);
	}

	public static <T> T newSolutionProxy(Class<T> clazz, String target, String solutionName, int socketTimeout) {
		return newProxy(clazz, target, new SolutionServerSelector(solutionName), socketTimeout);
	}

	public static <T> T newProjectProxy(Class<T> clazz, String target, SolutionProjectPair pair, int socketTimeout) {
		return newProxy(clazz, target, new ProjectServerSelector(pair.getSolutionName(), pair.getProjectName()), socketTimeout);
	}

	public static <T> T newServerProxy(Class<T> clazz, String target, List<Server> servers, int socketTimeout) {
		return newProxy(clazz, target, new FixedServerSelector(servers), socketTimeout);
	}

	@SuppressWarnings("unchecked")
	private static <T> T newProxy(Class<T> clazz, String target, ServerSelector selector, int socketTimeout) {
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] { clazz }, new RemoteServiceBulkProxy(selector, target,
				socketTimeout));
	}

	private final ServerSelector selector;
	private String target;
	private int socketTimeout;
	private int connectionTimeout = 2000;

	private RemoteServiceBulkProxy(ServerSelector selector, String target, int socketTimeout) {
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

		List<Server> servers = selector.getServers();
		if (!servers.isEmpty()) {
			// TODO 각 결과에 대해 populate를 아직 수행하지 않았음.
			RemoteServiceResponseList responses = RemoteServiceInvokeUtil.sendRequest(servers, request, connectionTimeout, socketTimeout, false);

			// TODO ThreadLocal에서 무조건 삭제하는 조건을 강제할 수 있을 지 확인.
			RemoteServiceResponseListHolder.set(responses);
		}

		return null;
	}

	private static interface ServerSelector {
		List<Server> getServers();
	}

	private static class AllSolutionServerSelector implements ServerSelector {

		public AllSolutionServerSelector() {
			super();
		}

		@Override
		public List<Server> getServers() {
			ServerContext serverContext = InfraContextFactory.getInfraContext().getServerContext();
			List<Server> servers = new ArrayList<Server>();
			Collection<Solution> solutions = serverContext.getSolutions();
			for (Solution solution : solutions) {
				servers.addAll(solution.getServers());
			}

			return servers;
		}
	}

	private static class SolutionServerSelector implements ServerSelector {
		String solutionName;

		public SolutionServerSelector(String solutionName) {
			super();
			this.solutionName = solutionName;
		}

		@Override
		public List<Server> getServers() {
			ServerContext serverContext = InfraContextFactory.getInfraContext().getServerContext();
			Solution solution = serverContext.getSolution(solutionName);
			return solution.getServers();
		}
	}

	private static class ProjectServerSelector implements ServerSelector {
		String solutionName;
		String projectName;

		public ProjectServerSelector(String solutionName, String projectName) {
			super();
			this.solutionName = solutionName;
			this.projectName = projectName;
		}

		@Override
		public List<Server> getServers() {
			ServerContext serverContext = InfraContextFactory.getInfraContext().getServerContext();
			Project project = serverContext.getProject(solutionName, projectName);

			return project.getServers();
		}
	}

	private static class FixedServerSelector implements ServerSelector {
		List<Server> servers;

		public FixedServerSelector(List<Server> servers) {
			super();
			this.servers = servers;
		}

		@Override
		public List<Server> getServers() {
			return servers;
		}
	}
}
