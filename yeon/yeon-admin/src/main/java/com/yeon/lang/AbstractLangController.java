package com.yeon.lang;

import com.yeon.lang.resource.ResourceCreateForm;
import com.yeon.lang.value.locale.LocalValueEntity;
import com.yeon.lang.value.locale.LocalValueService;
import com.yeon.lang.value.locale.LocalValueType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractLangController {
	@Autowired
	protected LocalValueService localeValueEntryService;

	protected void addDefaultNameAndDescription(ResourceType resourceType, String resourceId, ResourceCreateForm createForm) {
		if (StringUtils.isNotBlank(createForm.getName())) {
			// TODO 기본 locale을 받거나 아예 넣지 않는 방식으로 처리 변경.
			if (StringUtils.isBlank(createForm.getLocale())) {
				throw new IllegalArgumentException("Empty locale");
			}

			localeValueEntryService.add(LocalValueType.NAME, new LocalValueEntity(resourceType, resourceId, null, createForm.getLocale(), createForm.getName()));
		}

		if (StringUtils.isNotBlank(createForm.getDescription())) {
			// TODO 기본 locale을 받거나 아예 넣지 않는 방식으로 처리 변경.
			if (StringUtils.isBlank(createForm.getLocale())) {
				throw new IllegalArgumentException("Empty locale");
			}

			localeValueEntryService.add(LocalValueType.DESCRIPTION, new LocalValueEntity(resourceType, resourceId, null, createForm.getLocale(), createForm.getDescription()));
		}
	}
}
