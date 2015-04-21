package com.naver.fog.frame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.fog.CachedResourceContext;
import com.naver.fog.ResourceType;
import com.naver.fog.field.Field;
import com.naver.fog.field.FieldContext;
import com.naver.fog.frame.field.FrameFieldService;
import com.naver.fog.frame.hierarchy.FrameHierarchyService;
import com.naver.fog.ui.template.TemplateContext;
import com.naver.fog.ui.template.TemplateService;

@Component
public class FrameContext extends CachedResourceContext<Frame> implements InitializingBean {
	public FrameContext() {
		super(ResourceType.FRAME, 3000);
	}

	private static FrameContext INSTANCE;

	public static FrameContext getContext() {
		return INSTANCE;
	}

	@Autowired
	private FrameFieldService frameFieldService;

	@Autowired
	private FrameHierarchyService frameHierarchyService;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private TemplateContext templateContext;

	@Autowired
	public void setFrameService(FrameService frameService) {
		super.resourceService = frameService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		INSTANCE = this;
	}

	public List<Frame> getParents(long frameId) {
		List<Long> parentIds = frameHierarchyService.listParentIds(frameId);
		if (parentIds.isEmpty()) {
			return Collections.emptyList();
		}

		List<Frame> parents = new ArrayList<Frame>(parentIds.size());
		for (long parentId : parentIds) {
			Frame parent = get(parentId);
			if (parent == null) {
				throw new FrameNotFoundException(parentId);
			}

			parents.add(parent);
		}

		return parents;
	}

	public List<Long> getFieldIds(long frameId) {
		return frameFieldService.listByFrame(frameId);
	}

	public List<Field> getFields(long frameId) {
		List<Long> fieldIds = frameFieldService.listByFrame(frameId);
		if (fieldIds.isEmpty()) {
			return Collections.emptyList();
		}

		FieldContext fieldContext = FieldContext.getContext();
		List<Field> fields = new ArrayList<Field>(fieldIds.size());
		for (long fieldId : fieldIds) {
			fields.add(fieldContext.getSafely(fieldId));
		}

		return fields;
	}
}
