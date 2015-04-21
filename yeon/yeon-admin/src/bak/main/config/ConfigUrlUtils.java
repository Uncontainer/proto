package com.pulsarang.infra.config;

import com.pulsarang.infra.config.propinfo.ConfigPropertyInfoId;

public class ConfigUrlUtils {
	public static final String getPropertyCreateUrl(ConfigEntityId id) {
		return "/config/property/create/" + id.getCategory() + "/" + id.getName();
	}

	public static final String getPropertyModifyUrl(ConfigEntityId id) {
		return "/config/property/modify/" + id.getCategory() + "/" + id.getName();
	}

	public static String getConfigDetailUrl(String solutionName, ConfigEntityId configEntityId) {
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("/config/read");
		urlBuilder.append("/").append(configEntityId.getCategory());
		urlBuilder.append("/").append(configEntityId.getName());
		urlBuilder.append("?solutionName=").append(solutionName);

		return urlBuilder.toString();
	}

	public static String getPropertyInfoListUrl(String configCategory) {
		String url = "/config/propertyInfo/list/";
		if (configCategory != null) {
			url += configCategory;
		}

		return url;
	}

	public static String getPropertyInfoReadUrl(ConfigPropertyInfoId id) {
		return "/config/propertyInfo/read/" + id.getConfigCategory() + "/" + id.getPropertyName();
	}

	public static String getPropertyInfoModifyUrl(ConfigPropertyInfoId id) {
		return "/config/propertyInfo/modify/" + id.getConfigCategory() + "/" + id.getPropertyName();
	}

	public static String getConfigCategoryModifyUrl(String categoryName) {
		return "/config/category/modify/" + categoryName;
	}

	public static String getConfigCategoryListUrl() {
		return "/config/category/list";
	}

}
