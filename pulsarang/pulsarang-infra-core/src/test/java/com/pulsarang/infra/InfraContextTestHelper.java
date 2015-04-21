package com.pulsarang.infra;


public class InfraContextTestHelper {

	static InfraContext backup;

	public static void setInfraContext(InfraContext infraContext) {
		InfraContextFactory.INSTANCE = infraContext;
	}

	public static InfraContext getInfraContext() {
		return InfraContextFactory.INSTANCE;
	}

	public static void backup() {
		backup = InfraContextFactory.INSTANCE;
	}

	public static void restore() {
		InfraContextFactory.INSTANCE = backup;
	}
}
