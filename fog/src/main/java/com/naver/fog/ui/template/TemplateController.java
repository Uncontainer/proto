package com.naver.fog.ui.template;

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

import com.naver.fog.Resource;
import com.naver.fog.ui.layout.AbstractLayout;
import com.naver.fog.ui.layout.HtmlBuilder;
import com.naver.fog.ui.layout.Layout;
import com.naver.fog.user.User;
import com.naver.fog.web.AbstractResourceController;
import com.naver.fog.web.AutoCompleteItem;
import com.naver.fog.web.FogHandledException;
import com.naver.fog.web.FogHandledException.HandleType;
import com.naver.fog.web.ViewMode;

@Controller
@RequestMapping("template")
public class TemplateController extends AbstractResourceController {
	@Autowired
	private TemplateService templateService;

	@Autowired
	private TemplateContext templateContext;

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		List<Template> templates = templateService.listLatest(10);
		modelMap.addAttribute("templates", templates);

		return "template/index";
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String get(@PathVariable("id") long id, ModelMap modelMap) {
		return showTemplate(ViewMode.READ, id, modelMap);
	}

	private String showTemplate(ViewMode viewMode, long id, ModelMap modelMap) {
		setMode(viewMode, modelMap);
		Template template = getTemplateSafely(id);

		modelMap.addAttribute("template", template);
		HtmlBuilder viewLayoutHtml = AbstractLayout.toLayoutHtml(template.getViewLayout());
		modelMap.addAttribute("viewLayoutHtml", viewLayoutHtml.getMarkup());
		modelMap.addAttribute("viewLayoutScript", viewLayoutHtml.getScript());
		HtmlBuilder editLayoutHtml = AbstractLayout.toLayoutHtml(template.getEditLayout());
		modelMap.addAttribute("editLayoutHtml", editLayoutHtml.getMarkup());
		modelMap.addAttribute("editLayoutScript", editLayoutHtml.getScript());
		modelMap.addAttribute("deviceWidth", 640);

		return "template/template";
	}

	@RequestMapping(value = "{id}/add", method = RequestMethod.GET)
	public String add(@PathVariable("id") long id, ModelMap modelMap) {
		setMode(ViewMode.ADD, modelMap);
		Template template = getTemplateSafely(id);
		template.setId(Resource.NULL_ID);
		modelMap.addAttribute("template", template);

		return "template/template";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@ModelAttribute Template template
		, @RequestParam(value = "viewLayoutHtml", required = false) String viewLayoutHtml
		, @RequestParam(value = "editLayoutHtml", required = false) String editLayoutHtml
		, User user
		, ModelMap modelMap) {
		validateTemplate(template, viewLayoutHtml, editLayoutHtml);
		templateService.add(template);

		return "redirect:/template/" + template.getId();
	}

	@RequestMapping(value = "{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable("id") long id, ModelMap modelMap) {
		return showTemplate(ViewMode.EDIT, id, modelMap);
	}

	@RequestMapping(value = "modify", method = RequestMethod.POST)
	public String modify(@ModelAttribute Template template
		, @RequestParam(value = "viewLayoutHtml", required = false) String viewLayoutHtml
		, @RequestParam(value = "editLayoutHtml", required = false) String editLayoutHtml
		, User user
		, ModelMap modelMap) {
		validateTemplate(template, viewLayoutHtml, editLayoutHtml);
		templateService.modify(template);
		templateContext.updateCache(template);

		return "redirect:/template/" + template.getId();
	}

	@RequestMapping(value = "_search", method = RequestMethod.GET)
	public String search(@RequestParam("term") String term, ModelMap modelMap) {
		// TODO 검색은 context에서 가져와 모두 cache로 올릴 필요는 없음.
		List<Template> templates = templateContext.listByNamePrefix(term, 10);
		List<AutoCompleteItem> result = new ArrayList<AutoCompleteItem>(templates.size());
		for (Template template : templates) {
			result.add(new AutoCompleteItem(template));
		}

		modelMap.addAttribute("result", result);

		return "plain:common/json";
	}

	private Template getTemplateSafely(long id) {
		Template template = templateService.getById(id);
		if (template == null) {
			throw new TemplateNotFoundException(id);
		}
		return template;
	}

	private void validateTemplate(Template template, String viewLayoutHtml, String editLayoutHtml) {
		Layout viewLayout = AbstractLayout.fromLayoutHtml(viewLayoutHtml);
		Layout editLayout = AbstractLayout.fromLayoutHtml(editLayoutHtml);

		template.setViewLayout(viewLayout);
		template.setEditLayout(editLayout);

		if (template.getTargetResource() == null) {
			throw new FogHandledException(null, HandleType.ALERT_AND_BACK, "", "Null target resource.");
		}

		validateResource(template);
		//		if (template.getType() == null) {
		//			throw new FogHandledException(null, HandleType.ALERT_AND_BACK, "", "Null template type.");
		//		}
	}
}
