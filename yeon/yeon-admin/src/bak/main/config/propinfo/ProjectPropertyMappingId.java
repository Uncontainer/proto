package com.pulsarang.infra.config.propinfo;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ProjectPropertyMappingId implements Serializable {
	public ProjectPropertyMappingId() {
	}

	public ProjectPropertyMappingId(String configCategory, String propertyName, String projectName) {
		this.configPropertyInfoId = new ConfigPropertyInfoId(configCategory, propertyName);
		this.projectName = projectName;
	}

	private static final long serialVersionUID = 3826064580354128389L;

	@Column(name = "pjt_nm", length = 20)
	String projectName;

	@Column
	ConfigPropertyInfoId configPropertyInfoId;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public ConfigPropertyInfoId getConfigPropertyInfoId() {
		return configPropertyInfoId;
	}

	public void setConfigPropertyInfoId(ConfigPropertyInfoId configPropertyInfoId) {
		this.configPropertyInfoId = configPropertyInfoId;
	}

}
