package com.naver.fog.ui.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.fog.AbstractResourceService;

@Service
public class TemplateService extends AbstractResourceService<Template> {
	@Autowired
	public void setTemplateDao(TemplateDao templateDao) {
		this.resourceDao = templateDao;
	}
}
