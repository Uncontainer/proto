package com.pulsarang;

import org.junit.BeforeClass;

import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.PropertyFileInfraConfiguration;

public class InfraCoreTestBase {
	@BeforeClass
	public static void initInfraContext() {
		if (InfraContextFactory.isInitialized()) {
			return;
		}

		InfraContextFactory.init(new PropertyFileInfraConfiguration());
	}
}
