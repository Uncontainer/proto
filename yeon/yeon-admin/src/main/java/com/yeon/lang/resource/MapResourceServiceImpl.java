package com.yeon.lang.resource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.yeon.infra.util.SerialGenerator;
import com.yeon.lang.AbstractMapResourceService;
import com.yeon.lang.LocalValue;
import com.yeon.lang.ResourceGetCondition;
import com.yeon.lang.ResourceType;
import com.yeon.lang.impl.MapResource;
import com.yeon.lang.value.locale.LocalValueEntity;
import com.yeon.lang.value.locale.LocalValueService;
import com.yeon.lang.value.locale.LocalValueType;

@Service
public class MapResourceServiceImpl extends AbstractMapResourceService<MapResource> implements BasicMapResourceService {
	@Autowired
	private LocalValueService localValueService;

	@Autowired
	private SerialGenerator serialGenerator;

	@Autowired
	private MapResourceDao dao;

	public MapResourceServiceImpl() {
		super(ResourceType.RESOURCE);
	}

	@Override
	protected MapResource getBasicInfo(String resourceId) {
		MapResource resource = dao.select(resourceId);

		return resource;
	}

	@Override
	public void add(MapResource resource) {
		if (resource.getId() != null) {
			throw new IllegalArgumentException("Registered resource.(" + resource.getId() + ")");
		}

		resource.setId(ResourceType.PROPERTY.buildId(serialGenerator.next("resource", 1)));
		dao.insert(resource);

		super.addBaseData(resource, ResourceType.PROPERTY);
		super.addTriples(resource);
	}

	@Override
	public int modify(MapResource resource) {
		// TODO 속성 및 prototype 처리 추가.
		return dao.update(resource);
	}

	@Override
	public Page<MapResource> search(ResourceSearchCriteria criteria, Pageable pageable) {
		switch (criteria.getSearchType()) {
		case CLASS:
			return dao.selectByTypeClassId(criteria.getKeyword(), pageable);
		case ID:
			MapResource resource = dao.select(criteria.getKeyword());
			if (resource == null) {
				return new PageImpl<MapResource>(Collections.<MapResource> emptyList(), pageable, 0);
			} else {
				return new PageImpl<MapResource>(Arrays.asList(resource), pageable, 1);
			}
		case NAME:
			Page<LocalValueEntity> names = localValueService.listByPrefix(LocalValueType.NAME, ResourceType.RESOURCE, criteria.getKeyword(), pageable);
			// total = names.getSize();
			// content = new ArrayList<SimpleResource>(names.getSize());
			// for (LocaleValueEntity entity : names) {
			// content.add(new SimpleResource(entity.getResourceId(), entity.getValue(), null));
			// }
			return null;
		default:
			throw new IllegalArgumentException("Unsupported resource search type.(" + criteria.getSearchType() + ")");
		}
	}

	@Override
	public String getIdByName(LocalValue name, String typeClassId) {
		if (typeClassId == null || typeClassId.isEmpty()) {
			return null;
		}

		List<LocalValueEntity> localNames = localValueService.listByValue(LocalValueType.NAME, ResourceType.RESOURCE, name.getLocale(), name.getValue());
		if (localNames.isEmpty()) {
			return null;
		}

		for (LocalValueEntity localName : localNames) {
			// TODO 성능 개선
			MapResource resource = get(localName.getResourceId());
			if (typeClassId.equals(resource.getTypeClassId())) {
				return resource.getId();
			}
		}

		return null;
	}

	@Override
	public int remove(String resourceId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getId(ResourceGetCondition condition) {
		if (condition.getResourceId() != null) {
			return condition.getResourceId();
		}

		ResourceType resourceType = condition.getResourceType();
		if (resourceType == null) {
			throw new IllegalArgumentException("ResourceType is required.");
		}

		return getIdByName(condition.getName(), condition.getClassId());
	}
}
