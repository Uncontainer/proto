package com.naver.mage4j.core.mage.core.model.resource.layout;

import java.util.List;

import com.naver.mage4j.core.mage.core.model.app.Mage_Core_Model_App_Area.AreaType;

public interface CoreLayoutUpdateRepositoryCustom {
	List<String> findXmlAll(short storeId, String handle, AreaType area, String packageName, String theme);
}
