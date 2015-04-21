package com.naver.fog.field;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.naver.fog.Resource;
import com.naver.fog.frame.Frame;
import com.naver.fog.frame.FrameContext;
import com.naver.fog.frame.field.FrameFieldService;
import com.naver.fog.ui.template.Template;
import com.naver.fog.ui.template.TemplateBuilder;
import com.naver.fog.user.User;
import com.naver.fog.web.AbstractResourceController;
import com.naver.fog.web.AutoCompleteItem;
import com.naver.fog.web.FogHandledException;
import com.naver.fog.web.FogHandledException.HandleType;
import com.naver.fog.web.ViewMode;

@Controller
@RequestMapping("field")
public class FieldController extends AbstractResourceController {
	@Autowired
	private FieldService fieldService;

	@Autowired
	private FieldContext fieldContext;

	@Autowired
	private FrameFieldService frameFieldService;

	@Autowired
	private FrameContext frameContext;

	@Autowired
	private TemplateBuilder templateBuilder;

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		List<Field> fields = fieldService.listLatest(10);
		modelMap.addAttribute("fields", fields);

		return "field/index";
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String get(@PathVariable("id") long id, User user, ModelMap modelMap) {
		setMode(ViewMode.READ, modelMap);
		Field field = getFieldSafely(id, user);
		List<Frame> frames = frameContext.list(frameFieldService.listFrameIdsByField(id, 100));

		modelMap.addAttribute("field", field);
		modelMap.addAttribute("frames", frames);

		return "field/field";
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String add(ModelMap modelMap) {
		setMode(ViewMode.ADD, modelMap);
		modelMap.addAttribute("field", new Field());

		return "field/field";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@ModelAttribute Field field, @RequestParam Map<String, Object> paramMap, User user, ModelMap modelMap) {
		validateField(field, paramMap);
		fieldService.add(field);

		Template template = templateBuilder.addFieldTemplate(field, user, true);

		return "redirect:/template/" + template.getId() + "/edit";
	}

	@RequestMapping(value = "{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable("id") long id, User user, ModelMap modelMap) {
		setMode(ViewMode.EDIT, modelMap);
		Field field = getFieldSafely(id, user);
		modelMap.addAttribute("field", field);

		return "field/field";
	}

	@RequestMapping(value = "modify", method = RequestMethod.POST)
	public String modify(@ModelAttribute Field field, @RequestParam Map<String, Object> paramMap, ModelMap modelMap) {
		validateField(field, paramMap);
		fieldService.modify(field);
		fieldContext.updateCache(field);

		return "redirect:/field/" + field.getId();
	}

	@RequestMapping(value = "_search", method = RequestMethod.GET)
	public String search(@RequestParam("term") String term, ModelMap modelMap) {
		// TODO 검색은 context에서 가져와 모두 cache로 올릴 필요는 없음.
		List<Field> fields = fieldContext.listByNamePrefix(term, 10);
		List<AutoCompleteItem> result = new ArrayList<AutoCompleteItem>(fields.size());
		for (Field field : fields) {
			result.add(new AutoCompleteItem(field));
		}

		modelMap.addAttribute("result", result);

		return "plain:common/json";
	}

	private Field getFieldSafely(long id, User user) {
		Field field = fieldService.getById(id);
		if (field == null) {
			throw new FieldNotFoundException(id);
		}

		if (field.getDefaultTemplate() == null) {
			if (field.getDefaultTemplateId() != Field.NULL_ID) {
				// TODO 예외 처리 추가.
			}

			templateBuilder.addFieldTemplate(field, user, true);
		}

		return field;
	}

	private void validateField(Field field, Map<String, Object> paramMap) {
		validateResource(field);
		if (field.getFieldType() == null) {
			throw new FogHandledException(null, HandleType.ALERT_AND_BACK, "", "Null field type.");
		}

		if (field.getFieldType() == FieldType.FRAME) {
			long frameId = MapUtils.getLong(paramMap, FramedField.PARAM_FRAME_ID, Resource.NULL_ID);
			if (frameContext.get(frameId) == null) {
				throw new FogHandledException(null, HandleType.ALERT_AND_BACK, "", "Frame not found.");
			}
			field.setProperty(FramedField.PARAM_FRAME_ID, frameId);
		}
	}
}
