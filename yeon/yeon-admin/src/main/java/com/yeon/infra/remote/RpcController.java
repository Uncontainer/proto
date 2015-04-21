package com.yeon.infra.remote;

import com.yeon.infra.AbstractConfigController;
import com.yeon.infra.ServerSelectCriteria;
import com.yeon.remote.RemoteServiceMonitor;
import com.yeon.remote.RemoteServiceProxy;
import com.yeon.remote.reload.ReloadService;
import com.yeon.server.Server;
import com.yeon.util.MapModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/rpc")
public class RpcController extends AbstractConfigController {
	private final Logger log = LoggerFactory.getLogger(RpcController.class);

	@RequestMapping("list")
	public String list(ModelMap model, @ModelAttribute ServerSelectCriteria criteria) throws Exception {

		Server server = selectServer(model, criteria);

		List<MapModel> rpcInfos = null;
		if (server != null) {
			ReloadService reloadService = RemoteServiceProxy.newServerProxy(ReloadService.class, RemoteServiceMonitor.NAME, server, 3000);
			try {
				rpcInfos = reloadService.list(null);
			} catch (Exception e) {
				log.error("Fail to get rpc list", e);
				model.addAttribute("errorMessage", e.getMessage());
			}
		} else {
			model.addAttribute("errorMessage", "Target sever does not exist.");
		}

		if (rpcInfos == null) {
			rpcInfos = Collections.emptyList();
		}

		model.addAttribute("rpcInfos", rpcInfos);

		return "/rpc/list";
	}
}
