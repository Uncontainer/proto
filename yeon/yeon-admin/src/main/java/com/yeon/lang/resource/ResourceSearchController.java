package com.yeon.lang.resource;

import com.yeon.lang.*;
import com.yeon.lang.impl.MapResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/resource/search")
public class ResourceSearchController extends AbstractLangController {
	@Autowired
	private BasicMapResourceService resourceService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("criteria", new ResourceSearchCriteria());
		model.addAttribute("searchTypes", ResourceSearchType.values());
		model.addAttribute("resourceTypes", ResourceType.values());
		model.addAttribute("popup", false);

		return "resource/resource_search";
	}

	@RequestMapping(value = "submit", method = RequestMethod.GET)
	public String search(ResourceSearchCriteria criteria,
		@RequestParam(value = Parameters.PAGE, required = false, defaultValue = "0") int page,
		@RequestParam(value = Parameters.PAGE_SIZE, required = false, defaultValue = "10") int pageSize, ModelMap model) {
		if (pageSize == 0) {
			pageSize = Constants.DEFAULT_PAGE_SIZE;
		}

		Pageable pageable = new PageRequest(page, pageSize);

		Page<MapResource> result = resourceService.search(criteria, pageable);

		model.addAttribute(JsonResultModel.PARAM_RESULT, JsonResultModelBuilder.aSuccess().value(result).build());

		return "json:json_result";
	}
}
