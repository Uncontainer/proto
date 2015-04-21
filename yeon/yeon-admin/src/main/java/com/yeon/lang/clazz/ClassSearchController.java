package com.yeon.lang.clazz;

import com.yeon.lang.AbstractLangController;
import com.yeon.lang.JsonResultModel;
import com.yeon.lang.JsonResultModelBuilder;
import com.yeon.lang.SimpleResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/class/search")
public class ClassSearchController extends AbstractLangController {
	@Autowired
	private MapClassService classService;

	@RequestMapping(method = RequestMethod.GET)
	public String create(ModelMap model) {
		model.addAttribute("criteria", new ClassSearchCriteria());
		model.addAttribute("types", ClassSearchType.values());
		model.addAttribute("popup", false);

		return "class/class_search";
	}

	@RequestMapping(value = "popup", method = RequestMethod.GET)
	public String createPopup(@RequestParam("tagId") String tagId, ModelMap model) {
		model.addAttribute("criteria", new ClassSearchCriteria());
		model.addAttribute("types", ClassSearchType.values());
		model.addAttribute("tagId", tagId);
		model.addAttribute("popup", true);

		return "popup:class/class_search";
	}

	@RequestMapping(value = "submit", method = RequestMethod.GET)
	public String search(ClassSearchCriteria criteria, ModelMap model) {
		model.addAttribute("criteria", criteria);

		Page<SimpleResource> list = classService.search(criteria);

		model.addAttribute(JsonResultModel.PARAM_RESULT, JsonResultModelBuilder.aSuccess().value(list).build());

		return "json:json_result";
	}
}
