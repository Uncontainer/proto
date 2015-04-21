package com.pulsarang.infra.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pulsarang.infra.remote.RemoteServiceBulkProxy;
import com.pulsarang.infra.remote.RemoteServiceResponseList;
import com.pulsarang.infra.remote.RemoteServiceResponseListHolder;
import com.pulsarang.infra.remote.reload.ReloadService;

@Controller
@RequestMapping("/server")
public class ServerController {

	@Autowired
	private ServerService serverService;

	@RequestMapping("/list")
	public String list(ModelMap model) {
		List<ServerEntity> servers = serverService.listAll();

		model.addAttribute("servers", servers);

		return "/server/list";
	}

	@RequestMapping(value = "/read", method = RequestMethod.GET)
	public String read(@ModelAttribute ServerEntity server, ModelMap model) {
		ServerEntity savedServer = serverService.getServer(server.getId());

		model.addAttribute("mode", "read");
		model.addAttribute("server", savedServer);

		return "/server/item";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createForm(ModelMap model) {
		ServerEntity server = new ServerEntity();

		model.addAttribute("mode", "create");
		model.addAttribute("server", server);

		return "/server/item";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createSubmit(@ModelAttribute ServerEntity server, ModelMap model) throws Exception {
		serverService.addServer(server);

		return expireAndRedirectToRead(model, server);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public String modifyForm(@ModelAttribute ServerEntity server, ModelMap model) {
		ServerEntity savedServer = serverService.getServer(server.getId());
		model.addAttribute("mode", "modify");
		model.addAttribute("server", savedServer);

		return "/server/item";
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modifySubmit(@ModelAttribute ServerEntity server, ModelMap model) throws Exception {
		if (server.getId() == null) {
			throw new IllegalArgumentException("Empty id.");
		}

		ServerEntity savedServer = serverService.getServer(server.getId());
		server.setRunningStatus(savedServer.getRunningStatus());
		server.setLastStartupDate(savedServer.getLastStartupDate());
		server.setLastShutdownDate(savedServer.getLastShutdownDate());

		serverService.modifyServer(server);
		return expireAndRedirectToRead(model, server);
	}

	@RequestMapping(value = "/remove"/* , method = RequestMethod.POST */)
	public String remove(@ModelAttribute ServerEntity server) {
		serverService.removeServer(server);

		return "redirect:/server/list";
	}

	private String expireAndRedirectToRead(ModelMap model, ServerEntity server) throws Exception {
		ReloadService reloadService = RemoteServiceBulkProxy.newAllSolutionProxy(ReloadService.class, ServerCache.NAME, 5000);
		reloadService.expire(null);

		RemoteServiceResponseList response = RemoteServiceResponseListHolder.getAndClear();

		model.addAttribute("success", response.isSuccess());
		model.addAttribute("result", response.getEntries());
		model.addAttribute("callbackUrl", "/server/read?id=" + server.getId());

		return "/update/refreshResult";
	}
}
