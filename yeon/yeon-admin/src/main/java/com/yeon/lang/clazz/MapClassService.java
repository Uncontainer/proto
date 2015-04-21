package com.yeon.lang.clazz;

import com.yeon.lang.MapResourceService;
import com.yeon.lang.SimpleResource;
import com.yeon.lang.impl.MapClass;
import org.springframework.data.domain.Page;

public interface MapClassService extends MapResourceService<MapClass> {
	Page<SimpleResource> search(ClassSearchCriteria criteria);

	String getIdByAlias(String alias);
}
