package com.naver.mage4j.core.mage.core.model.resource.layout;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.jpa.impl.JPAQuery;
import com.naver.mage4j.core.mage.core.model.app.Mage_Core_Model_App_Area.AreaType;
import com.naver.mage4j.core.mage.core.model.resource.BaseCustomRepository;

//@Repository
public class CoreLayoutUpdateRepositoryImpl extends BaseCustomRepository<CoreLayoutUpdate, Integer> implements CoreLayoutUpdateRepositoryCustom {
	final QCoreLayoutUpdate layoutUpdate = QCoreLayoutUpdate.coreLayoutUpdate;
	final QCoreLayoutLink layoutLink = QCoreLayoutLink.coreLayoutLink;

	@Override
	public List<String> findXmlAll(short storeId, String handle, AreaType area, String packageName, String theme) {
		JPAQuery query = createJPAQuery().from(layoutLink).where(
			layoutLink.coreStore.storeId.in(Arrays.asList((short)0, storeId))
			, layoutLink.area.eq(area.getCode())
			, layoutLink.package_.eq(packageName)
			, layoutLink.theme.eq(theme)
			, layoutLink.coreLayoutUpdate.handle.eq(handle))
			.orderBy(layoutLink.coreLayoutUpdate.sortOrder.asc());

		return query.list(layoutLink.coreLayoutUpdate.xml);
	}
}
