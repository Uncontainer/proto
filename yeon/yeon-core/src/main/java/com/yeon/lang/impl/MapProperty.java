package com.yeon.lang.impl;

import com.yeon.lang.BuiltinClass;
import com.yeon.lang.Class;
import com.yeon.lang.Property;

public class MapProperty extends AbstractMapResource implements Property {
	private static final String PARAM_DOMAIN = "_pd";
	private static final String PARAM_RANGE = "_pr";

	public String getDomainClassId() {
		return (String) data.get(PARAM_DOMAIN);
	}

	public void setDomainClassId(String domainClassId) {
		data.put(PARAM_DOMAIN, domainClassId);
	}

	public String getRangeClassId() {
		return (String) data.get(PARAM_RANGE);
	}

	public void setRangeClassId(String rangeClassId) {
		data.put(PARAM_RANGE, rangeClassId);
	}

	@Override
	public Class getDomain() {
		return (Class) resourceService.get(getDomainClassId());
	}

	// public void setDomain(Class domain) {
	// setValue(PARAM_DOMAIN, domain);
	// }

	@Override
	public Class getRange() {
		return (Class) resourceService.get(getRangeClassId());
	}

	// public void setRange(Class range) {
	// setValue(PARAM_RANGE, range);
	// }

	@Override
	public String toString(Object object) {
		// TODO type별 serialization(string 변환) 기능 추가.
		return object != null ? object.toString() : null;
	}

    @Override
    public String getTypeClassId() {
        return BuiltinClass.PROPERTY.getId();
    }

    @Override
    public Class getTypeClass() {
        return BuiltinClass.PROPERTY.getTypeClass();
    }
}
