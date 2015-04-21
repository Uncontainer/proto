package com.naver.fog.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;

import com.naver.fog.Resource;
import com.naver.fog.web.FogHandledException.HandleType;

public class AbstractResourceController {
	protected static String PARAM_MODE = "mode";

	protected void validateResource(Resource resource) {
		if (StringUtils.isBlank(resource.getName())) {
			throw new FogHandledException(null, HandleType.ALERT_AND_BACK, "", "Empty resource name.");
		}

		if (StringUtils.isBlank(resource.getDescription())) {
			throw new FogHandledException(null, HandleType.ALERT_AND_BACK, "", "Empty resource description.");
		}
	}

	protected void setMode(ViewMode mode, ModelMap modelMap) {
		modelMap.addAttribute(PARAM_MODE, mode);
	}
}
