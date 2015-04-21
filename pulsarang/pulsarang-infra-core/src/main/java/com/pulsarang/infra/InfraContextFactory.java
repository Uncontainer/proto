package com.pulsarang.infra;

public class InfraContextFactory {
	static InfraContext INSTANCE;

	public static void init(String infraAdminAddress, String solutionName, String projectName) {
		BasicInfraConfiguration configuration = new BasicInfraConfiguration();
		configuration.setInfraAdminAddress(infraAdminAddress);
		configuration.setSolutionName(solutionName);
		configuration.setProjectName(projectName);

		init(configuration);
	}

	public synchronized static void init(InfraConfiguration infraConfiguration) {
		if (INSTANCE != null) {
			throw new IllegalStateException("InfraContext has already initialized.");
		}

		INSTANCE = new InfraContext(infraConfiguration);

		INSTANCE.init();

		// InfraConfigContext.destroy()를 명시적으로 호출해야 하지만, 사용자가 누락했을 경우에 대한 대비책.
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				InfraContextFactory.destroy();
			}
		});
	}

	public synchronized static void destroy() {
		if (INSTANCE != null) {
			INSTANCE.close();
			INSTANCE = null;
		}
	}

	public synchronized static boolean isInitialized() {
		return INSTANCE != null;
	}

	public synchronized static InfraContext getInfraContext() {
		if (INSTANCE == null) {
			// init(new PropertyFileInfraConfiguration());
			throw new IllegalStateException("InfraContext has not initialized.");
		}

		return INSTANCE;
	}
}
