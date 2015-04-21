package com.pulsarang.infra.config.propinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/config/projectPropertyMapping")
public class ProjectPropertyMappingController {
	@Autowired
	ProjectPropertyMappingService ptpmService;

	@RequestMapping("/list/{configCategory}/{propertyName}")
	public String list(ModelMap model, //
			@PathVariable("configCategory") String configCategory, //
			@PathVariable("propertyName") String propertyName) {
		ConfigPropertyInfoId configPropertyInfoId = new ConfigPropertyInfoId(configCategory, propertyName);
		List<String> projectNames = ptpmService.getProjectNames(configPropertyInfoId);

		model.addAttribute("config_item_project_infos", projectNames);
		model.addAttribute("configPropertyInfoId", configPropertyInfoId);
		model.addAttribute("propertyName", propertyName);

		return "/config/project_property_mapping_list";
	}

	@RequestMapping("/remove/{configCategory}/{propertyName}/{projectName}")
	public String remove(@PathVariable("configCategory") String configCategory, //
			@PathVariable("propertyName") String propertyName, //
			@PathVariable("projectName") String projectName) {
		ProjectPropertyMappingId id = new ProjectPropertyMappingId(configCategory, propertyName, projectName);
		ptpmService.removeProjectConfigPropertyMapping(id);

		return "redirect:" + getListUrl(id.getConfigPropertyInfoId());
	}

	@RequestMapping(value = "/create/{configCategory}/{propertyName}", method = RequestMethod.GET)
	public String create(ModelMap model, //
			@PathVariable("configCategory") String configCategory, //
			@PathVariable("propertyName") String propertyName, //
			@RequestParam("selectedProjectName") String selectedProjectName) {
		model.addAttribute("configCategory", configCategory);
		model.addAttribute("propertyName", propertyName);
		model.addAttribute("selectedProjectName", selectedProjectName);

		return "/config/propinfo/project_property_mapping";
	}

	public String createSubmit(@ModelAttribute ProjectPropertyMapping mapping) {
		ptpmService.createProjectConfigPropertyMapping(mapping);

		return "redirect:" + getListUrl(mapping.getId().getConfigPropertyInfoId());
	}

	private String getListUrl(ConfigPropertyInfoId configPropertyInfoId) {
		return "/config/propinfo/projectPropertyMapping/list/" + configPropertyInfoId.getConfigCategory() + "/" + configPropertyInfoId.getPropertyName();
	}
}
