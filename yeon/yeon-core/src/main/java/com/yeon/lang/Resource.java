package com.yeon.lang;

import java.util.List;

public interface Resource {
	String getId();

    ResourceList getPrototypes();

	String getTypeClassId();

	Class getTypeClass();

	Object getPropertyObjectByName(String name);

	Object getPropertyObjectById(String id);

	List<LocalValue> getNames();

	List<LocalValue> getDescriptions();
}