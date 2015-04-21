package com.yeon.lang.property;

import com.yeon.lang.MapResourceService;
import com.yeon.lang.impl.MapProperty;

import java.util.List;

public interface MapPropertyService extends MapResourceService<MapProperty> {
	List<MapProperty> listByClass(String classId);
}
