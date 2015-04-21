package com.pulsarang.infra.config.propinfo;

import com.mysema.query.jpa.impl.JPAQuery;
import com.pulsarang.core.data.BaseCustomRepository;

import java.util.List;

public class ConfigPropertyInfoRepositoryImpl extends BaseCustomRepository<ConfigPropertyInfo, ConfigPropertyInfoId> implements
		ConfigPropertyInfoRepositoryCustom {

	QConfigPropertyInfo configPropertyInfo = QConfigPropertyInfo.configPropertyInfo;

	@Override
	public List<ConfigPropertyInfo> findConfigPropertyInfos(String configCategory) {
		JPAQuery query = createJPAQuery().from(configPropertyInfo).where(configPropertyInfo.id.configCategory.eq(configCategory));

		return query.list(configPropertyInfo);
	}
}
