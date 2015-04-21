package com.yeon;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class YeonContextFactoryBean implements FactoryBean<YeonContext>, InitializingBean {
	private String adminAddress;
	private String solutionName;
	private String projectName;

	@Override
	public void afterPropertiesSet() throws Exception {
		BasicYeonConfiguration yeonConfiguration = new BasicYeonConfiguration();
		yeonConfiguration.setInfraAdminAddress(adminAddress);
		yeonConfiguration.setSolutionName(solutionName);
		yeonConfiguration.setProjectName(projectName);

		YeonContextFactory.init(yeonConfiguration);
	}

	public String getAdminAddress() {
		return adminAddress;
	}

	public void setAdminAddress(String adminAddress) {
		this.adminAddress = adminAddress;
	}

	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public YeonContext getObject() throws Exception {
		return YeonContext.getYeonContext();
	}

	@Override
	public Class<?> getObjectType() {
		return YeonContext.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
