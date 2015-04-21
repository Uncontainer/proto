package com.yeon.lang.resource;

import com.yeon.lang.*;
import com.yeon.lang.impl.MapClass;
import com.yeon.lang.impl.MapResource;
import com.yeon.lang.triple.Triple;
import com.yeon.lang.triple.TripleService;
import com.yeon.lang.value.locale.LocalValueEntity;
import com.yeon.lang.value.locale.LocalValueService;
import com.yeon.lang.value.locale.LocalValueType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractMapResourceService<T extends MapResource> {
	@Autowired
	private TripleService tripleService;

	@Autowired
	private LocalValueService localValueService;

	@Autowired
	private ResourceHierarchyService hierarchyService;

	protected abstract MapClass getClass(String classId);

	protected abstract T getBasicInfo(String resourceId);

	protected final ResourceType resourceType;

	protected AbstractMapResourceService(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public T get(String resourceId) {
		T resource = getBasicInfo(resourceId);

		List<LocalValueEntity> localNameEntities = localValueService.listByResourceId(LocalValueType.NAME, resource.getId());
		if (!localNameEntities.isEmpty()) {
			List<LocalValue> localNames = new ArrayList<LocalValue>(localNameEntities.size());
			for (LocalValueEntity localNameEntity : localNameEntities) {
				LocalValue localName = new LocalValue(localNameEntity.getLocale(), localNameEntity.getValue());
				localNames.add(localName);
			}
			resource.setNames(localNames);
		}

		List<Triple> triples = tripleService.listByResourceId(resource.getId());
		for (Triple triple : triples) {
			resource.setPropertyObjectById(triple.getPropertyId(), triple.getObject());
		}

		List<String> prototypeIds = hierarchyService.getParentIds(resource.getId());
		if (!prototypeIds.isEmpty()) {
			resource.setPrototypes(new ResourceList(prototypeIds));
		}

		return resource;
	}

	protected void addBaseData(Resource resource, ResourceType resourceType) {
		for (LocalValue localName : resource.getNames()) {
			LocalValueEntity entity = new LocalValueEntity(resourceType, resource.getId(), null, localName.getLocale(), localName.getValue());
			localValueService.add(LocalValueType.NAME, entity);
		}

		for (LocalValue localDescription : resource.getDescriptions()) {
			LocalValueEntity entity = new LocalValueEntity(resourceType, resource.getId(), null, localDescription.getLocale(), localDescription.getValue());
			localValueService.add(LocalValueType.NAME, entity);
		}

		ResourceList prototypes = resource.getPrototypes();
		if (!prototypes.isEmpty()) {
			hierarchyService.add(resource.getId(), prototypes.getResourceIds());
		}
	}

	protected void fillBaseData(MapResource resource) {
		List<String> prototypeIds = hierarchyService.getParentIds(resource.getId());
		if (!prototypeIds.isEmpty()) {
			resource.setPrototypes(new ResourceList(prototypeIds));
		}

		List<LocalValueEntity> localNameEntities = localValueService.listByResourceId(LocalValueType.NAME, resource.getId());
		if (!localNameEntities.isEmpty()) {
			List<LocalValue> localNames = new ArrayList<LocalValue>(localNameEntities.size());
			for (LocalValueEntity localNameEntity : localNameEntities) {
				LocalValue localName = new LocalValue(localNameEntity.getLocale(), localNameEntity.getValue());
				localNames.add(localName);
			}
			resource.setNames(localNames);
		}
	}

	protected void addTriples(MapResource resource) {
		Map<String, Object> values = ((MapResource) resource).getValues();
		List<Property> properties = resource.getTypeClass().getProperties();
		for (Property property : properties) {
			Object object = values.get(property.getId());
			if (object != null) {
				String value;
				if (object instanceof Resource) {
					value = ((Resource) object).getId();
				} else {
					// TODO type별 serialization(string 변환) 기능 추가.
					value = object.toString();
				}
				Triple triple = new Triple(resource.getId(), property.getId(), value);

				tripleService.add(triple);
			}
		}
	}

	protected void fillTriples(MapResource resource) {
		List<Triple> triples = tripleService.listByResourceId(resource.getId());
		for (Triple triple : triples) {
			resource.setPropertyObjectById(triple.getPropertyId(), triple.getObject());
		}
	}
}
