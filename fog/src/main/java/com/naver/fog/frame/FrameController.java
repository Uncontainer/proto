package com.naver.fog.frame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.naver.fog.content.Content;
import com.naver.fog.content.ContentService;
import com.naver.fog.field.Field;
import com.naver.fog.field.FieldContext;
import com.naver.fog.frame.field.FrameField;
import com.naver.fog.frame.field.FrameFieldService;
import com.naver.fog.frame.hierarchy.FrameHierarchy;
import com.naver.fog.frame.hierarchy.FrameHierarchyService;
import com.naver.fog.ui.template.Template;
import com.naver.fog.ui.template.TemplateBuilder;
import com.naver.fog.user.User;
import com.naver.fog.web.AbstractResourceController;
import com.naver.fog.web.AutoCompleteItem;
import com.naver.fog.web.FogHandledException;
import com.naver.fog.web.FogHandledException.HandleType;
import com.naver.fog.web.ViewMode;

@Controller
@RequestMapping("frame")
public class FrameController extends AbstractResourceController {
	@Autowired
	private FrameService frameService;

	@Autowired
	private FrameHierarchyService frameHierarchyService;

	@Autowired
	private FrameFieldService frameFieldService;

	@Autowired
	private FrameContext frameContext;

	@Autowired
	private FieldContext fieldContext;

	@Autowired
	private ContentService contentService;

	@Autowired
	private TemplateBuilder templateBuilder;

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		List<Frame> frames = frameService.listLatest(10);
		modelMap.addAttribute("frames", frames);

