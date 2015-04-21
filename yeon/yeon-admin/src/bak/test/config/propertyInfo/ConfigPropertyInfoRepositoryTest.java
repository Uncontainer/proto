package com.pulsarang.infra.config.propertyInfo;

import com.pulsarang.infra.config.propinfo.ConfigPropertyInfo;
import com.pulsarang.infra.config.propinfo.ConfigPropertyInfoId;
import com.pulsarang.infra.config.propinfo.ConfigPropertyInfoRepository;
import com.pulsarang.test.spring.SpringTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ConfigPropertyInfoRepositoryTest extends SpringTestBase {

	@Autowired
	ConfigPropertyInfoRepository repository;

	@Test
	public void test() {
		ConfigPropertyInfoId id = new ConfigPropertyInfoId("123", "xx");
		ConfigPropertyInfo configPropertyInfo = repository.findOne(id);
		System.out.println(configPropertyInfo);
	}
}
