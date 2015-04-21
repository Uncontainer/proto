package com.naver.mage4j.core.mage.core.model.app;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AreaContext {
	@Autowired
	private ApplicationContext applicationContext;

	private Map<Mage_Core_Model_App_Area.AreaType, Mage_Core_Model_App_Area> _areas = new EnumMap<Mage_Core_Model_App_Area.AreaType, Mage_Core_Model_App_Area>(Mage_Core_Model_App_Area.AreaType.class);

	public synchronized Mage_Core_Model_App_Area get(Mage_Core_Model_App_Area.AreaType areaType, Mage_Core_Model_App_Area.PartType part) {
		Mage_Core_Model_App_Area area = _areas.get(areaType);
		if (area == null) {
			area = (Mage_Core_Model_App_Area)applicationContext.getBean(Mage_Core_Model_App_Area.NAME, areaType);
			area.load(part);
			// TODO 캐싱 처리 추가.
			//			_areas.put(areaType, area);
		}

		return area;
	}
}
