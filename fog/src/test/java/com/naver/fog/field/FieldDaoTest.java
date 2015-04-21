package com.naver.fog.field;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.naver.fog.AbstractTestBase;
import com.naver.fog.Resource;

public class FieldDaoTest extends AbstractTestBase {
	@Autowired
	private FieldDao fieldDao;

	@Test
	public void crudTest() {
		Field field = new Field();
		field.setName("test");
		field.setFieldType(FieldType.INTEGER);
		field.setDescription("unit test");
		field.setCreatorId(Long.MAX_VALUE);
		field.setCreatedDate(new Date());

		// insert
		fieldDao.insert(field);
		Assert.assertNotSame(Resource.NULL_ID, field.getId());

		// select
		Field dbField = fieldDao.selectById(field.getId());
		Assert.assertEquals("test", dbField.getName());
		Assert.assertEquals(FieldType.INTEGER, dbField.getFieldType());
		Assert.assertEquals("unit test", dbField.getDescription());
		Assert.assertEquals(Long.MAX_VALUE, dbField.getCreatorId());
		Assert.assertNotNull(dbField.getCreatedDate());
		final Date createdDate = dbField.getCreatedDate();

		List<Field> fields = fieldDao.selectLatest(10);
		Assert.assertTrue(fields.size() >= 1);
		Assert.assertEquals("test", fields.get(0).getName());

		fields = fieldDao.selectByIds(Arrays.asList(dbField.getId()));
		Assert.assertEquals(1, fields.size());
		Assert.assertEquals("test", fields.get(0).getName());

		List<Long> fieldIds = fieldDao.selectIdsByNamePrefix("t", 1);
		Assert.assertEquals(1, fieldIds.size());

		// update
		dbField.setName("test2");
		dbField.setFieldType(FieldType.STRING);
		dbField.setDescription("unit test2");
		dbField.setCreatorId(Long.MIN_VALUE);
		dbField.setCreatedDate(new Date(createdDate.getTime() + 10000L));

		int updateCount = fieldDao.update(dbField);
		Assert.assertEquals(1, updateCount);

		dbField = fieldDao.selectById(dbField.getId());
		Assert.assertEquals("test2", dbField.getName());
		Assert.assertEquals(FieldType.STRING, dbField.getFieldType());
		Assert.assertEquals("unit test2", dbField.getDescription());
		Assert.assertEquals(Long.MAX_VALUE, dbField.getCreatorId());
		Assert.assertEquals(createdDate.getTime(), dbField.getCreatedDate().getTime());

		// delete
		int deleteCount = fieldDao.deleteById(field.getId());
		Assert.assertEquals(1, deleteCount);
		Assert.assertNull(fieldDao.selectById(field.getId()));
	}
}
