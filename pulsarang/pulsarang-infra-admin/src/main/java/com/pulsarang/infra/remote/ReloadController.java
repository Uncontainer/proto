package com.pulsarang.infra.remote;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.AbstractConfigController;
import com.pulsarang.infra.ServerSelectCriteria;
import com.pulsarang.infra.remote.reload.ReloadService;
import com.pulsarang.infra.remote.table.TableBuilder;
import com.pulsarang.infra.server.ServerEntity;

@Controller
@RequestMapping("/rpc/reload")
public class ReloadController extends AbstractConfigController {
	private final Logger log = LoggerFactory.getLogger(ReloadController.class);

	@RequestMapping("/list/{targetName}")
	public String list(ModelMap model, @ModelAttribute ServerSelectCriteria criteria, @PathVariable("targetName") String targetName) {

		ServerEntity serverEntity = selectServer(model, criteria);

		List<MapModel> list = null;
		if (serverEntity != null) {
			ReloadService reloadService = RemoteServiceProxy.newServerProxy(ReloadService.class, targetName, serverEntity.getServer(), 3000);
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
