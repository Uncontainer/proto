package com.yeon.lang;

import com.yeon.util.MapModel;

public class ResourceGetCondition extends MapModel {
	private ResourceType resourceType;
	private LocalValue name;
	private String classId;
	private String resourceId;
	private boolean registIfNotExist;

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public LocalValue getName() {
		return name;
	}

	public void setName(LocalValue name) {
		this.name = name;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public boolean isRegistIfNotExist() {
		return registIfNotExist;
	}

	public void setRegistIfNotExist(boolean registIfNotExist) {
		this.registIfNotExist = registIfNotExist;
	}

	public static ResourceGetCondition createClassByAliasCondition(String alias) {
		ResourceGetCondition condition = new ResourceGetCondition();
		condition.resourceType = ResourceType.CLASS;
		condition.name = new LocalValue(NamedClassId.LOCALE, alias);

		return condition;
	}

	public static ResourceGetCondition createResourceByNameCondition(String locale, String name, String typeClassId) {
		ResourceGetCondition condition = new ResourceGetCondition();
		condition.resourceType = ResourceType.RESOURCE;
		condition.name = new LocalValue(locale, name);
		condition.classId = typeClassId;

		return condition;
	}

	public void setName(String locale, String value) {
		setName(new LocalValue(locale, value));
	}
}
