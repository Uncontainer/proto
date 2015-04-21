package com.pulsarang.infra.config.prop;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.pulsarang.infra.AbstractConfigController;
import com.pulsarang.infra.config.ConfigEntityId;
import com.pulsarang.infra.config.ConfigUrlUtils;
import com.pulsarang.infra.config.info.ConfigInfoEntity;
import com.pulsarang.infra.config.info.ConfigInfoService;
import com.pulsarang.infra.util.EnvironmentUtil;

@Controller
@RequestMapping("/config/property")
public class ConfigPropertyController {
	private static final String PARAM_DEFAULT_RUN_ENV = "default_run_env";
	private static final String PARAM_DEFAULT_TICKET = "default_config";

	@Autowired
	ConfigInfoService ciService;

	@Autowired
	ConfigPropertyService cpService;

	@RequestMapping(value = "/read/{configCategory}/{configName}", method = RequestMethod.GET)
	public String get(ModelMap model, //
			@PathVariable("configCategory") String configCategory, //
			@PathVariable("configName") String configName) {
		ConfigEntityId configEntityId = new ConfigEntityId(configCategory, configName);
		List<ConfigProperty> configProperites = cpService.listRich(configEntityId, null);

		model.addAttribute(AbstractConfigController.PARAM_MODE, AbstractConfigController.MODE_READ);
		model.addAttribute("configCategory", configCategory);
		model.addAttribute("configName", configName);
		model.addAttribute("configProperites", configProperites);
		model.addAttribute("env", EnvironmentUtil.getRunEnvironment());

		return "/config/prop/config_property_list";
	}

	@RequestMapping(value = "/create/{configCategory}/{configName}", method = RequestMethod.GET)
	public String create(ModelMap model, //
			@PathVariable(value = "configCategory") String configCategory, //
			@PathVariable(value = "configName") String configName, //
			@RequestParam(value = "defaultRunEnv", required = false) String defaultRunEnv, //
			@RequestParam(value = "defaultConfigName", required = false) String defaultConfigName) {
		ConfigEntityId configEntityId = new ConfigEntityId(configCategory, configName);
		ConfigInfoEntity configInfo = ciService.getConfigInfo(configEntityId);
		if (configInfo == null) {
			throw new IllegalArgumentException("Config '" + configEntityId + "' does not exist.");
		}

		Map<String, Object> defaultConfig;
		if (StringUtils.isNotEmpty(defaultRunEnv) && StringUtils.isNotEmpty(defaultConfigName)) {
			defaultConfig = cpService.get(configEntityId, defaultRunEnv);
		} else {
			defaultConfig = Collections.emptyMap();
		}

		List<ConfigProperty> configProperites = cpService.listRich(configEntityId, defaultConfig);

		model.addAttribute(AbstractConfigController.PARAM_MODE, AbstractConfigController.MODE_CREATE);
		model.addAttribute("configInfo", configInfo);
		model.addAttribute("configProperites", configProperites);
		model.addAttribute("env", EnvironmentUtil.getRunEnvironment());
		model.addAttribute(PARAM_DEFAULT_RUN_ENV, defaultRunEnv);
		model.addAttribute(PARAM_DEFAULT_TICKET, defaultConfigName);

		return "/config/prop/config_property_list";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createSubmit(ModelMap model, //
			@RequestParam("configCategory") String configCategory, //
			@RequestParam("configName") String configName, //
			@RequestParam Map<String, String> properties, //
			@RequestParam(value = "solutionName", required = false) String solutionName, //
			@RequestParam(value = "skipValidation", defaultValue = "false") boolean skipValidation) throws Exception {
		ConfigEntityId configEntityId = new ConfigEntityId(configCategory, configName);
		cpService.create(configEntityId, properties, skipValidation);

		return "redirect:" + ConfigUrlUtils.getConfigDetailUrl(solutionName, configEntityId);
	}

	@RequestMapping(value = "/modify/{configCategory}/{configName}", method = RequestMethod.GET)
	public String modify(ModelMap model, //
			@PathVariable("configCategory") String configCategory, //
			@PathVariable("configName") String configName) {
		// TODO 원격에서 가져오는 로직 추가.
		Map<String, Object> appConfig = new HashMap<String, Object>();

		ConfigEntityId configEntityId = new ConfigEntityId(configCategory, configName);
		List<ConfigProperty> configProperites = cpService.listRich(configEntityId, appConfig);

		model.addAttribute(AbstractConfigController.PARAM_MODE, AbstractConfigController.MODE_MODIFY);
		model.addAttribute("solutionName", "");
		model.addAttribute("configCategory", configCategory);
		model.addAttribute("configName", configName);
		model.addAttribute("configProperites", configProperites);
		model.addAttribute("env", EnvironmentUtil.getRunEnvironment());

		return "/config/prop/config_property_list";
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modifySubmit(ModelMap model, //
			@RequestParam("configCategory") String configCategory, //
			@RequestParam("configName") String configName, //
			@RequestParam Map<String, Object> properties, //
			@RequestParam(value = "solutionName", required = false) String solutionName, //
			@RequestParam(value = "skipValidation", defaultValue = "false") boolean skipValidation) throws Exception {
		ConfigEntityId configEntityId = new ConfigEntityId(configCategory, configName);
		cpService.update(configEntityId, properties, skipValidation);

		return "redirect:" + ConfigUrlUtils.getConfigDetailUrl(solutionName, configEntityId);
	}
}
