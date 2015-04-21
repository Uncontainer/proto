package com.yeon.lang.resource;

import com.yeon.lang.LocalValue;
import com.yeon.lang.MapResourceService;
import com.yeon.lang.impl.MapResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BasicMapResourceService extends MapResourceService<MapResource> {
	Page<MapResource> search(ResourceSearchCriteria criteria, Pageable pageable);

	String getIdByName(LocalValue name, String typeClassId);
}
