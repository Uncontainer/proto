package com.naver.fog.frame.field;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.naver.fog.AbstractTestBase;

public class FrameFieldDaoTest extends AbstractTestBase {
	@Autowired
	private FrameFieldDao frameFieldDao;

	@Test
	public void crudTest() {
		long frameId = Long.MAX_VALUE;
		long fieldId = Long.MIN_VALUE;

		frameFieldDao.deleteByFrame(frameId);

		FrameField frameField = new FrameField();
		frameField.setFrameId(frameId);
		frameField.setFieldId(fieldId);
		frameField.setCreatorId(TEST_USER_ID);
		frameField.setCreatedDate(new Date());

		frameFieldDao.insert(frameField);

		FrameField dbFrameField = frameFieldDao.select(frameId, fieldId);
		Assert.assertNotNull(dbFrameField);
		Assert.assertEquals(TEST_USER_ID, dbFrameField.getCreatorId());
		Assert.assertNotNull(dbFrameField.getCreatedDate());

		List<FrameField> fields = frameFieldDao.selectByFrame(frameId);
		Assert.assertEquals(1, fields.size());

		List<Long> fieldIds = frameFieldDao.selectIdsByFrame(frameId);
		Assert.assertEquals(1, fieldIds.size());

		List<Long> frameIds = frameFieldDao.selectFrameIdsByFieldId(fieldId, 1);
		Assert.assertEquals(1, frameIds.size());

		int deleteCount = frameFieldDao.delete(frameId, frameField.getFieldId());
		Assert.assertEquals(1, deleteCount);
	}
}
