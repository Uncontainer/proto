package com.pulsarang.infra.config.propinfo;

import java.util.List;

import com.mysema.query.jpa.impl.JPAQuery;
import com.pulsarang.core.data.BaseCustomRepository;

public class ConfigPropertyInfoRepositoryImpl extends BaseCustomRepository<ConfigPropertyInfo, ConfigPropertyInfoId> implements
		ConfigPropertyInfoRepositoryCustom {

	QConfigPropertyInfo configPropertyInfo = QConfigPropertyInfo.configPropertyInfo;

	@Override
	public List<ConfigPropertyInfo> findConfigPropertyInfos(String configCategory) {
		JPAQuery query = createJPAQuery().from(configPropertyInfo).where(configPropertyInfo.id.configCategory.eq(configCategory));

		return query.list(configPropertyInfo);
	}
}
