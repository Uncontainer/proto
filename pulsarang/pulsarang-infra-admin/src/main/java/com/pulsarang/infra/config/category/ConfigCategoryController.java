package com.pulsarang.infra.config.category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.pulsarang.infra.AbstractConfigController;
import com.pulsarang.infra.config.ConfigUrlUtils;
import com.pulsarang.infra.config.scope.ConfigScopeType;

@Controller
@RequestMapping("/config/category")
public class ConfigCategoryController extends AbstractConfigController {
	@Autowired
	ConfigCategoryService ccService;
	
	@RequestMapping("/list")
	public String listAll(ModelMap model) {
		List<ConfigCategoryEntity> configCategories = ccService.getConfigCategories();

		model.addAttribute("configCategories", configCategories);

		return "/config/category/config_category_list";
	}

	@RequestMapping(value = "/read/{categoryName}", method = RequestMethod.GET)
	public String read(ModelMap model, @PathVariable("categoryName") String categoryName) {
		ConfigCategoryEntity configCategory = ccService.getConfigCategory(categoryName);

		if (configCategory == null) {
			throw new IllegalArgumentException("Fail to find configCategory.(" + categoryName + ")");
		}

		setModeRead(model);
		model.addAttribute("configCategory", configCategory);

		return "/config/category/config_category";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(ModelMap model, @RequestParam(value = "configCategory", defaultValue = "") String configCategory) {
		ConfigCategoryEntity configCategoryEntity = new ConfigCategoryEntity();
		configCategoryEntity.setName(configCategory);
		configCategoryEntity.setScopeTypeCode(ConfigScopeType.PROJECT.name());

		setModeCreate(model);
		model.addAttribute("configCategory", configCategoryEntity);
		model.addAttribute("scopeTypeCodes", getScopeTypeCodes());

		return "/config/category/config_category";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createSubmit(@ModelAttribute ConfigCategoryEntity configCategory) {
		ccService.createConfigCategory(configCategory);

		return "redirect:" + ConfigUrlUtils.getConfigCategoryModifyUrl(configCategory.getName());
	}

	@RequestMapping(value = "/remove/{categoryName}", method = RequestMethod.GET)
	public String remove(ModelMap model, @PathVariable("categoryName") String categoryName) {
		ccService.removeConfigCategory(categoryName);

		return "redirect:" + ConfigUrlUtils.getConfigCategoryListUrl();
	}

	@RequestMapping(value = "/modify/{categoryName}", method = RequestMethod.GET)
	public String modify(ModelMap model, @PathVariable("categoryName") String categoryName) {
		ConfigCategoryEntity configCategory = ccService.getConfigCategory(categoryName);
		if (configCategory == null) {
			throw new IllegalArgumentException("Fail to find configCategory.(" + categoryName + ")");
		}

		setModeModify(model);
		model.addAttribute("configCategory", configCategory);
		model.addAttribute("scopeTypeCodes", getScopeTypeCodes());

		return "/config/category/config_category";
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modifySubmit(@ModelAttribute ConfigCategoryEntity configCategory) {
		ccService.modifyConfigCategory(configCategory);

		return "redirect:" + ConfigUrlUtils.getConfigCategoryListUrl();
	}

	private List<String> getScopeTypeCodes() {
		ConfigScopeType[] types = ConfigScopeType.values();
		List<String> scopeTypeCodes = new ArrayList<String>(types.length);
		for (ConfigScopeType type : types) {
			scopeTypeCodes.add(type.toString());
		}
		return scopeTypeCodes;
	}

}
