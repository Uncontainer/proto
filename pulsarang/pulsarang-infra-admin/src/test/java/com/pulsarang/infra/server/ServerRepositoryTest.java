package com.pulsarang.infra.server;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pulsarang.test.spring.SpringTestBase;

public class ServerRepositoryTest extends SpringTestBase {

	@Autowired
	private ServerRepository repo;

	@Test
	public void test() {
		// ServerEntity server = new ServerEntity();
		// server.setSolutionName("a");
		// server.setProjectName("b");
		// server.setIp("1.2.3.4");
		//
		// repo.saveAndFlush(server);

		List<String> solutionNames = repo.findSolutionNames();
		repo.findNormalServers("dummy", "dummy");
		System.out.println(solutionNames);
	}
}
