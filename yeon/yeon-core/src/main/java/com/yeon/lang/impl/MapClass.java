package com.yeon.lang.impl;

import com.yeon.YeonContext;
import com.yeon.lang.*;
import com.yeon.lang.Class;
import com.yeon.lang.clazz.ClassClass;
import com.yeon.util.LangUtils;

import java.util.List;

public class MapClass extends AbstractMapResource implements Class {
	private static final String PARAM_PROPERTY = "_cp";

    public static final String CLASS_RESOURCE_ID =  ResourceType.CLASS.buildId("p1");

    public static final MapClass NULL = new MapClass(){
    };

	public MapClass() {
	}

	public MapClass(String id) {
        super.setId(id);
	}

	@Override
	public Property getPropertyByName(String name) {
		return getPropertyByName(new LocalValue(LangUtils.getLocale(name).getLanguage(), name));
	}

	public Property getPropertyByName(LocalValue name) {
		for (Property property : getProperties()) {
			if (property.getNames().contains(name)) {
				return property;
			}
		}

		// TODO 자동 등록 옵션이 true일 경우에만 등록...
		// throw new PropertyNotFoundException(this, name);

		Property property = PropertyByReflectionBuilder.buildByName(this, name);
		resourceService.add(property);

		return property;
	}

	@Override
	public Property getPropertyById(String propertyId) {
		for (Property property : getProperties()) {
			if (property.getId().equals(propertyId)) {
				return property;
			}
		}

//        ResourceList prototypes = getPrototypes();
//        for(int i=0; i<prototypes.size(); i++  ) {
//            com.yeon.lang.Class prototype = (com.yeon.lang.Class)prototypes.get(i);
//            Property property = prototype.getPropertyById(propertyId);
//            if(property != null) {
//                return property;
//            }
//        }

        throw new PropertyNotFoundException(this, propertyId);
	}

	public void setProperties(List<? extends Property> properties) {
		data.put(PARAM_PROPERTY, properties);
	}

	@Override
	public List<Property> getProperties() {
		return getListDataValue(PARAM_PROPERTY);
	}

    @Override
    public String getTypeClassId() {
        return ClassClass.ID;
    }

    @Override
    public Class getTypeClass() {
        return BuiltinClass.CLASS.getTypeClass();
    }
}
