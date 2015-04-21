package com.naver.fog.content;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.naver.fog.AbstractTestBase;
import com.naver.fog.Resource;

public class ContentDaoTest extends AbstractTestBase {
	@Autowired
	private ContentDao contentDao;

	@Test
	public void crudTest() {
		Content content = new Content();
		content.setFrameId(Long.MAX_VALUE);
		content.setValue("test value");
		content.setName("test");
		content.setDescription("unit test");
		content.setCreatorId(Long.MAX_VALUE);

		// insert
		contentDao.insert(content);
		Assert.assertNotSame(Resource.NULL_ID, content.getId());

		// select
		Content dbContent = contentDao.selectById(content.getId());
		Assert.assertEquals("test", dbContent.getName());
		Assert.assertEquals(Long.MAX_VALUE, dbContent.getFrameId());
		Assert.assertEquals("test value", dbContent.getValue());
		Assert.assertEquals("unit test", dbContent.getDescription());
		Assert.assertEquals(Long.MAX_VALUE, dbContent.getCreatorId());
		Assert.assertNotNull(dbContent.getCreatedDate());
		final Date createdDate = dbContent.getCreatedDate();

		List<Content> fields = contentDao.selectLatest(10);
		Assert.assertTrue(fields.size() >= 1);
		Assert.assertEquals("test", fields.get(0).getName());

		fields = contentDao.selectByIds(Arrays.asList(dbContent.getId()));
		Assert.assertEquals(1, fields.size());
		Assert.assertEquals("test", fields.get(0).getName());

		// update
		dbContent.setName("test2");
		dbContent.setFrameId(Long.MIN_VALUE);
		dbContent.setValue("test value2");
		dbContent.setDescription("unit test2");
		dbContent.setCreatorId(Long.MIN_VALUE);
		dbContent.setCreatedDate(new Date(createdDate.getTime() + 10000L));

		int updateCount = contentDao.update(dbContent);
		Assert.assertEquals(1, updateCount);

		dbContent = contentDao.selectById(dbContent.getId());
		Assert.assertEquals("test2", dbContent.getName());
		Assert.assertEquals(Long.MIN_VALUE, dbContent.getFrameId());
		Assert.assertEquals("test value2", dbContent.getValue());
		Assert.assertEquals("unit test2", dbContent.getDescription());
		Assert.assertEquals(Long.MAX_VALUE, dbContent.getCreatorId());
		Assert.assertNotSame(createdDate.getTime(), dbContent.getCreatedDate().getTime());

		// delete
		int deleteCount = contentDao.deleteById(content.getId());
		Assert.assertEquals(1, deleteCount);
		Assert.assertNull(contentDao.selectById(content.getId()));
	}
}
