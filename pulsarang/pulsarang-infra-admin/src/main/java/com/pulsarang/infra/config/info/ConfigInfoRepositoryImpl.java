package com.pulsarang.infra.config.info;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mysema.query.jpa.impl.JPAQuery;
import com.pulsarang.core.data.BaseCustomRepository;
import com.pulsarang.infra.config.ConfigEntityId;
import com.pulsarang.infra.config.category.ConfigCategory;

public class ConfigInfoRepositoryImpl extends BaseCustomRepository<ConfigInfoEntity, ConfigEntityId> implements ConfigInfoRepositoryCustom {
	QConfigInfoEntity configInfoEntity = QConfigInfoEntity.configInfoEntity;

	@Override
	public List<String> findSuperConfigNames(ConfigEntityId configEntityId) {
		JPAQuery query = createJPAQuery().from(configInfoEntity).where(configInfoEntity.id.eq(configEntityId));
		String superConfigNamesString = query.uniqueResult(configInfoEntity.superConfigNames);
		if (StringUtils.isEmpty(superConfigNamesString)) {
			return Collections.emptyList();
		}

		// TODO Tokenizing을 좀 더 상위 레벨로 올려야 할 것 같음.
		return Arrays.asList(StringUtils.split(superConfigNamesString, ", "));
	}

	@Override
	public List<String> findSubConfigNames(ConfigEntityId configEntityId) {
		JPAQuery query = createJPAQuery().from(configInfoEntity).where(configInfoEntity.id.category.eq(configEntityId.getCategory()),
				configInfoEntity.superConfigNames.like("%" + configEntityId.getName() + "%"));

		// TODO ConfigId를 돌려주는 것도 괜찮을 것 같음.
		return query.list(configInfoEntity.id.name);
	}

	@Override
	public List<String> findConfigNamesByCategory(ConfigCategory configCategory) {
		JPAQuery query = createJPAQuery().from(configInfoEntity).where(configInfoEntity.id.category.eq(configCategory.getName()));

		return query.list(configInfoEntity.id.name);
	}
}
