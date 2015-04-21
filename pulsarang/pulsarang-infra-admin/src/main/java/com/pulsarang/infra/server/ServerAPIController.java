package com.pulsarang.infra.server;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pulsarang.infra.util.ApiUtils;

@Controller
@RequestMapping("/api/config/server")
public class ServerAPIController {
	@Autowired
	private ServerService serverService;

	@RequestMapping("/list")
	public void listAll(HttpServletResponse response) throws IOException {
		List<Server> servers = serverService.getServerInfos();

		ApiUtils.writeMapModels(response, servers);
	}

	@RequestMapping("/inform/{type}")
	public void info(@PathVariable("type") String type, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Server server = ApiUtils.readMapModel(request, Server.class);
		serverService.updateServer(server, "startup".equals(type));
	}
}
