package com.yeon.lang.clazz;

import com.yeon.infra.util.SerialGenerator;
import com.yeon.lang.*;
import com.yeon.lang.impl.MapClass;
import com.yeon.lang.impl.MapProperty;
import com.yeon.lang.property.MapPropertyService;
import com.yeon.lang.value.locale.LocalValueEntity;
import com.yeon.lang.value.locale.LocalValueService;
import com.yeon.lang.value.locale.LocalValueType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class MapClassServiceImpl extends AbstractMapResourceService<MapClass> implements MapClassService {
	@Autowired
	private MapClassDao dao;

	@Autowired
	private LocalValueService localValueService;

	@Autowired
	private SerialGenerator serialGenerator;

	@Autowired
	private MapPropertyService propertyService;

	public MapClassServiceImpl() {
		super(ResourceType.CLASS);
	}

	@Override
	public MapClass get(String classId) {
		for (BuiltinClass builtinClass : BuiltinClass.values()) {
			if (classId.equals(builtinClass.getId())) {
				return builtinClass.getTypeClass();
			}
		}

		return super.get(classId);
	}

	@Override
	protected MapClass getBasicInfo(String classId) {
		MapClass clazz = dao.select(classId);
		if (clazz == null) {
			return null;
		}

		List<MapProperty> properties = propertyService.listByClass(classId);
		clazz.setProperties(properties);

		return clazz;
	}

	@Override
	public void add(MapClass clazz) {
		if (clazz.getId() != null) {
			throw new IllegalArgumentException("Registered class.(" + clazz.getId() + ")");
		}

		clazz.setId(ResourceType.CLASS.buildId(serialGenerator.next("class", 1)));
		dao.insert(clazz);

		addBaseData(clazz, ResourceType.CLASS);
	}

	@Override
	public int remove(String classId) {
		return dao.delete(classId);
	}

	@Override
	public Page<SimpleResource> search(ClassSearchCriteria criteria) {
		Pageable pageable = new PageRequest(0, 10);
		List<SimpleResource> content;
		long total;
		switch (criteria.getType()) {
		case ID:
			MapClass clazz = dao.select(criteria.getKeyword());
			if (clazz == null) {
				content = Collections.<SimpleResource> emptyList();
				total = 0L;
			} else {
				List<LocalValueEntity> localNames = localValueService.listByResourceId(LocalValueType.NAME, clazz.getId());
				String name = null;
				for (LocalValueEntity entity : localNames) {
					// TODO locale에 따른 이름 셋팅 추가.
					name = entity.getValue();
				}
				content = Arrays.asList(new SimpleResource(clazz.getId(), name, null));
				total = 1L;
			}
			break;
		case NAME:
			Page<LocalValueEntity> localNames = localValueService.listByPrefix(LocalValueType.NAME, ResourceType.CLASS, criteria.getKeyword(), pageable);
			total = localNames.getSize();
			content = new ArrayList<SimpleResource>(localNames.getSize());
			for (LocalValueEntity localName : localNames) {
				content.add(new SimpleResource(localName.getResourceId(), localName.getValue(), null));
			}
			break;
		default:
			throw new IllegalArgumentException("Unsupported class search type.(" + criteria.getType() + ")");
		}

		return new PageImpl<SimpleResource>(content, pageable, total);
	}

	@Override
	public String getIdByAlias(String name) {
		List<LocalValueEntity> localNames = localValueService.listByValue(LocalValueType.NAME, ResourceType.CLASS, NamedClassId.LOCALE, name);
		if (localNames.isEmpty()) {
			return null;
		} else if (localNames.size() > 1) {
			throw new IllegalStateException("Duplicatate class name.(" + name + ")");
		}

		return localNames.get(0).getResourceId();
	}

	@Override
	public int modify(MapClass resource) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getId(ResourceGetCondition condition) {
		// TODO Auto-generated method stub
		return null;
	}
}
