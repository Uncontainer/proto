package com.yeon.lang.impl;

import com.yeon.YeonContext;
import com.yeon.lang.*;
import com.yeon.util.MapModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractMapResource extends MapModel implements Resource {
    protected static final String PARAM_ID = "_i";
    protected static final String PARAM_NAME = "_n";
	protected static final String PARAM_CLASS = "_c";
    protected static final String PARAM_DESCRIPTION = "_d";
    protected static final String PARAM_PROTOTYPE = "_p";

    // TODO 생성자가 호출되기 전에 할당이 될 수 있는 매커니즘 추가.
    protected transient ResourceService resourceService = YeonContext.getResourceContext().getResourceService();

    protected AbstractMapResource() {
        super();
    }

    protected AbstractMapResource(Map<String,Object> properties) {
        super(properties);
    }

    @Override
	public String getId() {
		return (String) data.get(PARAM_ID);
	}

	public void setId(String id) {
		data.put(PARAM_ID, id);
	}

	@Override
	public List<LocalValue> getNames() {
		return getListDataValue(PARAM_NAME);
	}

	public void setNames(List<LocalValue> localNames) {
		data.put(PARAM_NAME, localNames);
	}

	@Override
	public List<LocalValue> getDescriptions() {
		return getListDataValue(PARAM_DESCRIPTION);
	}

	public void setDescriptions(List<LocalValue> descriptions) {
		data.put(PARAM_DESCRIPTION, descriptions);
	}

	@Override
	public ResourceList getPrototypes() {
		ResourceList prototypes = (ResourceList) data.get(PARAM_PROTOTYPE);
		if (prototypes == null) {
			return ResourceList.EMPTY;
		}

		return prototypes;
	}

	public void setPrototypes(ResourceList prototypes) {
		data.put(PARAM_PROTOTYPE, prototypes);
	}

	@Override
	public Object getPropertyObjectByName(String name) {
		return getPropertyObject(getTypeClass().getPropertyByName(name));
	}

	public void setPropertyObjectByName(String name, Object value) {
		setPropertyObject(getTypeClass().getPropertyByName(name), value);
	}

	@Override
	public Object getPropertyObjectById(String id) {
		return getPropertyObject(getTypeClass().getPropertyById(id));
	}

	public void setPropertyObjectById(String id, Object value) {
		Property property = getTypeClass().getPropertyById(id);
		if (property == null) {
			throw new IllegalArgumentException("Undefined property id.(" + id + ")");
		}

		setPropertyObject(property, value);
	}

	@Override
	public void setValue(String name, Object value) {
		Property property = getTypeClass().getPropertyByName(name);
		if (property == null) {
			throw new IllegalArgumentException("Undefined property name.(" + name + ")");
		}

		setPropertyObject(property, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getValue(String name) {
		return (T) getPropertyObjectByName(name);
	}

	@Override
	public <T> T getValue(String name, T defaultValue) {
		T value = getValue(name);
		return value != null ? value : defaultValue;
	}

	public Object getPropertyObject(Property property) {
		Object object = data.get(property.getId());
		if (object == null) {
			ResourceList prototypes = getPrototypes();
			for (int i = 0; i < prototypes.size(); i++) {
				// TODO type 검사 추가.
			    object = ((AbstractMapResource) prototypes.get(i)).getPropertyObject(property);
				if (object != null) {
                    break;
				}
			}
		}

		if (object instanceof ResourceIdentifiable) {
			return resourceService.get(((ResourceIdentifiable) object).getResourceId());
		} else if (object instanceof ResourceList) {
			((ResourceList) object).setResourceService(resourceService);
			return object;
		}

		return object;
	}

	public void setPropertyObject(Property property, Object value) {
		data.put(property.getId(), value);
	}

	protected <T> List<T> getListDataValue(String key) {
		@SuppressWarnings("unchecked")
		List<T> list = (List<T>) data.get(key);

		if (list == null) {
			return Collections.emptyList();
		}

		return list;
	}

	public void replaceWith(Resource resource) {
		if (resource instanceof AbstractMapResource) {
			data = new HashMap<String, Object>(((AbstractMapResource) resource).data);
		}

		throw new IllegalArgumentException("Require MapResource.");
	}

	public void toNull() {
		data = null;
	}
}
