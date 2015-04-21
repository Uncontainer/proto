package com.pulsarang.infra.server;

import com.jeojiri.lang.SpringTestBase;
import com.yeon.infra.server.ServerDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ServerRepositoryTest extends SpringTestBase {

	@Autowired
	private ServerDao dao;

	@Test
	public void test() {
		// ServerEntity server = new ServerEntity();
		// server.setSolutionName("a");
		// server.setProjectName("b");
		// server.setIp("1.2.3.4");
		//
		// repo.saveAndFlush(server);

		List<String> solutionNames = dao.findSolutionNames();
		dao.findNormalServers("dummy", "dummy");
		System.out.println(solutionNames);
	}
}
