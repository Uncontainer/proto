package com.naver.fog.frame.hierarchy;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.naver.fog.AbstractTestBase;

public class FrameHierarchyDaoTest extends AbstractTestBase {
	@Autowired
	private FrameHierarchyDao frameHierarchyDao;

	@Test
	public void crudTest() {
		long frameId = Long.MAX_VALUE;
		long parentFrameId = Long.MIN_VALUE;

		frameHierarchyDao.deleteByFrame(frameId);

		FrameHierarchy frameHierarchy = new FrameHierarchy();
		frameHierarchy.setFrameId(frameId);
		frameHierarchy.setParentFrameId(parentFrameId);
		frameHierarchy.setCreatorId(TEST_USER_ID);
		frameHierarchy.setCreatedDate(new Date());

		frameHierarchyDao.insert(frameHierarchy);

		FrameHierarchy dbFrameHierarchy = frameHierarchyDao.select(frameId, parentFrameId);
		Assert.assertNotNull(dbFrameHierarchy);
		Assert.assertEquals(TEST_USER_ID, dbFrameHierarchy.getCreatorId());
		Assert.assertNotNull(dbFrameHierarchy.getCreatedDate());

		List<Long> hierarchyIds = frameHierarchyDao.selectParentIds(frameId);
		Assert.assertEquals(1, hierarchyIds.size());

		int deleteCount = frameHierarchyDao.delete(frameId, frameHierarchy.getParentFrameId());
		Assert.assertEquals(1, deleteCount);
	}
}
