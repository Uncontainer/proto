package com.yeon.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.yeon.lang.impl.AbstractMapResource;
import com.yeon.lang.impl.MapResource;
import com.yeon.lang.resource.ResourceHierarchyService;
import com.yeon.lang.triple.Triple;
import com.yeon.lang.triple.TripleService;
import com.yeon.lang.value.locale.LocalValueEntity;
import com.yeon.lang.value.locale.LocalValueService;
import com.yeon.lang.value.locale.LocalValueType;

public abstract class AbstractMapResourceService<T extends AbstractMapResource> implements MapResourceService<T> {
	@Autowired
	private TripleService tripleService;

	@Autowired
	private LocalValueService localValueService;

	@Autowired
	private ResourceHierarchyService hierarchyService;

	protected final ResourceType resourceType;

	protected AbstractMapResourceService(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	@Override
	public T get(String resourceId) {
		T resource = getBasicInfo(resourceId);
		if (resource == null) {
			return null;
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
		Map<String, Object> values = resource.getValues();
		List<Property> properties = resource.getTypeClass().getProperties();
		for (Property property : properties) {
			Object object = values.get(property.getId());
			if (object != null) {
				String value;
				if (object instanceof Resource) {
					value = ((Resource) object).getId();
				} else {
					value = property.toString(object);
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

	protected abstract T getBasicInfo(String resourceId);
}
