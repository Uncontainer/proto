package com.naver.fog.frame.hierarchy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FrameHierarchyService {
	@Autowired
	private FrameHierarchyDao frameHierarchyDao;

	public List<Long> listParentIds(long frameId) {
		return frameHierarchyDao.selectParentIds(frameId);
	}

	public void add(FrameHierarchy frameHierarchy) {
		if (frameHierarchy.getFrameId() == frameHierarchy.getParentFrameId()) {
			throw new IllegalArgumentException("Frame hierarchy does not support cycle.");
		}

		frameHierarchyDao.insert(frameHierarchy);
	}

	public int remove(long frameId, long parentFrameId) {
		return frameHierarchyDao.delete(frameId, parentFrameId);
	}
}
