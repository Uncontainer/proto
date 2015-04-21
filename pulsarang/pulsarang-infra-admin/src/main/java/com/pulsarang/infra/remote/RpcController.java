package com.pulsarang.infra.remote;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.AbstractConfigController;
import com.pulsarang.infra.ServerSelectCriteria;
import com.pulsarang.infra.remote.reload.ReloadService;
import com.pulsarang.infra.server.ServerEntity;

@Controller
@RequestMapping("/rpc")
public class RpcController extends AbstractConfigController {
	private final Logger log = LoggerFactory.getLogger(RpcController.class);

	@RequestMapping("list")
	public String list(ModelMap model, @ModelAttribute ServerSelectCriteria criteria) throws Exception {

		ServerEntity serverEntity = selectServer(model, criteria);

		List<MapModel> rpcInfos = null;
		if (serverEntity != null) {
			ReloadService reloadService = RemoteServiceProxy.newServerProxy(ReloadService.class, RemoteServiceReloadable.NAME, serverEntity.getServer(),
					3000);
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
