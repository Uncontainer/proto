package com.yeon.lang.value.locale;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yeon.lang.JsonResultModel;
import com.yeon.lang.JsonResultModelBuilder;
import com.yeon.lang.Resource;
import com.yeon.lang.clazz.MapClassService;
import com.yeon.lang.property.MapPropertyService;
import com.yeon.lang.resource.BasicMapResourceService;

@Controller
@RequestMapping("/lang/value/locale")
public class LocalValueController {
	@Autowired
	private BasicMapResourceService resourceService;

	@Autowired
	private MapClassService classService;

	@Autowired
	private MapPropertyService propertyService;

	@Autowired
	private LocalValueService localeValueEntityService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return "lang/value/locale/locale_value_index";
	}

	// @RequestMapping("{valueType}/{resourceId}")
	// public String resource(@PathVariable("valueType") String valueTypeString, @PathVariable("resourceId") String resourceId, ModelMap model) {
	// ResourceEntity resource = resourceService.get(resourceId);
	// if (resource == null) {
	// return "lang/common/resource_not_found";
	// }
	//
	// List<? extends LocaleValue> entries = getService(valueTypeString).listByResourceId(resourceId);
	//
	// model.addAttribute("resourceId", resourceId);
	// model.addAttribute("entries", entries);
	//
	// return "lang/value/locale/locale_value_list";
	// }

	@RequestMapping("{valueType}/{resourceId}")
	public String list(@PathVariable("valueType") String valueTypeString, @PathVariable("resourceId") String resourceId, ModelMap model) {
		LocalValueType type = toType(valueTypeString);
		JsonResultModel jsonResultModel;
		Resource resource = getResource(resourceId);
		if (resource == null) {
			jsonResultModel = JsonResultModelBuilder.aFail().message("해당 리소스를 찾을 수 없습니다.(" + resourceId + ")").build();
		} else {
			List<LocalValueEntity> localValues = localeValueEntityService.listByResourceId(type, resourceId);
			jsonResultModel = JsonResultModelBuilder.aSuccess().value(localValues).build();
		}

		model.addAttribute(JsonResultModel.PARAM_RESULT, jsonResultModel);

		return "json:json_result";
	}

	private Resource getResource(String resourceId) {
		switch (resourceId.charAt(0)) {
		case 'R':
			return resourceService.get(resourceId);
		case 'C':
			return classService.get(resourceId);
		case 'P':
			return propertyService.get(resourceId);
		default:
			throw new IllegalArgumentException("Invalid resourceId format.(" + resourceId + ")");
		}
	}

	@RequestMapping("{valueType}/create")
	public String add(@PathVariable("valueType") String valueTypeString, LocalValueEntity entry, ModelMap model) {
		LocalValueType type = toType(valueTypeString);
		LocalValueEntity localValue = localeValueEntityService.add(type, entry);

		model.addAttribute(JsonResultModel.PARAM_RESULT, JsonResultModelBuilder.aSuccess().value(localValue).build());

		return "json:json_result";
	}

	@RequestMapping("{valueType}/modify")
	public String modify(@PathVariable("valueType") String valueTypeString, LocalValueEntity entry, ModelMap model) {
		LocalValueType type = toType(valueTypeString);
		localeValueEntityService.modify(type, entry);

		return "json:json_result_success";
	}

	@RequestMapping("{valueType}/remove")
	public String remove(@PathVariable("valueType") String valueTypeString, @RequestParam("id") long id, ModelMap model) {
		LocalValueType type = toType(valueTypeString);
		localeValueEntityService.remove(type, id);

		return "json:json_result_success";
	}

	private LocalValueType toType(String valueTypeString) {
		return LocalValueType.valueOf(valueTypeString.toUpperCase());
	}
}
