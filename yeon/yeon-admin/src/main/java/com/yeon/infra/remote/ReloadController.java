package com.yeon.infra.remote;

import com.yeon.infra.AbstractConfigController;
import com.yeon.infra.ServerSelectCriteria;
import com.yeon.infra.remote.table.TableBuilder;
import com.yeon.remote.RemoteServiceProxy;
import com.yeon.remote.reload.ReloadService;
import com.yeon.server.Server;
import com.yeon.util.MapModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/rpc/reload")
public class ReloadController extends AbstractConfigController {
	private final Logger log = LoggerFactory.getLogger(ReloadController.class);

	@RequestMapping("/list/{targetName}")
	public String list(ModelMap model, @ModelAttribute ServerSelectCriteria criteria, @PathVariable("targetName") String targetName) {
		Server server = selectServer(model, criteria);

		List<MapModel> list = null;
		if (server != null) {
			ReloadService reloadService = RemoteServiceProxy.newServerProxy(ReloadService.class, targetName, server, 3000);
			try {
				list = reloadService.list(null);
			} catch (Exception e) {
				log.error("Fail to get reload list", e);
				model.addAttribute("errorMessage", e.getMessage());
			}
		} else {
			model.addAttribute("errorMessage", "Target sever does not exist.");
		}

		if (list == null) {
			list = Collections.emptyList();
		}

		String refreshUrl = "/rpc/reload/list/" + targetName;

		model.addAttribute("table", TableBuilder.build(targetName, refreshUrl, list));

		return "/rpc/reload_list";
	}
}
