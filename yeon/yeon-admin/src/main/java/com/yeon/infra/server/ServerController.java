package com.yeon.infra.server;

import com.yeon.remote.bulk.RemoteServiceBulkProxy;
import com.yeon.remote.bulk.RemoteServiceResponseList;
import com.yeon.remote.bulk.RemoteServiceResponseListHolder;
import com.yeon.remote.reload.ReloadService;
import com.yeon.server.Server;
import com.yeon.server.ServerCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/server")
public class ServerController {

	@Autowired
	private ServerService serverService;

	@RequestMapping("/list")
	public String list(ModelMap model) {
		List<Server> servers = serverService.listAll();

		model.addAttribute("servers", servers);

		return "/server/list";
	}

	@RequestMapping(value = "/read", method = RequestMethod.GET)
	public String read(@ModelAttribute Server server, ModelMap model) {
		Server savedServer = serverService.getServer(server.getId());

		model.addAttribute("mode", "read");
		model.addAttribute("server", savedServer);

		return "/server/item";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createForm(ModelMap model) {
		Server server = new Server();

		model.addAttribute("mode", "create");
		model.addAttribute("server", server);

		return "/server/item";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createSubmit(@ModelAttribute Server server, ModelMap model) throws Exception {
		serverService.addServer(server);

		return expireAndRedirectToRead(model, server);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public String modifyForm(@ModelAttribute Server server, ModelMap model) {
		Server savedServer = serverService.getServer(server.getId());
		model.addAttribute("mode", "modify");
		model.addAttribute("server", savedServer);

		return "/server/item";
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modifySubmit(@ModelAttribute Server server, ModelMap model) throws Exception {
		if (server.getId() == null) {
			throw new IllegalArgumentException("Empty id.");
		}

		Server savedServer = serverService.getServer(server.getId());
		server.setRunningStatus(savedServer.getRunningStatus());
		// TODO 시작/종료 시간 셋팅 로직 주석 풀기
//		server.setLastStartupDate(savedServer.getLastStartupDate());
//		server.setLastShutdownDate(savedServer.getLastShutdownDate());

		serverService.modifyServer(server);
		return expireAndRedirectToRead(model, server);
	}

	@RequestMapping(value = "/remove"/* , method = RequestMethod.POST */)
	public String remove(@ModelAttribute Server server) {
		serverService.removeServer(server);

		return "redirect:/server/list";
	}

	private String expireAndRedirectToRead(ModelMap model, Server server) throws Exception {
		ReloadService reloadService = RemoteServiceBulkProxy.newAllSolutionProxy(ReloadService.class, ServerCache.NAME, 5000);
		reloadService.expire(null);

		RemoteServiceResponseList response = RemoteServiceResponseListHolder.getAndClear();

		model.addAttribute("success", response.isSuccess());
		model.addAttribute("result", response.getEntries());
		model.addAttribute("callbackUrl", "/server/read?id=" + server.getId());

		return "/update/refreshResult";
	}
}
