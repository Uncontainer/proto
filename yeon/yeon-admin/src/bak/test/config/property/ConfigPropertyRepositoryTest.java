package com.pulsarang.infra.config.property;

import com.pulsarang.infra.config.ConfigEntityId;
import com.pulsarang.infra.config.prop.ConfigProperty;
import com.pulsarang.infra.config.prop.ConfigPropertyId;
import com.pulsarang.infra.config.prop.ConfigPropertyRepository;
import com.pulsarang.test.spring.SpringTestBase;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ConfigPropertyRepositoryTest extends SpringTestBase {

	@Autowired
	ConfigPropertyRepository tpRepository;

	@Test
	public void test() {
		ConfigEntityId configKey = new ConfigEntityId("category", "name");

		ConfigPropertyId id = new ConfigPropertyId(configKey, "dummyPropertyName");
		ConfigProperty property = new ConfigProperty(id, "dummyPropertyValue");
		tpRepository.save(property);
		int deleted = tpRepository.deleteAll(configKey);
		Assert.assertEquals(1, deleted);
	}
}
