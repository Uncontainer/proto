package com.yeon.lang.resource;

import com.yeon.lang.AbstractLangController;
import com.yeon.lang.Constants;
import com.yeon.lang.Parameters;
import com.yeon.lang.ResourceType;
import com.yeon.lang.impl.MapProperty;
import com.yeon.lang.impl.MapResource;
import com.yeon.lang.property.MapPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/resource")
public class ResourceController extends AbstractLangController {
	@Autowired
	private BasicMapResourceService resourceService;

	@Autowired
	private MapPropertyService propertyService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		return "redirect:/resource/search";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(ModelMap model, @RequestParam(value = Parameters.TYPE_CLASS_ID, required = false) String typeClassId) {
		model.addAttribute(Parameters.MODE, Constants.MODE_CREATE);

		MapResource resource = new MapResource(typeClassId);
		model.addAttribute(Parameters.RESOURCE, resource);

		if (typeClassId != null) {
			List<MapProperty> properties = propertyService.listByClass(typeClassId);
			model.addAttribute(Parameters.PROPERTIES, properties);
		}

		return "resource/resource_create";
	}

	@RequestMapping(value = "createSubmit", method = RequestMethod.POST)
	public String createSubmit(ResourceCreateForm createForm, ModelMap model) throws SQLException {
		MapResource resource = new MapResource(createForm.getTypeClassId());

		resourceService.add(resource);

		super.addDefaultNameAndDescription(ResourceType.RESOURCE, resource.getId(), createForm);

		model.addAttribute("resourceId", resource.getId());

		return "redirect:/resource/" + resource.getId();
	}

	@RequestMapping(value = "{resourceId}", method = RequestMethod.GET)
	public String get(@PathVariable("resourceId") String resourceId, ModelMap model) {
		return read(resourceId, model);
	}

	@RequestMapping(value = "read", method = RequestMethod.GET)
	public String read(@RequestParam(value = Parameters.RESOURCE_ID, required = true) String resourceId, ModelMap model) {
		MapResource resource = resourceService.get(resourceId);
		if (resource == null) {
			return "lang/common/resource_not_found";
		}

		model.addAttribute(Parameters.RESOURCE, resource);

		return "resource/resource_detail";
	}
}