		return "frame/index";
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String get(@PathVariable("id") long id, User user, ModelMap modelMap) {
		setMode(ViewMode.READ, modelMap);
		Frame frame = getFrameSafely(id, user);
		modelMap.addAttribute("frame", frame);

		Page<Content> contents = contentService.searchByFrame(id, new PageRequest(0, 30));
		modelMap.addAttribute("contents", contents);

		return "frame/frame";
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String add(ModelMap modelMap) {
		modelMap.addAttribute("frame", new Frame());
		setMode(ViewMode.ADD, modelMap);

		return "frame/frame";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@ModelAttribute Frame frame
		, @RequestParam(value = "parentFrameIds", required = false) List<Long> parentFrameIds
		, @RequestParam(value = "fieldIds", required = false) List<Long> fieldIds
		, User user
		, ModelMap modelMap) {
		validateFrame(frame, parentFrameIds, fieldIds);
		frameService.add(frame);
		if (parentFrameIds != null) {
			for (long parentFrameId : parentFrameIds) {
				FrameHierarchy frameHierarchy = new FrameHierarchy(frame.getId(), parentFrameId);
				frameHierarchyService.add(frameHierarchy);
			}
		}

		if (fieldIds != null) {
			for (long fieldId : fieldIds) {
				FrameField frameField = new FrameField(frame.getId(), fieldId);
				frameFieldService.add(frameField);
			}
		}

		Template template = templateBuilder.addFrameTemplate(frame, user, true);

		return "redirect:/template/" + template.getId() + "/edit";
	}

	@RequestMapping(value = "{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable("id") long id, User user, ModelMap modelMap) {
		setMode(ViewMode.EDIT, modelMap);
		Frame frame = getFrameSafely(id, user);
		modelMap.addAttribute("frame", frame);

		return "frame/frame";
	}

	@RequestMapping(value = "modify", method = RequestMethod.POST)
	public String modify(@ModelAttribute Frame frame
		, @RequestParam(value = "parentFrameIds", required = false) List<Long> parentFrameIds
		, @RequestParam(value = "fieldIds", required = false) List<Long> fieldIds
		, ModelMap modelMap) {
		validateFrame(frame, parentFrameIds, fieldIds);

		new FrameUpdater(frame, parentFrameIds, fieldIds).update();

		return "redirect:/frame/" + frame.getId();
	}

	private class FrameUpdater {
		Frame oldFrame;
		Frame frame;
		Set<Long> parentFrameIdSet;
		Set<Long> fieldIdSet;

		public FrameUpdater(Frame frame, List<Long> parentFrameIds, List<Long> fieldIds) {
			super();
			this.frame = frame;
			this.parentFrameIdSet = parentFrameIds != null ? new HashSet<Long>(parentFrameIds) : Collections.<Long> emptySet();
			this.fieldIdSet = fieldIds != null ? new HashSet<Long>(fieldIds) : Collections.<Long> emptySet();
			this.oldFrame = frameContext.get(frame.getId());
		}

		void update() {
			updateFrame();
			updateHierarchy();
			updateField();
			frameContext.updateCache(frame);
		}

		private void updateFrame() {
			frameService.modify(frame);
		}

		private void updateField() {
			List<Field> oldFields = oldFrame.getFields();
			for (Field oldField : oldFields) {
				if (fieldIdSet.remove(oldField.getId())) {
					continue;
				}

				frameFieldService.remove(frame.getId(), oldField.getId());
			}

			for (long fieldId : fieldIdSet) {
				FrameField frameField = new FrameField(frame.getId(), fieldId);
				frameFieldService.add(frameField);
			}
		}

		private void updateHierarchy() {
			List<Frame> oldParents = oldFrame.getParents();
			for (Frame oldParent : oldParents) {
				if (parentFrameIdSet.remove(oldParent.getId())) {
					continue;
				}

				frameHierarchyService.remove(frame.getId(), oldParent.getId());
			}

			for (long parentFrameId : parentFrameIdSet) {
				FrameHierarchy frameHierarchy = new FrameHierarchy(frame.getId(), parentFrameId);
				frameHierarchyService.add(frameHierarchy);
			}
		}
	}

	@RequestMapping(value = "_search", method = RequestMethod.GET)
	public String search(@RequestParam("term") String term, ModelMap modelMap) {
		// TODO 검색은 context에서 가져와 모두 cache로 올릴 필요는 없음.
		List<Frame> frames = frameContext.listByNamePrefix(term, 10);
		List<AutoCompleteItem> result = new ArrayList<AutoCompleteItem>(frames.size());
		for (Frame frame : frames) {
			result.add(new AutoCompleteItem(frame));
		}

		modelMap.addAttribute("result", result);

		return "plain:common/json";
	}

	private Frame getFrameSafely(long id, User user) {
		Frame frame = frameService.getById(id);
		if (frame == null) {
			throw new FrameNotFoundException(id);
		}

		if (frame.getDefaultTemplate() == null) {
			if (frame.getDefaultTemplateId() != Frame.NULL_ID) {
				// TODO 예외 처리 추가.
			}

			templateBuilder.addFrameTemplate(frame, user, true);
		}

		return frame;
	}

	private void validateFrame(Frame frame, List<Long> parentFrameIds, List<Long> fieldIds) {
		new FrameValidator(frame, parentFrameIds, fieldIds).validate();
	}

	private class FrameValidator {
		Frame frame;
		List<Long> parentFrameIds;
		List<Long> fieldIds;

		public FrameValidator(Frame frame, List<Long> parentFrameIds, List<Long> fieldIds) {
			super();
			this.frame = frame;
			this.parentFrameIds = parentFrameIds;
			this.fieldIds = fieldIds;
		}

		void validate() {
			validateResource(frame);
			validateParentFrame();
			validateField();
		}

		private void validateParentFrame() {
			if (parentFrameIds == null) {
				return;
			}

			HashSet<Long> frameIdSet = new HashSet<Long>();
			frameIdSet.add(frame.getId());
			for (Frame parentFrame : frameContext.list(parentFrameIds)) {
				checkCycle(frameIdSet, parentFrame);
			}
		}

		private void checkCycle(Set<Long> frameIdSet, Frame frame) {
			if (frameIdSet.contains(frame.getId())) {
				throw new FogHandledException(null, HandleType.ALERT_AND_BACK, "", "Frame has a duplicated parent or cycle.(" + frame.getId() + ":" + frame.getName() + ")");
			}

			frameIdSet.add(frame.getId());
			for (Frame parentFrame : frame.getParents()) {
				checkCycle(frameIdSet, parentFrame);
			}
		}

		private void validateField() {
			if (fieldIds == null) {
				return;
			}

			Set<Long> fieldIdSet = new HashSet<Long>();
			for (Field field : fieldContext.list(fieldIds)) {
				if (fieldIdSet.contains(field.getId())) {
					throw new FogHandledException(null, HandleType.ALERT_AND_BACK, "", "Frame has a duplicated field.(" + field.getId() + ":" + field.getName() + ")");
				}

				fieldIdSet.add(field.getId());
			}

			if (frame.hasParent()) {
				Map<Long, Integer> counter = new HashMap<Long, Integer>();
				for (Frame parent : frame.getParents()) {
					checkAmbiguousField(parent, counter);
				}

				for (Entry<Long, Integer> each : counter.entrySet()) {
					if (each.getValue() == 1) {
						continue;
					}

					if (fieldIdSet.contains(each.getKey())) {
						continue;
					}

					Field ambiguousField = fieldContext.get(each.getKey());
					throw new FogHandledException(null, HandleType.ALERT_AND_BACK, "", "Frame has a ambiguous field.(" + ambiguousField.getId() + ":" + ambiguousField.getName() + ")");
				}
			}
		}

		private void checkAmbiguousField(Frame frame, Map<Long, Integer> counter) {
			if (frame.hasParent()) {
				for (Frame parent : frame.getParents()) {
					checkAmbiguousField(parent, counter);
					for (Field field : frame.getFields()) {
						counter.put(field.getId(), 1);
					}
				}
			} else {
				for (Field field : frame.getFields()) {
					Integer count = counter.get(field.getId());
					count = (count != null) ? count + 1 : 1;
					counter.put(field.getId(), count);
				}
			}
		}
	}
}
