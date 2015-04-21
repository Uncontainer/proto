package com.naver.fog.frame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.fog.AbstractResourceService;

@Service
public class FrameService extends AbstractResourceService<Frame> {
	@Autowired
	public void setFrameDao(FrameDao frameDao) {
		this.resourceDao = frameDao;
	}
}
