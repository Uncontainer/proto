package com.pulsarang.infra.config.category;

import com.mysema.query.jpa.impl.JPAQuery;
import com.pulsarang.core.data.BaseCustomRepository;

import java.util.List;

public class ConfigCategoryRepositoryImpl extends BaseCustomRepository<ConfigCategoryEntity, String> implements ConfigCategoryRepositoryCustom {
	QConfigCategoryEntity configCategory = QConfigCategoryEntity.configCategoryEntity;

	@Override
	public List<String> findCategoryNames() {
		JPAQuery query = createJPAQuery().from(configCategory).distinct();
		return query.list(configCategory.name);
	}

}
