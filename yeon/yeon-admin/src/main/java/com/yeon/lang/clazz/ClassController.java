package com.yeon.lang.clazz;

import com.yeon.lang.*;
import com.yeon.lang.impl.MapClass;
import com.yeon.lang.property.MapPropertyDao;
import com.yeon.lang.resource.ResourceHierarchyService;
import com.yeon.lang.value.locale.LocalValueEntity;
import com.yeon.lang.value.locale.LocalValueType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/class")
public class ClassController extends AbstractLangController {
	@Autowired
	private MapClassService classService;

	@Autowired
	private MapPropertyDao propertyDao;

	@Autowired
	private ResourceHierarchyService hierarchyService;

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(ModelMap model) {
		model.addAttribute(Parameters.MODE, Constants.MODE_CREATE);
		model.addAttribute(Parameters.CLASS, new MapClass());
		return "class/class_create";
	}

	@RequestMapping(value = "createSubmit", method = RequestMethod.POST)
	public String createSubmit(ClassCreateForm classCreateForm, BindingResult result, ModelMap model) throws Exception {
		MapClass clazz = classCreateForm.getMapClass();
		new ClassValidator().validate(clazz, result);
		// if (result.hasErrors()) {
		// return "class/resourceForm";
		// }

		String alias = classCreateForm.getAlias();
		if (StringUtils.isNotBlank(alias)) {
			List<LocalValueEntity> existAlias = localeValueEntryService.listByValue(LocalValueType.NAME, ResourceType.CLASS, NamedClassId.LOCALE, alias);
			if (!existAlias.isEmpty()) {
				throw new IllegalArgumentException("Alias already exist.(" + existAlias.get(0) + ")");
			}
		}

		classService.add(clazz);

		super.addDefaultNameAndDescription(ResourceType.CLASS, clazz.getId(), classCreateForm);
		if (StringUtils.isNotBlank(alias)) {
			LocalValueEntity localValue = new LocalValueEntity(ResourceType.CLASS, clazz.getId(), null, NamedClassId.LOCALE, alias);

			localeValueEntryService.add(LocalValueType.NAME, localValue);
		}

		hierarchyService.add(clazz.getId(), classCreateForm.getSuperClassIds());

		model.addAttribute(Parameters.CLASS_ID, clazz.getId());

		return "redirect:read";
	}

	// @RequestMapping("/modify")
	// public String modify(@RequestParam(value = Parameters.RESOURCE_ID, required = true) String resourceId, ModelMap model) {
	// ClassEntity clazz = classRepository.findOne(resourceId);
	//
	// model.addAttribute(Parameters.MODE, Constants.MODE_MODIFY);
	// model.addAttribute(Parameters.RESOURCE, clazz);
	// model.addAttribute(Parameters.PROPERTIES, propertyRepository.findAllByClassId(resourceId));
	// model.addAttribute("jsonResourceNames", CollectionJsonMapper.getInstance().toJson(resourceNameService.getNamesByResourceId(resourceId)));
	//
	// return "class/classForm";
	// }
	//
	// @RequestMapping("/modifySubmit")
	// public String modifySubmit(ClassEntity clazz, BindingResult result,
	// @RequestParam(value = "jsonResourceNames", required = true) String jsonResourceNames, ModelMap model) throws Exception {
	// new ClassValidator().validate(clazz, result);
	// // if (result.hasErrors()) {
	// // return "class/resourceForm";
	// // }
	// List<Map<String, Object>> mapLocalNames = CollectionJsonMapper.getInstance().toList(jsonResourceNames);
	// List<LocalName> localNames = new ArrayList<LocalName>();
	// for (Map<String, Object> mapLocalName : mapLocalNames) {
	// localNames.add(new LocalName((String)mapLocalName.get("name"), (String)mapLocalName.get("locale")));
	// }
	// resourceNameService.modify(clazz.getId(), localNames);
	//
	// classRepository.save(clazz);
	//
	// model.addAttribute("resourceId", clazz.getId());
	//
	// return "redirect:read";
	// }

	// @RequestMapping("/detail.do")
	// public ModelAndView detail(@RequestParam(value = Parameters.RESOURCE_ID,
	// required = true) String resourceId) {
	// Resource resource = resourceBo.get(resourceId);
	// ModelAndView mav = new ModelAndView();
	// mav.addObject(Parameters.MODE, Constants.MODE_READ);
	// mav.setViewName("class/resource");
	// mav.addObject(Parameters.RESOURCE, resource);
	// return mav;
	// }

	@RequestMapping(value = "/{classId}", method = RequestMethod.GET)
	public String get(@PathVariable("classId") String classId, ModelMap model) {
		return read(classId, model);
	}

	@RequestMapping(value = "/read", method = RequestMethod.GET)
	public String read(@RequestParam(value = Parameters.CLASS_ID, required = true) String classId, ModelMap model) {
		MapClass clazz = classService.get(classId);

		model.addAttribute(Parameters.MODE, Constants.MODE_READ);
		model.addAttribute(Parameters.CLASS, clazz);
		model.addAttribute(Parameters.PROPERTIES, propertyDao.selectByClassId(classId));

		return "class/class_detail";
	}

	// @RequestMapping(value = "/list", method = RequestMethod.GET)
	// public String list(@RequestParam(value = Parameters.PAGE, required = false, defaultValue = "0") int page,
	// @RequestParam(value = Parameters.PAGE_SIZE, required = false, defaultValue = "0") int pageSize, ModelMap model) {
	// if (pageSize == 0) {
	// pageSize = Constants.DEFAULT_PAGE_SIZE;
	// }
	//
	// Pageable pageable = new PageRequest(page, pageSize);
	// model.addAttribute(Parameters.CLASSES, classRepository.findAll(pageable));
	// model.addAttribute(Parameters.TOTAL_COUNT, classRepository.count());
	// model.addAttribute(Parameters.PAGE_SIZE, pageSize);
	// model.addAttribute(Parameters.PAGE, page);
	//
	// return "class/class_list";
	// }

	@RequestMapping(value = "/remove")
	public String remove(@RequestParam(value = Parameters.CLASS_ID, required = true) String classId) {
		classService.remove(classId);

		return "redirect:list";
	}
}
