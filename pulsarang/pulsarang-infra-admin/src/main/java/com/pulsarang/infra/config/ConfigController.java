package com.pulsarang.infra.config;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pulsarang.infra.AbstractConfigController;
import com.pulsarang.infra.ServerSelectCriteria;
import com.pulsarang.infra.config.info.ConfigInfoEntity;
import com.pulsarang.infra.remote.RemoteServiceRequest;
import com.pulsarang.infra.server.SolutionProjectPair;
import com.pulsarang.infra.update.ConfigUpdateRequest;

@Controller
@RequestMapping("/config")
public class ConfigController extends AbstractConfigController {
	@Autowired
	ConfigService configService;

	@RequestMapping("/list")
	public String listAll(ModelMap model) {
		List<ConfigInfoEntity> configInfos = configService.getConfigInfos();

		// TODO solutionName 설정
		model.addAttribute("solutionName", "");
		model.addAttribute("configInfos", configInfos);

		return "/config/config_list";
	}

	@RequestMapping(value = "/read/{configCategory}/{configName}", method = RequestMethod.GET)
	public String get(ModelMap model, //
			@PathVariable("configCategory") String configCategory, //
			@PathVariable("configName") String configName, //
			@ModelAttribute ServerSelectCriteria criteria) throws Exception {
		ConfigEntityId configEntityid = new ConfigEntityId(configCategory, configName);

		List<Map<String, Object>> configOptions = configService.getDisplaying(configEntityid);

		model.addAttribute("solutionName", "");
		model.addAttribute("configCategory", configCategory);
		model.addAttribute("configName", configName);
		model.addAttribute("configOptions", configOptions);

		return "/config/config_detail";
	}

	@RequestMapping(value = "/remove/{configCategory}/{configName}")
	public String remove(ModelMap model, //
			@PathVariable("configCategory") String configCategory, //
			@PathVariable("configName") String configName) {
		ConfigEntityId configEntityid = new ConfigEntityId(configCategory, configName);

		configService.remove(configEntityid);

		// TODO solutionName 설정
		String solutionName = "";
		return expire(solutionName, configEntityid);
	}

	@RequestMapping(value = "/refresh/{configCategory}/{configName}")
	public String refresh(ModelMap model, //
			@PathVariable("configCategory") String configCategory, //
			@PathVariable("configName") String configName) {
		ConfigEntityId configEntityid = new ConfigEntityId(configCategory, configName);

		// return sendEvent(solutionName, request, getListUrl(solutionName));
		return redirectToRefresh(model, configEntityid);
	}

	public String expire(String solutionName, @ModelAttribute ConfigEntityId configEntityid) {
		RemoteServiceRequest request = new RemoteServiceRequest();
		request.setTarget(ConfigCache.NAME);
		request.setMethodName("expire");
		// TODO 지정된 config만 expire하는 코드 추가.

		return sendEvent(solutionName, request, getListUrl(solutionName));
	}

	public String expireAll(String solutionName) {
		RemoteServiceRequest request = new RemoteServiceRequest();
		request.setTarget(ConfigCache.NAME);
		request.setMethodName("expire");

		return sendEvent(solutionName, request, getListUrl(solutionName));
	}

	private String sendEvent(String solutionName, RemoteServiceRequest request, String listUrl) {
		return "redirect:" + getListUrl(solutionName);
	}

	private String getListUrl(String solutionName) {
		return "/config/list?solutionName=" + solutionName;
	}

	private String redirectToRefresh(ModelMap model, ConfigEntityId configEntityid) {
		String solutionName = "";
		ConfigUpdateRequest cuRequest = new ConfigUpdateRequest(configEntityid);
		cuRequest.setScopeProject(SolutionProjectPair.INFRA_ADMIN
		// , new SolutionProjectPair(eventProcessor.getSolutionName(),
		// eventProcessor.getProjectName())
				);
		cuRequest.setCallbackUrl(ConfigUrlUtils.getConfigDetailUrl(solutionName, configEntityid));

		return cuRequest.redirect(model);
	}
}
