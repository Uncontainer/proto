package com.pulsarang.infra.config.propinfo;

import com.pulsarang.infra.AbstractConfigController;
import com.pulsarang.infra.config.ConfigUrlUtils;
import com.pulsarang.infra.config.category.ConfigCategoryService;
import com.pulsarang.infra.config.propinfo.type.PropertyValueType;
import com.pulsarang.infra.config.validator.ExpectedValidator;
import com.pulsarang.infra.config.validator.Validator;
import com.pulsarang.infra.config.validator.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/config/propertyInfo")
public class ConfigPropertyInfoController {
	@Autowired
	ConfigPropertyInfoService cpiService;
	
	@Autowired
	ConfigCategoryService ccService;

	@RequestMapping("/list")
	public String listAll(ModelMap model) {
		return list(model, null);
	}

	@RequestMapping("/list/{configCategory}")
	public String list(ModelMap model, @PathVariable("configCategory") String configCategory) {
		List<ConfigPropertyInfo> configPropertyInfos = cpiService.getConfigPropertyInfos(configCategory);
		List<String> configCategoryNames = ccService.getCategoryNames();

		model.addAttribute("configPropertyInfos", configPropertyInfos);
		model.addAttribute("configCategoryNames", configCategoryNames);
		model.addAttribute("configCategory", configCategory);

		return "/config/propinfo/property_info_list";
	}

	@RequestMapping(value = "/read/{configCategory}/{propertyName}", method = RequestMethod.GET)
	public String read(ModelMap model, @PathVariable("configCategory") String configCategory, @PathVariable("propertyName") String propertyName) {
		ConfigPropertyInfoId id = new ConfigPropertyInfoId(configCategory, propertyName);
		ConfigPropertyInfo configPropertyInfo = cpiService.getConfigPropertyInfo(id);

		if (configPropertyInfo == null) {
			throw new IllegalArgumentException("Fail to find configPropertyInfo.(" + id + ")");
		}

		model.addAttribute(AbstractConfigController.PARAM_MODE, AbstractConfigController.MODE_READ);
		model.addAttribute("configPropertyInfo", configPropertyInfo);
		model.addAttribute("dataTypeCodes", getDataTypeCodes());

		return "/config/propinfo/property_info";
	}

	@RequestMapping(value = "/remove/{configCategory}/{propertyName}", method = RequestMethod.GET)
	public String remove(ModelMap model, @PathVariable("configCategory") String configCategory, @PathVariable("propertyName") String propertyName) {
		ConfigPropertyInfoId id = new ConfigPropertyInfoId(configCategory, propertyName);
		cpiService.removeConfigPropertyInfo(id);

		return "redirect:" + ConfigUrlUtils.getPropertyInfoListUrl(configCategory);
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(ModelMap model, @RequestParam(value = "configCategory", defaultValue = "") String configCategory) {
		ConfigPropertyInfoId id = new ConfigPropertyInfoId(configCategory, null);
		ConfigPropertyInfo configPropertyInfo = new ConfigPropertyInfo();
		configPropertyInfo.setId(id);
		List<String> configCategoryNames = ccService.getCategoryNames();

		model.addAttribute(AbstractConfigController.PARAM_MODE, AbstractConfigController.MODE_CREATE);
		model.addAttribute("configPropertyInfo", configPropertyInfo);
		model.addAttribute("configCategoryNames", configCategoryNames);
		model.addAttribute("dataTypeCodes", getDataTypeCodes());

		return "/config/propinfo/property_info";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createSubmit(@ModelAttribute ConfigPropertyInfo configPropertyInfo) {
		cpiService.createConfigPropertyInfo(configPropertyInfo);

		return "redirect:" + ConfigUrlUtils.getPropertyInfoModifyUrl(configPropertyInfo.getId());
	}

	@RequestMapping(value = "/modify/{configCategory}/{propertyName}", method = RequestMethod.GET)
	public String modify(ModelMap model, @PathVariable("configCategory") String configCategory, @PathVariable("propertyName") String propertyName) {
		ConfigPropertyInfoId id = new ConfigPropertyInfoId(configCategory, propertyName);
		ConfigPropertyInfo configPropertyInfo = cpiService.getConfigPropertyInfo(id);
		if (configPropertyInfo == null) {
			throw new IllegalArgumentException("Fail to find configPropertyInfo.(" + id + ")");
		}
		Validator validator = ValidatorFactory.createValidator(configPropertyInfo.getValidationExpression());
		if (validator != null && (validator instanceof ExpectedValidator)) {
			String dataType = configPropertyInfo.getDataTypeCode();
			switch (PropertyValueType.valueOf(dataType.toUpperCase())) {
			case LIST:
			case SET:
				break;
			default:
				configPropertyInfo.setCandidates(((ExpectedValidator) validator).getCandidates());
				break;
			}
		}

		model.addAttribute(AbstractConfigController.PARAM_MODE, AbstractConfigController.MODE_MODIFY);
		model.addAttribute("configPropertyInfo", configPropertyInfo);
		model.addAttribute("dataTypeCodes", getDataTypeCodes());
		// setParameterOnContext(ConfigParameters.RANGE);
		// setParameterOnContext(Me2ConfigParameters.PROJECT_NAME);

		return "/config/propinfo/property_info";
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modifySubmit(@ModelAttribute ConfigPropertyInfo configPropertyInfo) {
		cpiService.modifyConfigPropertyInfo(configPropertyInfo);

		return "redirect:" + ConfigUrlUtils.getPropertyInfoReadUrl(configPropertyInfo.getId());
	}

	// public String expireAll() {
	// Map<String, Object> paramMap = getParamMap(new String[]
	// {Me2ConfigParameters.SOLUTION_NAME}, null);
	//
	// Event event = new Event(ConfigConstants.TARGET_CONFIG_ITEM_INFO,
	// ReloadEventType.EXPIRE.name(), paramMap);
	// return sendEvent(getParameter(Me2ConfigParameters.SOLUTION_NAME), event,
	// getListUrl());
	// }

	private List<String> getDataTypeCodes() {
		PropertyValueType[] types = PropertyValueType.values();
		List<String> dataTypeCodes = new ArrayList<String>(types.length);
		for (PropertyValueType type : types) {
			dataTypeCodes.add(type.toString());
		}
		return dataTypeCodes;
	}
}
