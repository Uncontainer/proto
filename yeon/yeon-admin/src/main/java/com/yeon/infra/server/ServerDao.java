package com.yeon.infra.server;

import com.yeon.YeonContext;
import com.yeon.YeonParameters;
import com.yeon.lang.ResourceService;
import com.yeon.lang.query.ResultSet;
import com.yeon.server.Server;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ServerDao implements InitializingBean {
	@Autowired
	YeonContext yeonContext;

	private ResourceService resourceService;

	@Override
	public void afterPropertiesSet() throws Exception {
		resourceService = YeonContext.getResourceContext().getResourceService();
	}

	public List<String> findSolutionNames() {
		List<String> solutionNames = new ArrayList<String>();
		String query = "SELECT DISTINCT " + YeonParameters.SOLUTION_NAME + " FROM yeon.server";
		ResultSet rs = resourceService.select(query);
		try {
			while (rs.next()) {
				solutionNames.add((String) rs.getObject(1));
			}
		} finally {
			rs.close();
		}

		return solutionNames;
	}

	public List<String> findProjectNames(String solutionName) {
		List<String> projectNames = new ArrayList<String>();
		String query = "SELECT DISTINCT " + YeonParameters.PROCESSOR_NAME + " FROM yeon.server WHERE " + YeonParameters.SOLUTION_NAME + "=" + solutionName;
		ResultSet rs = resourceService.select(query);
		try {
			while (rs.next()) {
				projectNames.add((String) rs.getObject(1));
			}
		} finally {
			rs.close();
		}

		return projectNames;
	}

	public List<Server> findNormalServers(String solutionName, String projectName) {
		String query = "SELECT * FROM yeon.server WHERE " +
				YeonParameters.SOLUTION_NAME + "=" + solutionName +
				" AND " + YeonParameters.PROCESSOR_NAME + "=" + projectName;

		List<Server> servers = new ArrayList<Server>();
		ResultSet rs = resourceService.select(query);
		try {
			while (rs.next()) {
				servers.add((Server) rs.get());
			}
		} finally {
			rs.close();
		}

		return servers;
	}

	public Server findServer(Server server) {
		String query = "SELECT * FROM yeon.server WHERE " +
				YeonParameters.SOLUTION_NAME + "=" + server.getSolutionName() +
				" AND " + YeonParameters.PROCESSOR_NAME + "=" + server.getProjectName() +
				" AND ip=" + server.getIp();
		ResultSet rs = resourceService.select(query);
		try {
			return rs.next() ? (Server) rs.get() : null;
		} finally {
			rs.close();
		}
	}

	public void insert(Server server) {
		resourceService.add(server);
	}

	public List<Server> selectAll() {
		List<Server> servers = new ArrayList<Server>();
		ResultSet rs = resourceService.select("SELECT * from yeon.server");
		try {
			while (rs.next()) {
				servers.add((Server) rs.get());
			}
		} finally {
			rs.close();
		}

		return servers;
	}

	public List<Server> selectByIp(String ip) {
		List<Server> servers = new ArrayList<Server>();
		ResultSet rs = resourceService.select("SELECT * FROM yeon.server WHERE ip=" + ip);
		try {
			while (rs.next()) {
				servers.add((Server) rs.get());
			}
		} finally {
			rs.close();
		}

		return servers;
	}

	public Server select(String id) {
		return (Server) resourceService.get(id);
	}

	public void modify(Server server) {
		resourceService.modify(server);
	}

	public void delete(Server server) {
		resourceService.remove(server.getId());
	}
}
