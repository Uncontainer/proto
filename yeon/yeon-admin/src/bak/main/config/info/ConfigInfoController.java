package com.pulsarang.infra.config.info;

import com.pulsarang.infra.AbstractConfigController;
import com.pulsarang.infra.config.ConfigEntityId;
import com.pulsarang.infra.config.ConfigUrlUtils;
import com.pulsarang.infra.config.category.ConfigCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/config/configInfo")
public class ConfigInfoController {
	@Autowired
	ConfigInfoService ciService;

	@Autowired
	ConfigCategoryService ccService;

	@RequestMapping(value = "read/{configCategory}/{configName}", method = RequestMethod.GET)
	public String get(ModelMap model, //
			@PathVariable("configCategory") String configCategory, //
			@PathVariable("configName") String configName) {
		ConfigEntityId configEntityId = new ConfigEntityId(configCategory, configName);
		ConfigInfoEntity configInfoEntity = getConfigInfoSafely(configEntityId);

		model.addAttribute(AbstractConfigController.PARAM_MODE, AbstractConfigController.MODE_READ);
		model.addAttribute("configInfo", configInfoEntity);

		return "/config/info/config_info";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(ModelMap model) {
		List<String> configCategoryNames = ccService.getCategoryNames();

		model.addAttribute(AbstractConfigController.PARAM_MODE, AbstractConfigController.MODE_CREATE);
		model.addAttribute("configCategoryNames", configCategoryNames);

		return "/config/info/config_info";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String createSubmit(@ModelAttribute ConfigInfoEntity configInfo) {
		ciService.createConfigInfo(configInfo);

		return "redirect:" + ConfigUrlUtils.getPropertyCreateUrl(configInfo.getId());
	}

	@RequestMapping(value = "/modify/{configCategory}/{configName}", method = RequestMethod.GET)
	public String modify(ModelMap model, //
			@PathVariable("configCategory") String configCategory, //
			@PathVariable("configName") String configName) {
		ConfigEntityId configEntityId = new ConfigEntityId(configCategory, configName);
		ConfigInfoEntity configInfoEntity = getConfigInfoSafely(configEntityId);

		model.addAttribute(AbstractConfigController.PARAM_MODE, AbstractConfigController.MODE_MODIFY);
		model.addAttribute("configInfo", configInfoEntity);

		return "/config/info/config_info";
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modifySubmit(@ModelAttribute ConfigInfoEntity configInfo) {
		ciService.updateConfigInfo(configInfo);

		return "redirect:" + ConfigUrlUtils.getPropertyModifyUrl(configInfo.getId());
	}

	private ConfigInfoEntity getConfigInfoSafely(ConfigEntityId configId) {
		ConfigInfoEntity configInfo = ciService.getConfigInfo(configId);
		if (configInfo == null) {
			throw new IllegalArgumentException("Config info is not exist. (" + configId + ")");
		}

		return configInfo;
	}
}
