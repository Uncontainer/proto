package com.naver.fog.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.naver.fog.AbstractResourceService;

@Service
public class ContentService extends AbstractResourceService<Content> {
	@Autowired
	public void setContentDao(ContentDao contentDao) {
		super.resourceDao = contentDao;
	}

	public Page<Content> searchByFrame(long frameId, Pageable pageable) {
		return ((ContentDao)resourceDao).selectByFrame(frameId, pageable);
	}
}
