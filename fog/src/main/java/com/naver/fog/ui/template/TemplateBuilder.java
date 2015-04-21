package com.naver.fog.ui.template;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.fog.Resource;
import com.naver.fog.field.Field;
import com.naver.fog.field.FieldContext;
import com.naver.fog.field.FieldService;
import com.naver.fog.field.FramedField;
import com.naver.fog.frame.Frame;
import com.naver.fog.frame.FrameContext;
import com.naver.fog.frame.FrameService;
import com.naver.fog.ui.layout.Layout;
import com.naver.fog.ui.layout.LayoutFactory;
import com.naver.fog.ui.layout.component.BooleanLayout;
import com.naver.fog.ui.layout.component.DateLayout;
import com.naver.fog.ui.layout.component.NumberLayout;
import com.naver.fog.ui.layout.component.StringLayout;
import com.naver.fog.ui.layout.composite.LabeledLinearLayout;
import com.naver.fog.user.User;

@Component
public class TemplateBuilder {
	@Autowired
	private TemplateService templateService;

	@Autowired
	private TemplateContext templateContext;

	@Autowired
	private FrameService frameService;

	@Autowired
	private FrameContext frameContext;

	@Autowired
	private FieldService fieldService;

	@Autowired
	private FieldContext fieldContext;

	public TemplateBuilder() {
	}

	public Template addFieldTemplate(Field field, User user, boolean updateField) {
		Template template = buildFieldTemplate(field, user);
		templateService.add(template);
		templateContext.updateCache(template);

		if (updateField) {
			field.setDefaultTemplateId(template.getId());
			fieldService.modify(field);
			fieldContext.updateCache(field);
		}

		return template;
	}

	private Template buildFieldTemplate(Field field, User user) {
		Layout viewLayout;
		Layout editLayout;
		switch (field.getFieldType()) {
			case BOOLEAN:
				viewLayout = editLayout = new BooleanLayout(field.getId());
				break;
			case DATETIME:
				viewLayout = editLayout = new DateLayout(field.getId());
				break;
			case FLOAT:
				viewLayout = editLayout = new NumberLayout(field.getId());
				break;
			case FRAME:
				Frame frame = FrameContext.getContext().getSafely(((FramedField)field).getFrameId());
				Template frameDefaultTemplate = frame.getDefaultTemplate();
				if (frameDefaultTemplate == null) {
					if (frame.getDefaultTemplateId() != Resource.NULL_ID) {
						// TODO 예외 처리 추가.
					}
					frameDefaultTemplate = addFrameTemplate(frame, user, true);
				}

				viewLayout = frameDefaultTemplate.getViewLayout();
				editLayout = frameDefaultTemplate.getEditLayout();
				break;
			case INTEGER:
				viewLayout = editLayout = new NumberLayout(field.getId());
				break;
			case STRING:
				viewLayout = editLayout = new StringLayout(field.getId());
				break;
			default:
				throw new IllegalArgumentException("Unsupported field type: " + field.getFieldType());
		}

		Template template = new Template();
		template.setTargetResourceId(field.getId());
		template.setName(field.getName() + "Layout");
		template.setDescription(field.getName() + "'s default layout");
		template.setViewLayout(viewLayout);
		template.setEditLayout(editLayout);

		return template;
	}

	public Template addFrameTemplate(Frame frame, User user, boolean updateFrame) {
		Template template = buildFrameTemplate(frame, user);
		templateService.add(template);
		templateContext.updateCache(template);

		if (updateFrame) {
			frame.setDefaultTemplateId(template.getId());
			frameService.modify(frame);
			frameContext.updateCache(frame);
		}

		return template;
	}

	private Template buildFrameTemplate(Frame frame, User user) {
		List<Frame> parents = frame.getParents();
		Template defaultTemplate = null;
		if (parents.size() == 1) {
			Frame parent = parents.get(0);
			defaultTemplate = parent.getDefaultTemplate();
		}

		Layout viewLayout;
		Layout editLayout;
		List<Field> fields;
		if (defaultTemplate != null) {
			fields = frame.getFields();
			viewLayout = LayoutFactory.clone(defaultTemplate.getViewLayout(), frame.getId());
			editLayout = LayoutFactory.clone(defaultTemplate.getEditLayout(), frame.getId());
		} else {
			fields = frame.getFieldsAll();
			viewLayout = new LabeledLinearLayout(frame.getId());
			editLayout = new LabeledLinearLayout(frame.getId());
		}

		// TODO 기본 layout이 아닌 특화된 layout을 가져올 수 있을지 검토.
		for (Field field : fields) {
			Template fieldDefaultTemplate = field.getDefaultTemplate();
			if (fieldDefaultTemplate == null) {
				if (field.getDefaultTemplateId() != Resource.NULL_ID) {
					// TODO 예외 처리 추가.
				}
				fieldDefaultTemplate = addFieldTemplate(field, user, true);
			}

			viewLayout.add(fieldDefaultTemplate.getViewLayout());
			editLayout.add(fieldDefaultTemplate.getEditLayout());
		}

		Template template = new Template();
		template.setTargetResourceId(frame.getId());
		template.setName(frame.getName() + "Layout");
		template.setDescription(frame.getName() + "'s default layout");
		template.setViewLayout(viewLayout);
		template.setEditLayout(editLayout);

		return template;
	}
}
