package com.naver.mage4j.core.mage.core.layout;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.model.app.Mage_Core_Model_App_Area.AreaType;
import com.naver.mage4j.core.mage.core.model.design.Mage_Core_Model_Design_Package;
import com.naver.mage4j.core.mage.core.model.design.ThemeType;
import com.naver.mage4j.core.mage.core.model.resource.layout.CoreLayoutUpdateRepository;

@Component
public class LayoutContext {
	@Autowired
	private CoreLayoutUpdateRepository layoutUpdateRepository;

	/**
	 * Retrieve layout updates by handle
	 *
	 * @param string $handle
	 * @param array $params
	 * @return string
	 */
	public String fetchUpdatesByHandle(String handle, Mage_Core_Model_Design_Package.Param params) {
		Mage_Core_Model_Design_Package designPackage = AppContext.getCurrent().getDesignPackage();
		Short storeId = AppContext.getCurrent().getStore().getStoreId();
		AreaType area = params.getArea() != null ? params.getArea() : designPackage.getArea();
		String packageName = params.getPackageName() != null ? params.getPackageName() : designPackage.getPackageName();
		String theme = params.getTheme() != null ? params.getTheme() : designPackage.getTheme(ThemeType.LAYOUT);

		List<String> xmls = layoutUpdateRepository.findXmlAll(storeId, handle, area, packageName, theme);

		return StringUtils.join(xmls, "");
	}
}
