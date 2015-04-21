package com.naver.fog.content;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.naver.fog.field.Field;
import com.naver.fog.field.FieldContext;
import com.naver.fog.frame.FrameContext;
import com.naver.fog.frame.FrameNotFoundException;
import com.naver.fog.ui.renderer.RenderContext;
import com.naver.fog.ui.template.TemplateContext;
import com.naver.fog.web.AbstractResourceController;
import com.naver.fog.web.ViewMode;

@Controller
@RequestMapping("content")
public class ContentController extends AbstractResourceController {
	@Autowired
	private ContentService contentService;

	@Autowired
	private FrameContext frameContext;

	@Autowired
	private FieldContext fieldContext;

	@Autowired
	private TemplateContext templateContext;

	@Autowired
	private RenderContext renderContext;

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		List<Content> contents = contentService.listLatest(10);
		modelMap.addAttribute("contents", contents);

		return "content/index";
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String get(@PathVariable("id") long id, ModelMap modelMap) {
		setMode(ViewMode.READ, modelMap);
		Content content = getContentSafely(id);
		modelMap.addAttribute("content", content);

		String html = renderContext.render(content, ViewMode.READ);
		modelMap.addAttribute("html", html);

		return "content/content";
	}

	@RequestMapping(value = "add/{frameId}", method = RequestMethod.GET)
	public String add(ModelMap modelMap, @PathVariable("frameId") long frameId) {
		if (frameContext.get(frameId) == null) {
			throw new FrameNotFoundException(frameId);
		}

		setMode(ViewMode.ADD, modelMap);
		modelMap.addAttribute("content", new Content(frameId));

		String html = renderContext.render(new Content(frameId), ViewMode.ADD);
		modelMap.addAttribute("html", html);

		return "content/content";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@ModelAttribute Content content, @RequestParam Map<String, Object> paramMap, ModelMap modelMap) {
		Map<Long, Object> fieldValueMap = extractFieldValue(paramMap);
		validateContent(content);
		String value = ContentValueEncoder.encode(content.getFrame(), fieldValueMap);
		content.setValue(value);
		contentService.add(content);

		return "redirect:/content/" + content.getId();
	}

	@RequestMapping(value = "{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable("id") long id, ModelMap modelMap) {
		setMode(ViewMode.EDIT, modelMap);
		Content content = getContentSafely(id);
		modelMap.addAttribute("content", content);

		String html = renderContext.render(content, ViewMode.EDIT);
		modelMap.addAttribute("html", html);

		return "content/content";
	}

	@RequestMapping(value = "modify", method = RequestMethod.POST)
	public String modify(@ModelAttribute Content content, @RequestParam Map<String, Object> paramMap, ModelMap modelMap) {
		Map<Long, Object> fieldValueMap = extractFieldValue(paramMap);
		validateContent(content);
		String value = ContentValueEncoder.encode(content.getFrame(), fieldValueMap);
		content.setValue(value);
		contentService.modify(content);

		return "redirect:/content/" + content.getId();
	}

	private Map<Long, Object> extractFieldValue(Map<String, Object> paramMap) {
		Map<Long, Object> fieldValueMap = new HashMap<Long, Object>();
		for (Entry<String, Object> each : paramMap.entrySet()) {
			if (!NumberUtils.isDigits(each.getKey())) {
				continue;
			}

			long fieldId = Long.parseLong(each.getKey());
			Field field = fieldContext.getSafely(fieldId);
			// TODO 배열일 경우에 대한 처리 추가.
			fieldValueMap.put(fieldId, field.getFieldType().decode((String)each.getValue()));
		}

		return fieldValueMap;
	}

	private Content getContentSafely(long id) {
		Content content = contentService.getById(id);
		if (content == null) {
			throw new ContentNotFoundException(id);
		}

		return content;
	}

	private void validateContent(Content content) {
		super.validateResource(content);
	}
}
