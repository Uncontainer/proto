package com.yeon.lang.property;

import com.yeon.infra.util.SerialGenerator;
import com.yeon.lang.AbstractMapResourceService;
import com.yeon.lang.ResourceGetCondition;
import com.yeon.lang.ResourceType;
import com.yeon.lang.impl.MapProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapPropertyServiceImpl extends AbstractMapResourceService<MapProperty> implements MapPropertyService {
	@Autowired
	private MapPropertyDao propertyDao;

	@Autowired
	private SerialGenerator serialGenerator;

	public MapPropertyServiceImpl() {
		super(ResourceType.PROPERTY);
	}

	@Override
	protected MapProperty getBasicInfo(String propertyId) {
		MapProperty property = propertyDao.select(propertyId);

		return property;
	}

	@Override
	public List<MapProperty> listByClass(String classId) {
		return propertyDao.selectByClassId(classId);
	}

	@Override
	public void add(MapProperty property) {
		if (property.getId() != null) {
			throw new IllegalArgumentException("Registered property.(" + property.getId() + ")");
		}

		property.setId(ResourceType.PROPERTY.buildId(serialGenerator.next("property", 1)));
		propertyDao.insert(property);
		addBaseData(property, ResourceType.PROPERTY);
	}

	@Override
	public int remove(String propertyId) {
		return propertyDao.delete(propertyId);
	}

	@Override
	public int modify(MapProperty resource) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getId(ResourceGetCondition condition) {
		// TODO Auto-generated method stub
		return null;
	}
}
