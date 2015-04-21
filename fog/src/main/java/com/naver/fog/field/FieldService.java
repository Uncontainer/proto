package com.naver.fog.field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.fog.AbstractResourceService;

@Service
public class FieldService extends AbstractResourceService<Field> {
	@Autowired
	public void setFieldDao(FieldDao fieldDao) {
		this.resourceDao = fieldDao;
	}
}
