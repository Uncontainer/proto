package com.pulsarang.infra.config;

import com.pulsarang.infra.config.category.ConfigCategory;
import com.pulsarang.infra.config.category.ConfigCategoryPool;

public class ConfigId implements Comparable<ConfigId> {
	String name;
	String categoryName;

	String solutionName;
	String projectName;

	public ConfigId() {
	}

	public ConfigId(String categoryName, String name) {
		super();
		this.categoryName = categoryName;
		this.name = name;
	}

	public ConfigId(ConfigCategory category, String name) {
		super();
		this.categoryName = category.getName();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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

	public Class<? extends Config> getConfigClass() {
		ConfigCategory configCategory = ConfigCategoryPool.get(categoryName);
		if (configCategory == null) {
			return Config.class;
		} else {
			return configCategory.getConfigClass();
		}
	}

	@Override
	public String toString() {
		return name + "@" + categoryName;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		return toString().equals(obj.toString());
	}

	@Override
	public int compareTo(ConfigId o) {
		if (o == null) {
			return 1;
		}

		return toString().compareTo(o.toString());
	}

}
