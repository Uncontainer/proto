package com.yeon.lang.property;

import com.yeon.lang.*;
import com.yeon.lang.impl.MapProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

@Controller
@RequestMapping("/property")
public class PropertyController extends AbstractLangController {
	@Autowired
	private MapPropertyService propertyService;

	@RequestMapping("/create")
	public String create(ModelMap model, @RequestParam(value = "domainClassId", required = false) String domainClassId) {
		MapProperty property = new MapProperty();
		property.setDomainClassId(domainClassId);

		model.addAttribute(Parameters.MODE, Constants.MODE_CREATE);
		model.addAttribute(Parameters.PROPERTY, property);
		model.addAttribute("valueOnTimelines", ValueOnTimelineType.values());

		return "property/property_create";
	}

	@RequestMapping("/createSubmit")
	public String createSubmit(PropertyCreateForm propertyCreateForm, BindingResult result, ModelMap model) throws SQLException {
		MapProperty property = propertyCreateForm.getProperty();
		new PropertyValidator().validate(property, result);

		propertyService.add(property);

		super.addDefaultNameAndDescription(ResourceType.PROPERTY, property.getId(), propertyCreateForm);

		model.addAttribute("propertyId", property.getId());

		return "redirect:read";
	}

	// @RequestMapping("/modify")
	// public String modify(@RequestParam(value = Parameters.PROPERTY_ID, required = true) String propertyId, ModelMap model) {
	// model.addAttribute(Parameters.MODE, Constants.MODE_MODIFY);
	// PropertyEntity property = propertyRepository.findOne(propertyId);
	// model.addAttribute(Parameters.PROPERTY, property);
	// String jsonPropertyNames = CollectionJsonMapper.getInstance().toJson(resourceNameService.getNamesByResourceId(propertyId));
	// model.addAttribute("jsonPropertyNames", jsonPropertyNames);
	// model.addAttribute("valueOnTimelines", ValueOnTimelineType.values());
	//
	// return "property/propertyForm";
	// }
	//
	// @RequestMapping("/modifySubmit")
	// public String modifySubmit(PropertyEntity property, @RequestParam(value = "jsonPropertyNames", required = true) String jsonPropertyNames,
	// ModelMap model) {
	// List<Map<String, Object>> mapLocalNames = CollectionJsonMapper.getInstance().toList(jsonPropertyNames);
	// List<LocalName> localNames = new ArrayList<LocalName>();
	// for (Map<String, Object> mapLocalName : mapLocalNames) {
	// localNames.add(new LocalName((String)mapLocalName.get("name"), (String)mapLocalName.get("locale")));
	// }
	// resourceNameService.modify(property.getId(), localNames);
	//
	// propertyRepository.save(property);
	//
	// model.addAttribute("propertyId", property.getId());
	//
	// return "redirect:read";
	// }

	@RequestMapping("/read")
	public String read(@RequestParam(value = Parameters.PROPERTY_ID, required = true) String propertyId, ModelMap model) {
		model.addAttribute(Parameters.MODE, Constants.MODE_READ);
		MapProperty property = propertyService.get(propertyId);
		model.addAttribute(Parameters.PROPERTY, property);
		model.addAttribute("valueOnTimelines", ValueOnTimelineType.values());

		return "property/property_detail";
	}

	@RequestMapping("/remove")
	public String remove(@RequestParam(value = Parameters.PROPERTY_ID, required = true) String propertyId, ModelMap model) {
		propertyService.remove(propertyId);

		// TODO class 페이지로 이동하거나 Ajax로 처리.
		model.addAttribute("propertyId", propertyId);
		return "redirect:read";
	}
}
