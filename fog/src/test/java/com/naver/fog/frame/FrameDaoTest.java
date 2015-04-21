package com.naver.fog.frame;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.naver.fog.AbstractTestBase;
import com.naver.fog.Resource;

public class FrameDaoTest extends AbstractTestBase {
	@Autowired
	private FrameDao frameDao;

	@Test
	public void crudTest() {
		Frame frame = new Frame();
		frame.setName("test");
		frame.setDescription("unit test");
		frame.setCreatorId(Long.MAX_VALUE);

		// insert
		frameDao.insert(frame);
		Assert.assertNotSame(Resource.NULL_ID, frame.getId());

		// select
		Frame dbFrame = frameDao.selectById(frame.getId());
		Assert.assertEquals("test", dbFrame.getName());
		Assert.assertEquals("unit test", dbFrame.getDescription());
		Assert.assertEquals(Long.MAX_VALUE, dbFrame.getCreatorId());
		Assert.assertNotNull(dbFrame.getCreatedDate());

		List<Frame> frames = frameDao.selectLatest(10);
		Assert.assertTrue(frames.size() >= 1);
		Assert.assertEquals("test", frames.get(0).getName());

		List<Long> frameIds = frameDao.selectIdsByNamePrefix("t", 1);
		Assert.assertEquals(1, frameIds.size());

		// update
		dbFrame.setName("test2");
		dbFrame.setDescription("unit test2");
		dbFrame.setCreatorId(Long.MIN_VALUE);

		int updateCount = frameDao.update(dbFrame);
		Assert.assertEquals(1, updateCount);

		dbFrame = frameDao.selectById(dbFrame.getId());
		Assert.assertEquals("test2", dbFrame.getName());
		Assert.assertEquals("unit test2", dbFrame.getDescription());
		Assert.assertEquals(Long.MAX_VALUE, dbFrame.getCreatorId());
		Assert.assertNotNull(dbFrame.getCreatedDate());

		// delete
		int deleteCount = frameDao.deleteById(frame.getId());
		Assert.assertEquals(1, deleteCount);
		Assert.assertNull(frameDao.selectById(frame.getId()));
	}
}
